package io.github.PheonixVX.ThreeDShareware.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockRegistry {

	public static final Block BARREL_BLOCK = new BarrelBlock(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));

	public static void initialize() {
		Registry.register(Registry.BLOCK, new Identifier("threedshareware", "barrel"), BARREL_BLOCK);
	}
}
