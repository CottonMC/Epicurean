package io.github.cottonmc.epicurean.item;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EpicureanItems {
	public static final Item JELLY = register("jelly", new JellyItem(1, 0.25f));
	public static final Item SUPER_JELLY = register("super_jelly", new JellyItem(2, 0.3f));

	public static final Item DEP_JELLY = Registry.register(Registry.ITEM, "edibles:jelly", new DeprecatedItem());
	public static final Item DEP_SUPER_JELLY = Registry.register(Registry.ITEM, "edibles:super_jelly", new DeprecatedItem());

	public static Item register(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(EpicureanGastronomy.MOD_ID, name), item);
		return item;
	}

	public static Item mixRegister(String name, Item item) {
		Registry.register(Registry.ITEM, "minecraft:" + name, item);
		return item;
	}

	public static void init() {

	}
}
