package io.github.cottonmc.epicurean.meal;

import io.github.cottonmc.epicurean.container.CookingInventory;
import io.github.cottonmc.epicurean.recipe.MealRecipe;
//import io.github.cottonmc.skillcheck.SkillCheck;
//import io.github.cottonmc.skillcheck.api.traits.ClassManager;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SkillCheckMealBooster implements MealBooster {
	@Override
	public List<StatusEffectInstance> addBoostEffects(List<StatusEffectInstance> effects, List<ItemStack> seasonings, CookingInventory inv) {
		List<StatusEffectInstance> ret = new ArrayList<>(effects);
		int boostBy = 0;
//		int boostBy = ClassManager.getLevel(inv.accessor, SkillCheck.ARTISAN) * 100;
		for (StatusEffectInstance effect : effects) {
			ret = MealRecipe.addEffect(ret, new StatusEffectInstance(effect.getEffectType(), boostBy));
		}
		return ret;
	}
}
