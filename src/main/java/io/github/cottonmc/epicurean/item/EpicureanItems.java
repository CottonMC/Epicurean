package io.github.cottonmc.epicurean.item;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import io.github.cottonmc.epicurean.block.crop.EpicureanCrops;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodItemSetting;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EpicureanItems {
	public static Item.Settings defaultSettings() {
		return new Item.Settings().itemGroup(EpicureanGastronomy.EPICUREAN_GROUP);
	}

	public static Item.Settings foodSettings(int hunger, float saturation) {
		return defaultSettings().food(new FoodItemSetting.Builder().hunger(hunger).saturationModifier(saturation).build());
	}

	//non-meal-related stuff
	public static final Item JELLY = register("jelly", new JellyItem(2, 0.25f));
	public static final Item SUPER_JELLY = register("super_jelly", new JellyItem(4, 0.3f));
	public static final Item SMOKED_SALMON = register("smoked_salmon", new Item(foodSettings(8, .6f)));

	//ingredients
	public static final Item TOMATO = register("tomato", new SeasoningItem(2, 0.3f, StatusEffects.HASTE, foodSettings(2, 0.2f)));
	public static final Item PEPPER = register("pepper", new SeasoningItem(2, 0.3f, StatusEffects.RESISTANCE, foodSettings(2, 0.2f)));
	public static Item ONION;
	public static final Item DASHI = register("dashi", new Item(defaultSettings()));
	public static final Item BUTTER = register("butter", new SeasoningItem(1, 0.1f, StatusEffects.SPEED, defaultSettings()));
	public static Item SOYBEAN;
	public static final Item SOY_SAUCE = register("soy_sauce", new SeasoningItem(1, 0.1f, StatusEffects.STRENGTH, defaultSettings()));
	public static final Item SALT = register("salt", new SaltItem(defaultSettings()));

	//seeds
	public static Item TOMATO_SEEDS;
	public static Item PEPPER_SEEDS;

	//snacks
	public static final Item NOODLES = register("noodles", new MealItem(3, 0.2f));
	public static final Item TOFU = register("tofu", new MealItem(3, 0.2f));

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

	public static void init() {
		ONION = register("onion", new PlantableItem(EpicureanCrops.ONION_PLANT, foodSettings(2, 0.2f)));
		TOMATO_SEEDS = register("tomato_seeds", new PlantableItem(EpicureanCrops.TOMATO_PLANT, defaultSettings()));
		PEPPER_SEEDS = register("pepper_seeds", new PlantableItem(EpicureanCrops.PEPPER_PLANT, defaultSettings()));
		SOYBEAN = register("soybean", new PlantableItem(EpicureanCrops.SOYBEAN_PLANT, foodSettings(2, 0.2f)));
		CompostingChanceRegistry.INSTANCE.add(JELLY, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(SUPER_JELLY, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(PEPPER_SEEDS, 0.3f);
		CompostingChanceRegistry.INSTANCE.add(TOMATO_SEEDS, 0.3f);
		CompostingChanceRegistry.INSTANCE.add(SOYBEAN, 0.3f);
		CompostingChanceRegistry.INSTANCE.add(SOY_SAUCE, 1.0f);
		CompostingChanceRegistry.INSTANCE.add(PEPPER, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(TOMATO, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(ONION, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(DASHI, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(BUTTER, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(NOODLES, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(TOFU, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(PANCAKE, 0.85f);
		CompostingChanceRegistry.INSTANCE.add(CHICKEN_TENDER, 0.85f);
		CompostingChanceRegistry.INSTANCE.add(PAD_THAI, 1.0f);
		CompostingChanceRegistry.INSTANCE.add(SPAGHETTI, 1.0f);
		CompostingChanceRegistry.INSTANCE.add(RAMEN, 1.0f);
		CompostingChanceRegistry.INSTANCE.add(BEEF_STEW, 1.0f);
		CompostingChanceRegistry.INSTANCE.add(HAMBURGER, 1.0f);
		CompostingChanceRegistry.INSTANCE.add(SMOKED_SALMON, 0.3f);
	}
}
