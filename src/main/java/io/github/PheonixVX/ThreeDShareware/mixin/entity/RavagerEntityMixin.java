package io.github.PheonixVX.ThreeDShareware.mixin.entity;

import io.github.PheonixVX.ThreeDShareware.registry.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RavagerEntity.class)
public abstract class RavagerEntityMixin extends RaiderEntity {

	// Dummy constructor
	protected RavagerEntityMixin (EntityType<? extends RaiderEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void onDeath (DamageSource source) {
		this.dropItem(ItemRegistry.YELLOW_KEY);
		super.onDeath(source);
	}
}
