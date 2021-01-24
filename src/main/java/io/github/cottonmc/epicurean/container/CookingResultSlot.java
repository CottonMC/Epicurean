package io.github.cottonmc.epicurean.container;

import io.github.cottonmc.epicurean.recipe.EpicureanRecipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.util.collection.DefaultedList;

public class CookingResultSlot extends CraftingResultSlot {
	private final CraftingInventory craftingInv;
	private final PlayerEntity player;

	public CookingResultSlot(PlayerEntity player, CraftingInventory craftInv, Inventory resultInv, int invSlot, int x, int y) {
		super(player, craftInv, resultInv, invSlot, x, y);
		this.craftingInv = craftInv;
		this.player = player;
	}

	@Override
	public ItemStack onTakeItem(PlayerEntity player, ItemStack result) {
		this.onCrafted(result);
		DefaultedList<ItemStack> remainders = player.world.getRecipeManager().getRemainingStacks(EpicureanRecipes.MEAL, this.craftingInv, player.world);

		for(int i = 0; i < remainders.size(); ++i) {
			ItemStack ingredient = this.craftingInv.getStack(i);
			ItemStack remainder = remainders.get(i);
			if (!ingredient.isEmpty()) {
				this.craftingInv.removeStack(i, 1);
				ingredient = this.craftingInv.getStack(i);
			}

			if (!remainder.isEmpty()) {
				if (ingredient.isEmpty()) {
					this.craftingInv.setStack(i, remainder);
				} else if (ItemStack.areItemsEqual(ingredient, remainder) && ItemStack.areTagsEqual(ingredient, remainder)) {
					remainder.increment(ingredient.getCount());
					this.craftingInv.setStack(i, remainder);
				} else if (!this.player.inventory.insertStack(remainder)) {
					this.player.dropItem(remainder, false);
				}
			}
		}

		return result;
	}
}
