package io.github.cottonmc.epicurean.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.StringItem;
import net.minecraft.util.DefaultedList;

public class PlantableItem extends StringItem {

	public PlantableItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Override
	public void appendItemsForGroup(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (this.isInItemGroup(group)) {
			stacks.add(new ItemStack(this));
		}
	}

	@Override
	public boolean hasRecipeRemainder() {
		return false;
	}
}
