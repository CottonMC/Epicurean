package io.github.cottonmc.epicurean.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class JellyItem extends SeasoningItem {
	public JellyItem(int hunger, float saturation) {
		super(hunger, saturation, EpicureanItems.defaultSettings().recipeRemainder(Items.GLASS_BOTTLE));
	}

	@Override
	public boolean hasEnchantmentGlint(ItemStack stack) {
		return stack.getItem() == EpicureanItems.SUPER_JELLY;
	}

	@Override
	public StatusEffectInstance getBonusEffect(ItemStack stack) {
		if (stack.getItem() == EpicureanItems.SUPER_JELLY) return new StatusEffectInstance(StatusEffects.JUMP_BOOST, 1200);
		else return null;
	}
}
