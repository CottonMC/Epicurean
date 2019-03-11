package io.github.cottonmc.epicurean.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class JellyItem extends SeasoningItem {
	public JellyItem(int hunger, float saturation) {
		super(hunger, saturation, EpicureanItems.DEFAULT_SETTINGS.recipeRemainder(Items.GLASS_BOTTLE));
	}

	@Override
	public boolean hasEnchantmentGlint(ItemStack stack) {
		return stack.getItem() == EpicureanItems.SUPER_JELLY;
	}

}
