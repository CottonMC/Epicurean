package io.github.cottonmc.epicurean.mixins;

import io.github.cottonmc.epicurean.item.SpecialFoodItem;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodItemSetting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Items.class)
public class MixinEdibleItems {

	@Shadow
	@Mutable
	@Final
	public static Item IRON_NUGGET = Registry.register(Registry.ITEM, "iron_nugget", new SpecialFoodItem(2, 0.1F, (new Item.Settings()).itemGroup(ItemGroup.MATERIALS)));

	@Shadow
	@Mutable
	@Final
	public static Item GOLD_NUGGET = Registry.register(Registry.ITEM, "gold_nugget", new SpecialFoodItem(4, 0.3F, (new Item.Settings()).itemGroup(ItemGroup.MATERIALS)));

	@Shadow
	@Mutable
	@Final
	public static Item GLISTERING_MELON_SLICE = Registry.register(Registry.ITEM, "glistering_melon_slice", new Item(new Item.Settings().itemGroup(ItemGroup.BREWING).food(new FoodItemSetting.Builder().hunger(4).saturationModifier(0.5F).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 90, 0), 1.0F).alwaysEdible().build())));
}
