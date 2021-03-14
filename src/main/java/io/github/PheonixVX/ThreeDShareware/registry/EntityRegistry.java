package io.github.PheonixVX.ThreeDShareware.registry;

import io.github.PheonixVX.ThreeDShareware.registry.entity.CustomBarrelBlockEntityRenderer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntityType;

public class EntityRegistry {

	public static void initialize() {
		BlockEntityRendererRegistry.INSTANCE.register(BlockEntityType.BARREL, CustomBarrelBlockEntityRenderer::new);
	}
}
