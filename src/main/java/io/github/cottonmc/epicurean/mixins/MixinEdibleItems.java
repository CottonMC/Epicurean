package io.github.cottonmc.epicurean.mixins;

import io.github.cottonmc.epicurean.item.SpecialFoodItem;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Items.class)
public class MixinEdibleItems {
	//TODO: keep this???

	@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", ordinal = 289))
	private static Item swapIronNugget(Item original) {
		return new SpecialFoodItem(4, 0.3f, new Item.Settings().group(ItemGroup.MATERIALS));
	}

	@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", ordinal = 146))
	private static Item swapGoldNugget(Item original) {
		return new SpecialFoodItem(4, 0.3f, new Item.Settings().group(ItemGroup.MATERIALS));
	}

	@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", ordinal = 155))
	private static Item swapGlisteringMelon(Item original) {
		return new Item(new Item.Settings().group(ItemGroup.BREWING).food(new FoodComponent.Builder().hunger(4).saturationModifier(0.5F).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 90, 0), 1.0F).alwaysEdible().build()));
	}

}
