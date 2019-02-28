package io.github.cottonmc.epicurean.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
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

	public static CompoundTag mealToNBT(FlavorGroup group, int strength, int duration) {
		CompoundTag tag = new CompoundTag();
		tag.putString("ProminentFlavor", group.toString());
		tag.putInt("Strength", strength);
		tag.putInt("Duration", duration);
		return tag;
	}

	public StatusEffectInstance getMealEffect(ItemStack stack) {
		if (!stack.hasTag() || stack.getTag().containsKey("FlavorProfile")) return new StatusEffectInstance(StatusEffects.SATURATION, 200);
		CompoundTag profile = stack.getTag().getCompound("FlavorProfile");
		FlavorGroup group = FlavorGroup.forName(profile.getString("ProminentFlavor"));
		int strength = profile.getInt("Strength");
		int duration = profile.getInt("Duration");
		return new StatusEffectInstance(group.getEffect(), duration, strength);
	}
}
