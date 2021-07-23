package io.github.PheonixVX.ThreeDShareware.mixin.block;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BarrelBlock.class)
public abstract class BarrelBlockMixin extends BlockWithEntity {
	// Dummy constructor
	protected BarrelBlockMixin (Settings settings) {
		super(settings);
	}

	@Override
	public void onEntityCollision (BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient && entity instanceof ProjectileEntity projectileEntity) {
			Entity ownerOfProjectile = projectileEntity.getOwner();
			world.createExplosion(ownerOfProjectile, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 5.0f, Explosion.DestructionType.DESTROY);
		}
	}
}
