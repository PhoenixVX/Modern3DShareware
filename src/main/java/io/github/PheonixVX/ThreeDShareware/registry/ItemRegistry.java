package io.github.PheonixVX.ThreeDShareware.registry;

import io.github.PheonixVX.ThreeDShareware.registry.item.ThreeDItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

	public static final Item RED_KEY = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item BLUE_KEY = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item YELLOW_KEY = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item THREE_D_ITEM = new ThreeDItem(new FabricItemSettings().group(ItemGroup.MISC));

	public static void initialize() {
		Registry.register(Registry.ITEM, new Identifier("red_key"), RED_KEY);
		Registry.register(Registry.ITEM, new Identifier("blue_key"), BLUE_KEY);
		Registry.register(Registry.ITEM, new Identifier("yellow_key"), YELLOW_KEY);
		Registry.register(Registry.ITEM, new Identifier("3d"), THREE_D_ITEM);
	}
}
