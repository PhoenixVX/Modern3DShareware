package io.github.PheonixVX.ThreeDShareware.mixin.entity;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BarrelBlockEntity.class)
public abstract class BarrelBlockEntityMixin extends LootableContainerBlockEntity {
	@Shadow private int viewerCount;

	@Shadow protected abstract void setOpen (BlockState state, boolean open);

	private int ticksOpen = 0;

	// Dummy constructor
	protected BarrelBlockEntityMixin (BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
	}

	/**
	 * @author PheonixVX
	 */
	@Overwrite
	public void tick() {
		if (!this.world.isClient) {
			int var1 = this.pos.getX();
			int var2 = this.pos.getY();
			int var3 = this.pos.getZ();
			++this.ticksOpen;
			this.viewerCount = ChestBlockEntity.countViewers(this.world, this, this.ticksOpen, var1, var2);
			BlockState var4 = this.getCachedState();
			boolean var5 = var4.get(BarrelBlock.OPEN);
			boolean var6 = this.viewerCount > 0;
			if (var6 != var5) {
				if (var5) {
					this.world.playSound(var1, var2, var3, SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
				} else {
					this.world.playSound(var1, var2, var3, SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
				}
				if (!var6) {
					if (this.isEmpty()) {
						this.world.createExplosion(null, var1, var2, var3, 2.0F, Explosion.DestructionType.NONE);
						this.world.setBlockState(this.getPos(), Blocks.AIR.getDefaultState(), 3);
					}
				} else {
					this.setOpen(var4, true);
				}
			}
		}
	}
}
