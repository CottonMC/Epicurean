package io.github.cottonmc.epicurean.container;

import io.github.cottonmc.epicurean.recipe.EpicureanRecipes;
import net.minecraft.container.CraftingResultSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.DefaultedList;

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
		DefaultedList<ItemStack> remainders = player.world.getRecipeManager().method_8128(EpicureanRecipes.MEAL, this.craftingInv, player.world);

		for(int i = 0; i < remainders.size(); ++i) {
			ItemStack ingredient = this.craftingInv.getInvStack(i);
			ItemStack remainder = remainders.get(i);
			System.out.println(ingredient.getItem() + ": " + remainder.getItem());
			if (!ingredient.isEmpty()) {
				this.craftingInv.takeInvStack(i, 1);
				ingredient = this.craftingInv.getInvStack(i);
			}
			if (!ingredient.getItem().hasRecipeRemainder()) continue;
			if (!remainder.isEmpty()) {
				if (ingredient.isEmpty()) {
					this.craftingInv.setInvStack(i, remainder);
				} else if (ItemStack.areEqualIgnoreTags(ingredient, remainder) && ItemStack.areTagsEqual(ingredient, remainder)) {
					remainder.addAmount(ingredient.getAmount());
					this.craftingInv.setInvStack(i, remainder);
				} else if (!this.player.inventory.insertStack(remainder)) {
					this.player.dropItem(remainder, false);
				}
			}
		}

		return result;
	}
}
