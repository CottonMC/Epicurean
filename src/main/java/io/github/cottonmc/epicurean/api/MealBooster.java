package io.github.cottonmc.epicurean.api;

import io.github.cottonmc.epicurean.container.CookingInventory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A hook for other mods to add bonuses to meals.
 */
public interface MealBooster {
	List<MealBooster> BOOSTERS = new ArrayList<>();

	/**
	 * Add extra status effects to a meal.
	 * @param effects The effects on this meal.
	 * @param seasonings The seasonings being added.
	 * @param inv The inventory crafting this meal.
	 * @return All the effects this meal should have, including the ones previously passed in.
	 */
	default List<StatusEffectInstance> addBoostEffects(List<StatusEffectInstance> effects, List<ItemStack> seasonings, CookingInventory inv) {
		return new ArrayList<>();
	}

	/**
	 * Add extra hunger restoration to a meal.
	 * @param seasonings The seasonings being added.
	 * @param inv The inventory crafting this meal.
	 * @return How much extra hunger this meal should restore.
	 */
	default int addBoostHunger(List<ItemStack> seasonings, CookingInventory inv) {
		return 0;
	}

	/**
	 * Add extra saturation restoration to a meal.
	 * @param seasonings The seasonings being added.
	 * @param inv The inventory crafting this emal.
	 * @return How much extra saturation this meal should restore.
	 */
	default float addBoostSaturation(List<ItemStack> seasonings, CookingInventory inv) {
		return 0f;
	}
}
