package io.github.cottonmc.epicurean.meal;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
public class IngredientProfiles implements SimpleSynchronousResourceReloadListener {
	public static Map<Item, FlavorGroup> MEAL_INGREDIENTS = new HashMap<>();
	public static Map<Item, FlavorGroup> DRESSINGS = new HashMap<>();

	@Override
	public void apply(ResourceManager manager) {
		MEAL_INGREDIENTS.clear();
		DRESSINGS.clear();
		Tag<Item> SPICY = ItemTags.getContainer().get(new Identifier(EpicureanGastronomy.MOD_ID, "spicy"));
		Tag<Item> UMAMI = ItemTags.getContainer().get(new Identifier(EpicureanGastronomy.MOD_ID, "umami"));
		Tag<Item> ACIDIC = ItemTags.getContainer().get(new Identifier(EpicureanGastronomy.MOD_ID, "acidic"));
		Tag<Item> SWEET = ItemTags.getContainer().get(new Identifier(EpicureanGastronomy.MOD_ID, "sweet"));
		Tag<Item> BITTER = ItemTags.getContainer().get(new Identifier(EpicureanGastronomy.MOD_ID, "bitter"));
		Tag<Item> FILLING = ItemTags.getContainer().get(new Identifier(EpicureanGastronomy.MOD_ID, "filling"));

		// Highest impact: add spicy foods
		if (SPICY != null) putTag(SPICY, FlavorGroup.SPICY);
//		to add: pepper
		// Second-highest impact: add umami foods
		if (UMAMI != null) putTag(UMAMI, FlavorGroup.UMAMI);
		// Third-highest impact: add acidic foods
		if (ACIDIC != null) putTag(ACIDIC, FlavorGroup.ACIDIC);
//		to add: tomato
		// Fourth-highest impact: add sweet foods
		if (SWEET != null) putTag(SWEET, FlavorGroup.SWEET);
		// Fifth-highest impact: add bitter foods
		if (BITTER != null) putTag(BITTER, FlavorGroup.BITTER);
		// Lowest impact: add filling foods
		if (FILLING != null) putTag(FILLING, FlavorGroup.FILLING);
//		to add: dashi
		EpicureanGastronomy.LOGGER.info("Ingredient profiles set!");
	}

	private void putTag(Tag<Item> tag, FlavorGroup flavor) {
		Tag<Item> DRESSING_TAG = ItemTags.getContainer().get(new Identifier(EpicureanGastronomy.MOD_ID, "dressings"));
		for (Item ingredient : tag.values()) {
			if (DRESSING_TAG != null && DRESSING_TAG.contains(ingredient)) {
				DRESSINGS.put(ingredient, flavor);
			} else MEAL_INGREDIENTS.put(ingredient, flavor);
		}
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(EpicureanGastronomy.MOD_ID, "flavor_profiles");
	}
}
