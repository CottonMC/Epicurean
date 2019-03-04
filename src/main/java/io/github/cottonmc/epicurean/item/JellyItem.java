package io.github.cottonmc.epicurean.item;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;

public class JellyItem extends FoodItem {
	public JellyItem(int hunger, float saturation) {
		super(hunger, saturation, false, new Item.Settings().itemGroup(EpicureanGastronomy.EPICUREAN_GROUP).recipeRemainder(Items.GLASS_BOTTLE));
	}

	@Override
	public ItemStack onItemFinishedUsing(ItemStack stack, World world, LivingEntity entity) {
		super.onItemFinishedUsing(stack, world, entity);
		if (stack.getAmount() >= 1 && entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)entity;
			player.inventory.insertStack(new ItemStack(Items.GLASS_BOTTLE));
			return stack;
		}
		else return new ItemStack(Items.GLASS_BOTTLE);
	}

	@Override
	public boolean hasEnchantmentGlint(ItemStack stack) {
		return stack.getItem() == EpicureanItems.SUPER_JELLY;
	}

}
