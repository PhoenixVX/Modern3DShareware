package io.github.PheonixVX.ThreeDShareware.registry.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

import java.util.function.Function;

public class CustomBarrelBlockEntityRenderer extends BlockEntityRenderer<BarrelBlockEntity> {

	public CustomBarrelBlockEntityRenderer (BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render (BarrelBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		GlStateManager.pushMatrix();
		GlStateManager.translatef( 0.5F, 0,0.5F);
		this.dispatcher.textureManager.bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
		MinecraftClient client = MinecraftClient.getInstance();
		Entity cameraEntity = client.getCameraEntity();
		Vec2f ZERO = Vec2f.ZERO;
		if (cameraEntity != null) {
			ZERO = cameraEntity.getRotationClient();
		}

		GlStateManager.rotatef(-ZERO.y, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
		Sprite barrelSprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/barrel_side"));
		Sprite fireSprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/fire_0"));
		//Sprite barrelSprite = (Sprite) client.getSpriteAtlas(new Identifier("block/barrel_side"));
		//Sprite fireSprite = (Sprite) client.getSpriteAtlas(new Identifier("block/fire_0"));
		Tessellator tesselator = Tessellator.getInstance();
		BufferBuilder vertexBufferBuilder = tesselator.getBuffer();
		vertexBufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
		vertexBufferBuilder.vertex(-0.5D, 0.0D, 0.0D).texture(barrelSprite.getMinU(), barrelSprite.getMaxV()).color(255, 255, 255, 255).next();
		vertexBufferBuilder.vertex(0.5D, 0.0D, 0.0D).texture(barrelSprite.getMaxU(), barrelSprite.getMaxV()).color(255, 255, 255, 255).next();
		vertexBufferBuilder.vertex(0.5D, -1.0D, 0.0D).texture(barrelSprite.getMaxU(), barrelSprite.getMinV()).color(255, 255, 255, 255).next();
		vertexBufferBuilder.vertex(-0.5D, -1.0D, 0.0D).texture(barrelSprite.getMinU(), barrelSprite.getMinV()).color(255, 255, 255, 255).next();
		vertexBufferBuilder.sortQuads(0.0F, -1.0F, 0.0F);
		vertexBufferBuilder.vertex(-0.5D, 0.0D, 0.0D).texture(fireSprite.getMinU(), fireSprite.getMaxV()).color(255, 255, 255, 255).next();
		vertexBufferBuilder.vertex(0.5D, 0.0D, 0.0D).texture(fireSprite.getMaxU(), fireSprite.getMaxV()).color(255, 255, 255, 255).next();
		vertexBufferBuilder.vertex(0.5D, -1.0D, 0.0D).texture(fireSprite.getMaxU(), fireSprite.getMinV()).color(255, 255, 255, 255).next();
		vertexBufferBuilder.vertex(-0.5D, -1.0D, 0.0D).texture(fireSprite.getMinU(), fireSprite.getMinV()).color(255, 255, 255, 255).next();
		vertexBufferBuilder.sortQuads(0.0F, 0.0F, 0.0F);
		tesselator.draw();
		GlStateManager.popMatrix();
	}
}
