package io.github.cottonmc.epicurean.item;

import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SeasoningItem extends Item implements Seasoning {
	private int hungerRestored;
	private float saturationModifier;
	private StatusEffect effect;

	public SeasoningItem(int hungerRestored, float saturationModifier, Settings settings) {
		super(settings);
		this.hungerRestored = hungerRestored;
		this.saturationModifier = saturationModifier;
	}

	public SeasoningItem(int hungerRestored, float saturationModifier, StatusEffect effect, Settings settings) {
		this(hungerRestored, saturationModifier, settings);
		this.effect = effect;
	}

	@Override
	public boolean hasRecipeRemainder() {
		return false;
	}

	public int getHungerRestored(ItemStack stack) {
		return this.hungerRestored;
	}

	public float getSaturationModifier(ItemStack stack) {
		return this.saturationModifier;
	}

	@Override
	public StatusEffectInstance getBonusEffect(ItemStack stack) {
		if (effect != null) {
			return new StatusEffectInstance(effect, IngredientProfiles.EFFECT_TIMES.getOrDefault(effect, 1800));
		}
		return null;
	}
}
