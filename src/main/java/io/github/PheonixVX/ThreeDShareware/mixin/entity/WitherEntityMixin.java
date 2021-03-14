package io.github.PheonixVX.ThreeDShareware.mixin.entity;

import io.github.PheonixVX.ThreeDShareware.registry.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WitherEntity.class)
public abstract class WitherEntityMixin extends HostileEntity {

	// Dummy constructor
	protected WitherEntityMixin (EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void onDeath (DamageSource source) {
		this.dropItem(ItemRegistry.RED_KEY);
		super.onDeath(source);
	}
}
