package io.github.cottonmc.epicurean.block;

import net.minecraft.container.Container;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.util.DefaultedList;

public class CookingInventory extends CraftingInventory {
	public static final int SECTION_SIZE = 6;
	private final DefaultedList<ItemStack> stacks;

	public CookingInventory(Container container) {
		super(container, SECTION_SIZE, 2);
		this.stacks = DefaultedList.create(SECTION_SIZE * 2, ItemStack.EMPTY);
	}

	public void provideRecipeInputs(RecipeFinder finder) {
		//only get the items from the first section, since that's all we care about for matching
		for (int i = 0; i < SECTION_SIZE; i++) {
			ItemStack stack = stacks.get(i);
			finder.addNormalItem(stack);
		}

	}
}
