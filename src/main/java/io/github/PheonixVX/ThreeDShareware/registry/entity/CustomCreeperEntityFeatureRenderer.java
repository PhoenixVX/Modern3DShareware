package io.github.PheonixVX.ThreeDShareware.registry.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.Identifier;

public class CustomCreeperEntityFeatureRenderer extends FeatureRenderer<CreeperEntity, CreeperEntityModel<CreeperEntity>> {
	private static final Identifier TEXTURE = new Identifier("textures/entity/creeper/creeper_special.png");
	private final CreeperEntityModel<CreeperEntity> model = new CreeperEntityModel<>(2.0F);

	public CustomCreeperEntityFeatureRenderer (FeatureRendererContext<CreeperEntity, CreeperEntityModel<CreeperEntity>> context) {
		super(context);
	}

	@Override
	public void render (MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CreeperEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		renderModel(this.model, TEXTURE, matrices, vertexConsumers, light, entity, 0, 0, 0);
	}
}
