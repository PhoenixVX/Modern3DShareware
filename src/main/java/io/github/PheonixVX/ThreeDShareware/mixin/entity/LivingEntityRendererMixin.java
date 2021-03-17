package io.github.PheonixVX.ThreeDShareware.mixin.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {

	protected LivingEntityRendererMixin (EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;setupTransforms(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V"), method = "render", index = 3)
	private float modifyTransforms (float original) {
		return 0.0F;
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V"), method = "render")
	private void inject2DEntityRenderer (T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		this.method_20283(f, g, livingEntity, (float) i);
	}

	protected void method_20283 (double d, double d2, LivingEntity t, float f) {
		double d3 = Math.atan2(d2, d) / Math.PI * 180.0;
		double u2603 = f - d3;
		u2603 = Math.floor(u2603 / 45.0) * 45.0;
		RenderSystem.rotatef((float) d3, 0.0f, 1.0f, 0.0f);
		RenderSystem.scalef(0.02f, 1.0f, 1.0f);
		RenderSystem.rotatef((float) u2603, 0.0F, 1.0F, 0.0F);
	}
}
