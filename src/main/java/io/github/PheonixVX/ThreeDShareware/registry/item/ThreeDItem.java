package io.github.PheonixVX.ThreeDShareware.registry.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class ThreeDItem extends Item {
	private static final Random field_19177 = new Random();

	public ThreeDItem (Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip (ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		Formatting[] formattings = Formatting.values();
		Formatting randomFormat = formattings[field_19177.nextInt(formattings.length)];
		tooltip.add(new LiteralText("Tasty!").formatted(randomFormat));
	}

	@Override
	public ItemStack finishUsing (ItemStack stack, World world, LivingEntity user) {
		ItemStack resultStack = super.finishUsing(stack, world, user);
		if (user instanceof ClientPlayerEntity) {
			ClientPlayerEntity player = (ClientPlayerEntity) user;

		}

		return resultStack;
	}
}
