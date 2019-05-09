package io.github.cottonmc.epicurean.meal;

import io.github.cottonmc.epicurean.container.CookingInventory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface MealBooster {
	List<MealBooster> BOOSTERS = new ArrayList<>();

	default List<StatusEffectInstance> addBoostEffects(List<StatusEffectInstance> effects, List<ItemStack> seasonings, CookingInventory inv) {
		return new ArrayList<>();
	}

	default int addBoostHunger(List<ItemStack> seasonings, CookingInventory inv) {
		return 0;
	}

	default float addBoostSaturation(List<ItemStack> seasonings, CookingInventory inv) {
		return 0f;
	}
}
