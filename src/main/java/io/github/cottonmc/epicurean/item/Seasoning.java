package io.github.cottonmc.epicurean.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

public interface Seasoning {
	/**
	 * @param stack the stack of the item in question
	 * @return how much hunger restoration a seasoning in that stack should add to a meal
	 */
	int getHungerRestored(ItemStack stack);

	/**
	 * @param stack the stack of the item in question
	 * @return how much saturation a seasoning in that stack should add to a meal
	 */
	float getSaturationModifier(ItemStack stack);

	/**
	 * @param stack the stack of the item in question
	 * @return what status effect a seasoning in that stack should add to a meal
	 */
	default StatusEffectInstance getBonusEffect(ItemStack stack) {
		return null;
	}
}
