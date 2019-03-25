package io.github.cottonmc.epicurean.block.crop;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.IntegerProperty;
import net.minecraft.state.property.Properties;

public class PickableCropBlock extends HarvestableCropBlock {

	public static final IntegerProperty AGE = Properties.AGE_3;

	public PickableCropBlock(Item cropItem, int resetGrowthTo) {
		super(cropItem, resetGrowthTo);
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		builder.with(AGE);
	}

	@Override
	public int getCropAgeMaximum() {
		return 3;
	}

	@Override
	public IntegerProperty getAgeProperty() {
		return AGE;
	}
}
