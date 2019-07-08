package io.github.cottonmc.epicurean.recipe;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import io.github.cottonmc.epicurean.item.EpicureanItems;
import io.github.cottonmc.epicurean.item.JellyItem;
import io.github.cottonmc.epicurean.item.SpecialFoodItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class AddJellyRecipe extends SpecialCraftingRecipe {

	public AddJellyRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		ItemStack targetFood = ItemStack.EMPTY;
		ItemStack targetJelly = ItemStack.EMPTY;
		if (inv == null) return false;
		else {
			for (int i = 0; i < inv.getInvSize(); i++) {
				ItemStack stack = inv.getInvStack(i);
				if (!stack.isEmpty()) {
					if (stack.getItem() instanceof JellyItem) {
						if (!targetJelly.isEmpty()) return false;
						targetJelly = stack;
					} else if (stack.getItem().isFood()) {
						if (!targetFood.isEmpty()) return false;
						targetFood = stack;
					} else {
						return false;
					}
				}
			}
		}
		if (targetFood.getItem() instanceof SpecialFoodItem && !EpicureanGastronomy.config.edibleNuggets) return false;
		return (!targetFood.hasTag() || (!targetFood.getTag().containsKey("jellied") && !targetFood.getTag().containsKey("super_jellied"))) && !targetJelly.isEmpty();
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack food = ItemStack.EMPTY;
		ItemStack jelly = ItemStack.EMPTY;
		for (int i = 0; i < inv.getInvSize(); i++) {
			ItemStack stack = inv.getInvStack(i);
			if (stack.getItem() instanceof JellyItem) {
				jelly = stack;
			} else if (stack.getItem().isFood()) {
				food = stack.copy();
			}
		}
		if (food.isEmpty()) return food;
		food.setCount(1); //otherwise every crafting op will give 64 food back
		CompoundTag tag = food.getOrCreateTag();
		if (jelly.getItem() == EpicureanItems.SUPER_JELLY) tag.putByte("super_jellied", (byte)0);
		else tag.putByte("jellied", (byte)0);
		return food;
	}

	@Override
	public boolean fits(int x, int y) {
		return x >= 2 && y >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return EpicureanRecipes.ADD_JELLY_SERIALIZER;
	}
}
