package io.github.cottonmc.epicurean.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SeasoningItem extends Item {
	private int hungerRestored;
	private float saturationModifier;

	public SeasoningItem(int hungerRestored, float saturationModifier, Settings settings) {
		super(settings);
		this.hungerRestored = hungerRestored;
		this.saturationModifier = saturationModifier;
	}

	public int getHungerRestored(ItemStack stack) {
		return this.hungerRestored;
	}

	public float getSaturationModifier(ItemStack stack) {
		return this.saturationModifier;
	}
}
