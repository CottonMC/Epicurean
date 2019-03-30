package io.github.cottonmc.epicurean.item;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import io.github.cottonmc.epicurean.block.crop.EpicureanCrops;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodItemSetting;
import net.minecraft.item.Item;
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
	public static final Item TOMATO = register("tomato", new SeasoningItem(2, 0.3f, StatusEffects.HASTE, DEFAULT_SETTINGS));
	public static final Item PEPPER = register("pepper", new SeasoningItem(2, 0.3f, StatusEffects.RESISTANCE, DEFAULT_SETTINGS));
	public static final Item ONION = register("onion", new PlantableItem(EpicureanCrops.ONION_PLANT, EpicureanItems.DEFAULT_SETTINGS.food(new FoodItemSetting.Builder().hunger(2).saturationModifier(0.2F).build())));
	public static final Item DASHI = register("dashi", new Item(DEFAULT_SETTINGS));
	public static final Item BUTTER = register("butter", new SeasoningItem(1, 0.1f, StatusEffects.SPEED, DEFAULT_SETTINGS));
	public static final Item SOYBEAN = register("soybean", new PlantableItem(EpicureanCrops.SOYBEAN_PLANT, EpicureanItems.DEFAULT_SETTINGS.food(new FoodItemSetting.Builder().hunger(2).saturationModifier(0.2f).build())));
	public static final Item SALT = register("salt", new SaltItem(DEFAULT_SETTINGS));

	//seeds
	public static final Item TOMATO_SEEDS = register("tomato_seeds", new PlantableItem(EpicureanCrops.TOMATO_PLANT, EpicureanItems.DEFAULT_SETTINGS));
	public static final Item PEPPER_SEEDS = register("pepper_seeds", new PlantableItem(EpicureanCrops.PEPPER_PLANT, EpicureanItems.DEFAULT_SETTINGS));

	//snacks
	public static final Item NOODLES = register("noodles", new MealItem(3, 0.1f));

	//meals
	public static final Item PAD_THAI = register("pad_thai", new MealItem(10, 0.5f));
	public static final Item SPAGHETTI = register("spaghetti", new MealItem(10, 0.5f));
	public static final Item RAMEN = register("ramen", new MealItem(10, 0.5f));
	public static final Item BEEF_STEW = register("beef_stew", new MealItem(10, 0.5f));
	public static final Item CHICKEN_TENDER = register("chicken_tender", new MealItem(5, 0.3f));
	public static final Item HAMBURGER = register("hamburger", new MealItem(10, 0.5f));
	public static final Item PANCAKE = register("pancake", new MealItem(5, 0.3f));

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
