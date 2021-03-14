package io.github.PheonixVX.ThreeDShareware.mixin.entity;

import io.github.PheonixVX.ThreeDShareware.registry.entity.CustomCreeperEntityFeatureRenderer;
import net.minecraft.client.render.entity.CreeperEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntityRenderer.class)
public abstract class CreeperEntityRendererMixin extends MobEntityRenderer<CreeperEntity, CreeperEntityModel<CreeperEntity>> implements FeatureRendererContext<CreeperEntity, CreeperEntityModel<CreeperEntity>> {
	private static final Identifier TEXTURE = new Identifier("textures/entity/creeper/creeper_special.png");

	// Dummy constructor
	public CreeperEntityRendererMixin (EntityRenderDispatcher entityRenderDispatcher, CreeperEntityModel<CreeperEntity> entityModel, float f) {
		super(entityRenderDispatcher, entityModel, f);
	}

	@Inject(at = @At("TAIL"), method = "<init>")
	public void addFeatureRenderer (EntityRenderDispatcher entityRenderDispatcher, CallbackInfo ci) {
		this.addFeature(new CustomCreeperEntityFeatureRenderer(this));
	}

	@Override
	public Identifier getTexture (CreeperEntity entity) {
		return TEXTURE;
	}
}