package io.github.PheonixVX.ThreeDShareware.mixin.entity;

import io.github.PheonixVX.ThreeDShareware.registry.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ElderGuardianEntity.class)
public abstract class ElderGuardianEntityMixin extends GuardianEntity {

	// Dummy constructor
	public ElderGuardianEntityMixin (EntityType<? extends GuardianEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void onDeath (DamageSource source) {
		this.dropItem(ItemRegistry.BLUE_KEY);
		super.onDeath(source);
	}
}
