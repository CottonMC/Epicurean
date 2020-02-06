package io.github.cottonmc.epicurean.meal;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class IngredientProfiles implements SimpleResourceReloadListener {
	public static Map<Item, FlavorGroup> MEAL_INGREDIENTS = new HashMap<>();
	public static Map<Item, FlavorGroup> DRESSINGS = new HashMap<>();
	public static Map<StatusEffect, Integer> EFFECT_TIMES = new HashMap<>();

	@Override
	public CompletableFuture load(ResourceManager manager, Profiler profiler, Executor executor) {
		return CompletableFuture.supplyAsync(() -> {
			Jankson jankson = Jankson.builder().build();
			String path = "config/epicurean";
			Collection<Identifier> resources = manager.findResources(path, (name) -> name.equals("effect_times.json"));
			if (resources.size() == 0) EpicureanGastronomy.LOGGER.error("Couldn't find any effect time entries!");
			for (Identifier fileId : resources) {
				try {
					for (Resource res : manager.getAllResources(fileId)) {
						JsonObject json = jankson.load(IOUtils.toString(res.getInputStream()));
						Set<String> keys = json.keySet();
						boolean replace = false;
						if (keys.contains("replace")) {
							replace = json.get(Boolean.class, "replace");
						}
						keys.remove("replace");
						for (String key : keys) {
							StatusEffect effect = Registry.STATUS_EFFECT.get(new Identifier(key));
							if (EFFECT_TIMES.containsKey(effect)) {
								if (replace) {
									EFFECT_TIMES.remove(effect);
									EFFECT_TIMES.put(effect, json.get(Integer.class, key));
								}
							} else EFFECT_TIMES.put(effect, json.get(Integer.class, key));
						}
					}
				} catch (IOException | SyntaxError | NullPointerException e) {
					EpicureanGastronomy.LOGGER.error("Couldn't load effect times: %s", e);
				}
			}
			return EFFECT_TIMES;
		}, executor);
	}

	@Override
	public CompletableFuture<Void> apply(Object data, ResourceManager manager, Profiler profiler, Executor executor) {
		return CompletableFuture.runAsync(() -> {
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
			// Second-highest impact: add umami foods
			if (UMAMI != null) putTag(UMAMI, FlavorGroup.UMAMI);
			// Third-highest impact: add acidic foods
			if (ACIDIC != null) putTag(ACIDIC, FlavorGroup.ACIDIC);
			// Fourth-highest impact: add sweet foods
			if (SWEET != null) putTag(SWEET, FlavorGroup.SWEET);
			// Fifth-highest impact: add bitter foods
			if (BITTER != null) putTag(BITTER, FlavorGroup.BITTER);
			// Lowest impact: add filling foods
			if (FILLING != null) putTag(FILLING, FlavorGroup.FILLING);
			EpicureanGastronomy.LOGGER.info("Ingredient profiles set!");
		});
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
