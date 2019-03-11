package io.github.cottonmc.epicurean.mixins;

import io.github.cottonmc.epicurean.item.Seasoning;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.EggItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EggItem.class)
public class MixinEggSeasoning implements Seasoning {
	@Override
	public int getHungerRestored(ItemStack stack) {
		return 0;
	}

	@Override
	public float getSaturationModifier(ItemStack stack) {
		return 0;
	}

	@Override
	public StatusEffectInstance getBonusEffect(ItemStack stack) {
		return new StatusEffectInstance(StatusEffects.ABSORPTION, 1200);
	}
}
