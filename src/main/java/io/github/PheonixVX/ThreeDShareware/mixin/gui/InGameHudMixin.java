package io.github.PheonixVX.ThreeDShareware.mixin.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.options.AttackIndicator;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Random;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

	@Shadow protected abstract PlayerEntity getCameraPlayer ();
	@Shadow private int scaledWidth;
	@Shadow private int scaledHeight;
	@Shadow @Final private MinecraftClient client;
	@Shadow @Final private static Identifier WIDGETS_TEXTURE;
	@Shadow protected abstract void renderHotbarItem (int x, int y, float tickDelta, PlayerEntity player, ItemStack stack);
	@Shadow @Final private Random random;
	@Shadow public abstract int getTicks ();
	@Shadow public abstract TextRenderer getFontRenderer ();

	private static final Identifier HOT_BAR_TEXTURE = new Identifier("textures/gui/hotbar.png");

	/**
	 * @author PheonixVX
	 * @reason There isn't a way to make this compatible, so shut up and take my mixin
	 */
	@Overwrite
	public void renderHotbar(float tickDelta, MatrixStack matrices) {
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity == null) {
			return;
		}

		int width = this.scaledWidth / 2;
		int hotBarWidth = width - 128;
		int height = this.scaledHeight - 64;
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.client.getTextureManager().bindTexture(HOT_BAR_TEXTURE);
		float offset = this.getZOffset();
		this.setZOffset(-660);
		this.drawTexture(matrices, hotBarWidth, height, 0, 64, 256, 64);
		RenderSystem.pushMatrix();
		RenderSystem.translatef(0.0f, 0.0f, -200.0f);
		int time = (int)(Util.getMeasuringTimeMs() / 2000L % 4L);
		float[] ticks = new float[]{-20.0f, 0.0f, 20.0f, 0.0f};
		if (this.client.getEntityRenderDispatcher().camera != null) {
			InventoryScreen.drawEntity(hotBarWidth + 128, height + 135, 64, ticks[time], 0.0f, playerEntity);
		}
		RenderSystem.popMatrix();
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.client.getTextureManager().bindTexture(HOT_BAR_TEXTURE);
		this.setZOffset(-90);
		this.drawTexture(matrices, hotBarWidth, height, 0, 0, 256, 64);
		this.client.getTextureManager().bindTexture(WIDGETS_TEXTURE);
		int slotX = playerEntity.inventory.selectedSlot % 3;
		int slotY = playerEntity.inventory.selectedSlot / 3;
		this.drawTexture(matrices,hotBarWidth + 194 + slotX * 20 - 1, height + 2 + slotY * 20 - 1, 0, 22, 24, 22);
		this.setZOffset((int) offset);
		RenderSystem.enableRescaleNormal();
		RenderSystem.enableBlend();
		//RenderSystem.blendFuncSeparate(RenderSystem.SrcFactor.SRC_ALPHA, RenderSystem.DstFactor.ONE_MINUS_SRC_ALPHA, RenderSystem.SrcFactor.ONE, RenderSystem.DstFactor.ZERO);
		RenderSystem.defaultBlendFunc();
		DiffuseLighting.enableGuiDepthLighting(); // TODO: enableForItems()?
		for (int i = 0; i < 9; ++i) {
			int var12 = hotBarWidth + 194 + i % 3 * 20 + 2;
            int var13 = height + 2 + i / 3 * 20 + 2;
			this.renderHotbarItem(var12, var13, tickDelta, playerEntity, playerEntity.inventory.main.get(i));
		}
		if (this.client.options.attackIndicator == AttackIndicator.HOTBAR) {
			float var15 = this.client.player.getAttackCooldownProgress(0.0F);
			if (var15 < 1.0F) {
				int var12 = height + 2 + slotY * 20;
				int var13 = hotBarWidth + 194 + slotX * 22;
				this.client.getTextureManager().bindTexture(DrawableHelper.GUI_ICONS_TEXTURE);
				int var14 = (int)(var15 * 19.0F);
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.drawTexture(matrices, var13, var12, 0, 94, 18, 18);
				this.drawTexture(matrices, var13, var12 + 18 - var14, 18, 112 - var14, 18, var14);
			}
		}

		DiffuseLighting.disable();
		RenderSystem.disableRescaleNormal();
		RenderSystem.disableBlend();
	}

	/**
	 * @author PheonixVX
	 * @reason To make it not look stupid
	 */
	@Overwrite
	private void renderStatusBars(MatrixStack matrices) {
		PlayerEntity player = this.getCameraPlayer();
		if (player != null) {
			int var2 = MathHelper.ceil(player.getHealth());
			this.random.setSeed(this.getTicks() * 312871L);
			HungerManager var3 = player.getHungerManager();
			int var4 = var3.getFoodLevel();
			int var5 = this.scaledWidth / 2;
			int var6 = var5 - 91;
			int var7 = var5 + 91;
			int var8 = this.scaledHeight - 74;
			int var9 = MathHelper.ceil(player.getAbsorptionAmount());
			this.client.getProfiler().push("armor");
			int var10 = player.getArmor();
			String var11 = String.format("%02d%%", var10 * 100 / 20);
			int var12 = this.getFontRenderer().getWidth(var11);
			int var13 = var5 - 128;
			int var14 = this.scaledHeight - 64;
			this.getFontRenderer().draw(matrices, var11, (float)(var13 + 25 + (23 - var12)), (float)(var14 + 41), -1);
			this.client.getProfiler().swap("health");
			float var15 = player.getMaxHealth();
			String var16 = String.format("%02.0f%%", (float)(var2 + var9) / var15 * 100.0F);
			int var17 = this.getFontRenderer().getWidth(var16);
			this.getFontRenderer().draw(matrices, var16, (float)(var13 + 25 + (23 - var17)), (float)(var14 + 8), var9 > 0 ? -256 : -65536);
			this.client.getTextureManager().bindTexture(HOT_BAR_TEXTURE);
			int var18 = var8 - 10;
			this.client.getProfiler().swap("food");
			int var19 = 32 * var4 / 20;
			this.drawTexture(matrices, var13 + 161, var14 + 14, 0, 128, 32, var19);
			var18 -= 10;
			this.client.getTextureManager().bindTexture(DrawableHelper.GUI_ICONS_TEXTURE);
			this.client.getProfiler().swap("air");
			int var20 = player.getAir();
			int var21 = player.getMaxAir();
			if (player.world.getFluidState(new BlockPos(player.getPos())).isIn(FluidTags.WATER) || var20 < var21) {
				int var23 = MathHelper.ceil((double)(var20 - 2) * 10.0D / (double)var21);
				int var24 = MathHelper.ceil((double)var20 * 10.0D / (double)var21) - var23;

				for(int var25 = 0; var25 < var23 + var24; ++var25) {
					if (var25 < var23) {
						this.drawTexture(matrices, var7 - var25 * 8 - 9, var18, 16, 18, 9, 9);
					} else {
						this.drawTexture(matrices, var7 - var25 * 8 - 9, var18, 25, 18, 9, 9);
					}
				}
			}

			this.client.getProfiler().pop();
		}
	}

	@ModifyArg(
		method = "renderExperienceBar",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"),
		index = 2
	)
	private int modifyYPosition(int original) {
		return this.scaledHeight - 70;
	}
}
