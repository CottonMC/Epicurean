package io.github.cottonmc.epicurean.item;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import io.github.cottonmc.epicurean.block.EpicureanCrops;
import net.minecraft.item.FoodCropItem;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.SeedsItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EpicureanItems {
	public static final Item.Settings DEFAULT_SETTINGS = new Item.Settings().itemGroup(EpicureanGastronomy.EPICUREAN_GROUP);

	//jelly and super-jelly, with deprecated forms
	public static final Item JELLY = register("jelly", new JellyItem(2, 0.25f));
	public static final Item SUPER_JELLY = register("super_jelly", new JellyItem(4, 0.3f));
	public static final Item DEP_JELLY = Registry.register(Registry.ITEM, "edibles:jelly", new DeprecatedItem());
	public static final Item DEP_SUPER_JELLY = Registry.register(Registry.ITEM, "edibles:super_jelly", new DeprecatedItem());

	//ingredients
	public static final Item TOMATO = register("tomato", new FoodItem(2, 0.2f, false, DEFAULT_SETTINGS));
	public static final Item PEPPER = register("pepper", new FoodItem(2, 0.2f, false, DEFAULT_SETTINGS));
	//TODO: figure out what category onions are most needed in
	public static final Item ONION = register("onion", new FoodCropItem(2, 0.2f, EpicureanCrops.ONION_PLANT, SoundEvents.ITEM_CROP_PLANT, DEFAULT_SETTINGS));
	public static final Item DASHI = register("dashi", new Item(DEFAULT_SETTINGS));
	public static final Item BUTTER = register("butter", new SeasoningItem(1, 0.25f, DEFAULT_SETTINGS));

	//seeds
	public static final Item TOMATO_SEEDS = register("tomato_seeds", new SeedsItem(EpicureanCrops.TOMATO_PLANT, SoundEvents.ITEM_CROP_PLANT, DEFAULT_SETTINGS));
	public static final Item PEPPER_SEEDS = register("pepper_seeds", new SeedsItem(EpicureanCrops.PEPPER_PLANT, SoundEvents.ITEM_CROP_PLANT, DEFAULT_SETTINGS));

	//snacks
	public static final Item NOODLES = register("noodles", new MealItem(1, 0.1f, false));

	//meals
	public static final Item PAD_THAI = register("pad_thai", new MealItem(10, 0.5f, false));
	public static final Item SPAGHETTI = register("spaghetti", new MealItem(10, 0.5f, false));
	public static final Item RAMEN = register("ramen", new MealItem(10, 0.5f, false));

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
