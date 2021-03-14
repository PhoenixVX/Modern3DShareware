package io.github.PheonixVX.ThreeDShareware.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.GameProfile;
import io.github.PheonixVX.ThreeDShareware.misc.CheatCodes;
import io.github.PheonixVX.ThreeDShareware.registry.ItemRegistry;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Keyboard.class)
public class KeyboardMixin {

	private final Random random = new Random();
	@Shadow
	@Final
	private MinecraftClient client;
	private String CURRENT_STRING = "";

	@Inject(at = @At("HEAD"), method = "onChar")
	public void onChar (long window, int i, int j, CallbackInfo ci) {
		if (window == this.client.getWindow().getHandle() && this.client.player != null) {
			if (!InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 67) || !InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 292)) {
				this.CURRENT_STRING += Character.toUpperCase((char) i);
			}
			if (this.CURRENT_STRING.length() > CheatCodes.MAX_CHEAT_LEN) {
				this.CURRENT_STRING = this.CURRENT_STRING.substring(this.CURRENT_STRING.length() - CheatCodes.MAX_CHEAT_LEN);
			}

			MinecraftServer var5 = this.client.getServer();
			GameProfile var6 = this.client.player != null ? this.client.player.getGameProfile() : null;
			if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT6)) {
				this.client.player.sendChatMessage("There is no cow level");
			} else if (var5 != null && var6 != null) {
				ServerPlayerEntity var7 = var5.getPlayerManager().getPlayer(var6.getId());
				if (var7 != null) {
					if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT5)) {
						var7.inventory.insertStack(new ItemStack(ItemRegistry.RED_KEY));
						var7.inventory.insertStack(new ItemStack(ItemRegistry.YELLOW_KEY));
						var7.inventory.insertStack(new ItemStack(ItemRegistry.BLUE_KEY));
						this.client.player.sendChatMessage("Got all keys!");
					} else if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT4)) {
						ItemStack var2 = new ItemStack(Items.CROSSBOW);
						EnchantmentHelper.set(ImmutableMap.of(Enchantments.MULTISHOT, 12), var2);
						var7.inventory.insertStack(var2);
						var7.inventory.insertStack(EnchantmentHelper.enchant(this.random, new ItemStack(Items.DIAMOND_SWORD), 30, true));
						var7.inventory.insertStack(EnchantmentHelper.enchant(this.random, new ItemStack(Items.DIAMOND_AXE), 30, true));
						var7.inventory.insertStack(EnchantmentHelper.enchant(this.random, new ItemStack(Items.DIAMOND_PICKAXE), 30, true));
						var7.inventory.insertStack(EnchantmentHelper.enchant(this.random, new ItemStack(Items.DIAMOND_HOE), 30, true));
						var7.inventory.insertStack(EnchantmentHelper.enchant(this.random, new ItemStack(Items.SHEARS), 30, true));
						var7.inventory.insertStack(EnchantmentHelper.enchant(this.random, new ItemStack(Items.BOW), 30, true));
						var7.equipStack(EquipmentSlot.HEAD, EnchantmentHelper.enchant(this.random, new ItemStack(Items.DIAMOND_HELMET), 30, true));
						var7.equipStack(EquipmentSlot.CHEST, EnchantmentHelper.enchant(this.random, new ItemStack(Items.DIAMOND_CHESTPLATE), 30, true));
						var7.equipStack(EquipmentSlot.LEGS, EnchantmentHelper.enchant(this.random, new ItemStack(Items.DIAMOND_LEGGINGS), 30, true));
						var7.equipStack(EquipmentSlot.FEET, EnchantmentHelper.enchant(this.random, new ItemStack(Items.DIAMOND_BOOTS), 30, true));
						var7.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
						this.client.player.sendChatMessage("Got all equipment!");
					} else if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT2)) {
						var7.abilities.allowFlying = !var7.abilities.allowFlying;
						var7.sendAbilitiesUpdate();
						this.client.player.sendChatMessage("FLYING=VERY YES");
					} else if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT1)) {
						var7.abilities.invulnerable = !var7.abilities.invulnerable;
						var7.sendAbilitiesUpdate();
						this.client.player.sendChatMessage("Nothing can stop you!");
					} else if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT3)) {
						var7.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 1200, 20));
						var7.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200, 20));
						var7.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1200, 20));
						this.client.player.sendChatMessage("Gordon's ALIVE!");
					} else if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT7)) {
						Vec3d var2 = var7.getPos();
						Vec3d var3 = new Vec3d(var2.x + (double) (this.random.nextFloat() * 3.0F), var2.y, var2.z + (double) (this.random.nextFloat() * 3.0F));
						HorseEntity var4 = EntityType.HORSE.create(var7.world);
						var4.updatePosition(var3.x, var3.y, var3.z);
						var4.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(200.0D);
						var4.getAttributeInstance(EntityAttributes.HORSE_JUMP_STRENGTH).setBaseValue(3.0D);
						var4.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(3.0D);
						var4.setTame(true);
						var4.setTemper(0);
						var4.equip(401, new ItemStack(Items.DIAMOND_HORSE_ARMOR));
						var4.equip(400, new ItemStack(Items.SADDLE));
						var7.world.spawnEntity(var4);
						this.client.player.sendChatMessage("VROOM!");
					} else if (this.CURRENT_STRING.endsWith(CheatCodes.CHEAT8)) {
						BlockPos var2 = new BlockPos((Position) var7);

						for (int var3 = 0; var3 < 5; ++var3) {
							CreeperEntity creeper = EntityType.CREEPER.create(var7.world);
							creeper.updatePosition(var7.getX() + 0.5D, var7.getY() + 0.5D, var7.getZ() + 0.5D);
							//var5.wouldPoseNotCollide();
							var7.world.spawnEntity(creeper);
							this.client.player.sendChatMessage("Special creeper has been spawned nearby!");
							break;
						}
					}
				}
			}
		}
	}
}