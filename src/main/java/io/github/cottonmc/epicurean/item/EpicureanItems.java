package io.github.cottonmc.epicurean.item;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.tags.TagEntryManager;
import io.github.cottonmc.cotton.tags.TagType;
import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EpicureanItems {
	public static final Item JELLY = register("jelly", new JellyItem(1, 0.25f));
	public static final Item SUPER_JELLY = register("super_jelly", new JellyItem(2, 0.3f));

	public static final Item DEP_JELLY = Registry.register(Registry.ITEM, "edibles:jelly", new DeprecatedItem());
	public static final Item DEP_SUPER_JELLY = Registry.register(Registry.ITEM, "edibles:super_jelly", new DeprecatedItem());

	public static final Item NOODLES = register("noodles", new MealItem(1, 0.1f, false));

	public static Item register(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(EpicureanGastronomy.MOD_ID, name), item);
		return item;
	}

	public static Item mixRegister(String name, Item item) {
		Registry.register(Registry.ITEM, "minecraft:" + name, item);
		return item;
	}

	public static void init() {
		// add cooked meats to a tag
		TagEntryManager.registerToTag(TagType.ITEM, new Identifier(Cotton.SHARED_NAMESPACE, "cooked_meat"),
				"minecraft:cooked_beef", "minecraft:cooked_chicken", "minecraft:cooked_cod", "minecraft:cooked_mutton",
				"minecraft:cooked_porkchop", "minecraft:cooked_rabbit", "minecraft:cooked_salmon");
	}
}
