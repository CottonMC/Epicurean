package io.github.cottonmc.epicurean.block.crop;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;

public class PickableCropBlock extends HarvestableCropBlock {

	public static final IntProperty AGE = Properties.AGE_3;

	public PickableCropBlock(Item cropItem, int resetGrowthTo) {
		super(cropItem, resetGrowthTo);
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public int getMaxAge() {
		return 3;
	}

	@Override
	public IntProperty getAgeProperty() {
		return AGE;
	}
}
