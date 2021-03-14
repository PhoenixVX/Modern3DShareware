package io.github.PheonixVX.ThreeDShareware.registry.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
		Formatting[] var5 = Formatting.values();
		Formatting var6 = var5[field_19177.nextInt(var5.length)];
		tooltip = (List<Text>) new LiteralText("Tasty!").formatted(var6);
	}
}
