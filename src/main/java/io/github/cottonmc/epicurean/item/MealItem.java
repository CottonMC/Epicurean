package io.github.cottonmc.epicurean.item;

import io.github.cottonmc.epicurean.meal.FlavorGroup;
import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class MealItem extends FoodItem {

	public MealItem(int hunger, float saturation, boolean wolfFood, StatusEffectInstance eatingEffect) {
		super(hunger, saturation, wolfFood, new Item.Settings().itemGroup(ItemGroup.FOOD));
		this.setStatusEffect(eatingEffect, 1);
	}

	@Override
	protected void onConsumed(ItemStack stack, World world, PlayerEntity player) {
		this.setStatusEffect(getMealEffect(stack), 1);
		super.onConsumed(stack, world, player);

	}

	public static CompoundTag makeFlavorProfile(FlavorGroup group, int strength, int duration) {
		CompoundTag tag = new CompoundTag();
		tag.putString("ProminentFlavor", group.toString());
		tag.putInt("Strength", strength);
		tag.putInt("Duration", duration);
		return tag;
	}

	public static CompoundTag makeIngredientList(ItemStack...ingredients) {
		CompoundTag tag = new CompoundTag();
		for (ItemStack stack : ingredients) {
			String name = Registry.ITEM.getId(stack.getItem()).toString();
			if (!tag.containsKey(name)) {
				tag.putInt(name, 1);
			} else {
				tag.putInt(name, tag.getInt(name) + 1);
			}
		}
		return tag;
	}

	public static StatusEffectInstance getMealEffect(ItemStack stack) {
		if (!stack.hasTag() || stack.getTag().containsKey("FlavorProfile")) return new StatusEffectInstance(StatusEffects.SATURATION, 200);
		CompoundTag profile = stack.getTag().getCompound("FlavorProfile");
		FlavorGroup group = FlavorGroup.forName(profile.getString("ProminentFlavor"));
		int strength = profile.getInt("Strength");
		int duration = profile.getInt("Duration");
		return new StatusEffectInstance(group.getEffect(), duration, strength);
	}

	//TODO: placing here till I decide what I wanna do with crafting
	public static ItemStack assembleMeal(ItemStack meal, ItemStack[] ingredients) {
		if (!meal.hasTag()) meal.setTag(new CompoundTag());
		int prominence = 0;
		for (ItemStack ingredient : ingredients) {
			Item item = ingredient.getItem();
			if (IngredientProfiles.MEAL_INGREDIENTS.containsKey(item)) {
				prominence = Math.max(prominence, IngredientProfiles.MEAL_INGREDIENTS.get(item).getImpact());
			}
		}
		//TODO: figure out how we want to calculate strength + duration
		meal.getTag().put("FlavorProfile", makeFlavorProfile(FlavorGroup.forImpact(prominence), 0, 200));
		meal.getTag().put("Ingredients", makeIngredientList(ingredients));
		return meal;
	}
}
