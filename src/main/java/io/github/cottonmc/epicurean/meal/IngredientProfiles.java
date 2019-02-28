package io.github.cottonmc.epicurean.meal;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.epicurean.EpicureanGastronomy;
import io.github.cottonmc.epicurean.item.EpicureanItems;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.Map;
public class IngredientProfiles implements SimpleSynchronousResourceReloadListener {
	public static Map<Item, FlavorGroup> MEAL_INGREDIENTS;
	public static Map<Item, FlavorGroup> DRESSINGS;

	@Override
	public void apply(ResourceManager manager) {
		MEAL_INGREDIENTS.clear();
		DRESSINGS.clear();
		// Highest impact: add spicy foods
//		MEAL_INGREDIENTS.put(EpicureanItems.PEPPER, FlavorGroup.SPICY);
		// Second-highest impact: add umami foods
		Tag<Item> COOKED_MEATS = ItemTags.getContainer().get(new Identifier(Cotton.SHARED_NAMESPACE, "cooked_meat"));
		for (Item meat : COOKED_MEATS.values()) {
			MEAL_INGREDIENTS.put(meat, FlavorGroup.UMAMI);
		}
		MEAL_INGREDIENTS.put(Items.field_17516, FlavorGroup.UMAMI); //brown mushroom
		MEAL_INGREDIENTS.put(Items.field_17517, FlavorGroup.UMAMI); //red mushroom
		// Third-highest impact: add acidic foods
//		MEAL_INGREDIENTS.put(EpicureanItems.TOMATO, FlavorGroup.ACIDIC);
		// Fourth-highest impact: add sweet foods
		MEAL_INGREDIENTS.put(Items.SUGAR, FlavorGroup.SWEET);
		DRESSINGS.put(EpicureanItems.JELLY, FlavorGroup.SWEET);
		DRESSINGS.put(EpicureanItems.SUPER_JELLY, FlavorGroup.SWEET);
		// Fifth-highest impact: add bitter foods
		MEAL_INGREDIENTS.put(Items.COCOA_BEANS, FlavorGroup.BITTER);
		// Lowest impact: add filling foods
		MEAL_INGREDIENTS.put(Items.WHEAT, FlavorGroup.FILLING);
		MEAL_INGREDIENTS.put(Items.POTATO, FlavorGroup.FILLING);
		MEAL_INGREDIENTS.put(Items.CARROT, FlavorGroup.FILLING);
//		MEAL_INGREDIENTS.put(EpicureanItems.DASHI, FlavorGroup.FILLING);
		EpicureanGastronomy.LOGGER.info("Ingredient profiles set!");
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(EpicureanGastronomy.MOD_ID, "flavor_profiles");
	}
}
