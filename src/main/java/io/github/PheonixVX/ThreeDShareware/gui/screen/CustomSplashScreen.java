package io.github.PheonixVX.ThreeDShareware.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.PheonixVX.ThreeDShareware.registry.GenericRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class CustomSplashScreen extends Screen {
	private static final Identifier MOJANG_LOGO = new Identifier("textures/gui/mojang_logo.png");
	private static final Identifier MOJANG_TEXT = new Identifier("textures/gui/mojang_text.png");
	private class_4295 field_19237;
	private long field_19238;
	private float field_19239;
	private long field_19240;
	private SoundInstance sound;

	public CustomSplashScreen (Text title) {
		super(new LiteralText("Amazing Logo"));
		this.field_19237 = class_4295.field_19243;
		this.field_19238 = -1L;
		this.field_19239 = 15.0F;
	}

	@Override
	public void render (MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.enableTexture();
		RenderSystem.enableBlend();
		RenderSystem.disableDepthTest();
		//GlStateManager.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
		RenderSystem.defaultBlendFunc();
		RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.clear(16640, MinecraftClient.IS_SYSTEM_MAC);
		RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
		long var4 = Util.getMeasuringTimeMs();
		long var6 = var4 - this.field_19240;
		this.field_19240 = var4;
		if (this.field_19237 == class_4295.field_19243) {
			this.field_19237 = class_4295.field_19244;
			this.field_19239 = 10.0F;
			this.field_19238 = -1L;
		} else {
			if (this.field_19237 == class_4295.field_19244) {
				this.field_19239 -= (float)var6 / 1000.0F;
				if (this.field_19239 <= 0.0F) {
					this.field_19237 = class_4295.field_19245;
					this.field_19238 = var4;
					this.sound = new PositionedSoundInstance(GenericRegistry.AWESOME_INTRO.getId(), SoundCategory.MASTER, 0.25F, 1.0F, false, 0, SoundInstance.AttenuationType.NONE, 0.0F, 0.0F, 0.0F, true) {
						public boolean method_20286() {
							return false;
						}
					};
					this.client.getSoundManager().play(this.sound);
				}
			} else if (!this.client.getSoundManager().isPlaying(this.sound)) {
				this.client.setScreen(new TitleScreen());
				this.field_19237 = class_4295.field_19243;
			}

			float var8 = 20.0F * this.field_19239;
			float var9 = 100.0F * MathHelper.sin(this.field_19239);
			Tessellator var10 = Tessellator.getInstance();
			BufferBuilder var11 = var10.getBuffer();
			var11.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
			if (this.field_19238 != -1L) {
				RenderSystem.setShaderTexture(0, MOJANG_TEXT);
				int var12 = MathHelper.clamp((int)(var4 - this.field_19238), 0, 255);
				this.method_20278(var11, this.width / 2, this.height - this.height / 8, 208, 38, var12);
			}

			var10.draw();
			matrices.push();
			matrices.translate((float)this.width / 2.0F, (float)this.height / 2.0F, this.getZOffset());
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(var8));
			// matrices.rotatef(var8, 0.0F, 0.0F, 1.0F);
			matrices.translate(var9, 0.0F, 0.0F);
			float var13 = 1.0F / (2.0F * this.field_19239 + 1.0F);
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(1.5F * this.field_19239));
			//matrices.rotatef(1.5F * this.field_19239, 0.0F, 0.0F, 1.0F);
			matrices.scale(var13, var13, 1.0F);
			RenderSystem.setShaderTexture(0, MOJANG_LOGO);
			var11.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
			this.method_20278(var11, 0, 0, 78, 76, 255);
			var10.draw();
			matrices.pop();
		}
	}

	private void method_20278(BufferBuilder var1, int var2, int var3, int var4, int var5, int var6) {
		int var7 = var4 / 2;
		int var8 = var5 / 2;
		var1.vertex(var2 - var7, var3 + var8, this.getZOffset()).texture(0.0F, 1.0F).color(255, 255, 255, var6).next();
		var1.vertex(var2 + var7, var3 + var8, this.getZOffset()).texture(1.0F, 1.0F).color(255, 255, 255, var6).next();
		var1.vertex(var2 + var7, var3 - var8, this.getZOffset()).texture(1.0F, 0.0F).color(255, 255, 255, var6).next();
		var1.vertex(var2 - var7, var3 - var8, this.getZOffset()).texture(0.0F, 0.0F).color(255, 255, 255, var6).next();
	}

	@Override
	public void onClose () {
		this.client.setScreen(new TitleScreen());
	}

	enum class_4295 {
		field_19243,
		field_19244,
		field_19245
	}
}
