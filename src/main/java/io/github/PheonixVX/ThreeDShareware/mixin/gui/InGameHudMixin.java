package io.github.PheonixVX.ThreeDShareware.mixin.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
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
	@Shadow protected abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);
	@Shadow @Final private Random random;
	@Shadow public abstract int getTicks ();
	@Shadow public abstract TextRenderer getTextRenderer();

	private static final Identifier HOT_BAR_TEXTURE = new Identifier("textures/gui/hotbar.png");

	/**
	 * @author PheonixVX
	 * @reason There isn't a way to make this compatible, so shut up and take my mixin
	 */
	@Overwrite
	private void renderHotbar(float tickDelta, MatrixStack matrices) {
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity == null) {
			return;
		}

		int width = this.scaledWidth / 2;
		int hotBarWidth = width - 128;
		int height = this.scaledHeight - 64;
		RenderSystem.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
		this.client.getTextureManager().bindTexture(HOT_BAR_TEXTURE);
		float offset = this.getZOffset();
		this.setZOffset(-660);
		this.drawTexture(matrices, hotBarWidth, height, 0, 64, 256, 64);
		matrices.push();
		matrices.translate(0.0f, 0.0f, -200.0f);
		int time = (int)(Util.getMeasuringTimeMs() / 2000L % 4L);
		float[] ticks = new float[]{-20.0f, 0.0f, 20.0f, 0.0f};
		if (this.client.getEntityRenderDispatcher().camera != null) {
			InventoryScreen.drawEntity(hotBarWidth + 128, height + 135, 64, ticks[time], 0.0f, playerEntity);
		}
		matrices.pop();
		RenderSystem.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
		this.client.getTextureManager().bindTexture(HOT_BAR_TEXTURE);
		this.setZOffset(-90);
		this.drawTexture(matrices, hotBarWidth, height, 0, 0, 256, 64);
		this.client.getTextureManager().bindTexture(WIDGETS_TEXTURE);
		int slotX = playerEntity.getInventory().selectedSlot % 3;
		int slotY = playerEntity.getInventory().selectedSlot / 3;
		this.drawTexture(matrices,hotBarWidth + 194 + slotX * 20 - 1, height + 2 + slotY * 20 - 1, 0, 22, 24, 22);
		this.setZOffset((int) offset);
		//RenderSystem.enableRescaleNormal();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		DiffuseLighting.enableGuiDepthLighting(); // TODO: enableForItems()?
		for (int i = 0; i < 9; ++i) {
			int var12 = hotBarWidth + 194 + i % 3 * 20 + 2;
            int var13 = height + 2 + i / 3 * 20 + 2;
			this.renderHotbarItem(var12, var13, tickDelta, playerEntity, playerEntity.getInventory().main.get(i), i);
		}
		if (this.client.options.attackIndicator == AttackIndicator.HOTBAR) {
			float var15 = this.client.player.getAttackCooldownProgress(0.0F);
			if (var15 < 1.0F) {
				int var12 = height + 2 + slotY * 20;
				int var13 = hotBarWidth + 194 + slotX * 22;
				this.client.getTextureManager().bindTexture(DrawableHelper.GUI_ICONS_TEXTURE);
				int var14 = (int)(var15 * 19.0F);
				RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
				this.drawTexture(matrices, var13, var12, 0, 94, 18, 18);
				this.drawTexture(matrices, var13, var12 + 18 - var14, 18, 112 - var14, 18, var14);
			}
		}

		DiffuseLighting.disableGuiDepthLighting();
		//DiffuseLighting.disable();
		//RenderSystem.disableRescaleNormal();
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
			int health = MathHelper.ceil(player.getHealth());
			this.random.setSeed(this.getTicks() * 312871L);
			HungerManager var3 = player.getHungerManager();
			int foodLevel = var3.getFoodLevel();
			int width = this.scaledWidth / 2;
			int posX = width + 91;
			int height = this.scaledHeight - 74;
			int absorptionAmount = MathHelper.ceil(player.getAbsorptionAmount());
			this.client.getProfiler().push("armor");
			int armor = player.getArmor();
			String formattedArmorString = String.format("%02d%%", armor * 100 / 20);
			int widthOfArmorString = this.getTextRenderer().getWidth(formattedArmorString);
			int var13 = width - 128;
			int var14 = this.scaledHeight - 64;
			this.getTextRenderer().draw(matrices, formattedArmorString, (float)(var13 + 25 + (23 - widthOfArmorString)), (float)(var14 + 41), -1);
			this.client.getProfiler().swap("health");
			float var15 = player.getMaxHealth();
			String var16 = String.format("%02.0f%%", (float)(health + absorptionAmount) / var15 * 100.0F);
			int var17 = this.getTextRenderer().getWidth(var16);
			this.getTextRenderer().draw(matrices, var16, (float)(var13 + 25 + (23 - var17)), (float)(var14 + 8), absorptionAmount > 0 ? -256 : -65536);
			this.client.getTextureManager().bindTexture(HOT_BAR_TEXTURE);
			int posY = height - 10;
			this.client.getProfiler().swap("food");
			int var19 = 32 * foodLevel / 20;
			this.drawTexture(matrices, var13 + 161, var14 + 14, 0, 128, 32, var19);
			posY -= 10;
			this.client.getTextureManager().bindTexture(DrawableHelper.GUI_ICONS_TEXTURE);
			this.client.getProfiler().swap("air");
			int currentAir = player.getAir();
			int maximumAir = player.getMaxAir();
			if (player.isSubmergedIn(FluidTags.WATER) || currentAir < maximumAir) {
				int var23 = MathHelper.ceil((double)(currentAir - 2) * 10.0D / (double)maximumAir);
				int var24 = MathHelper.ceil((double)currentAir * 10.0D / (double)maximumAir) - var23;

				for(int var25 = 0; var25 < var23 + var24; ++var25) {
					if (var25 < var23) {
						this.drawTexture(matrices, posX - var25 * 8 - 9, posY + 15, 16, 18, 9, 9);
					} else {
						this.drawTexture(matrices, posX - var25 * 8 - 9, posY + 15, 25, 18, 9, 9);
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

	@ModifyArg(
		method = "renderExperienceBar",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"),
		index = 3
	)
	private float modifyYPositionOfExperienceLevel(float original) {
		return this.scaledHeight - 80.0F;
	}
}
