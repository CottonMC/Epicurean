package io.github.cottonmc.epicurean.block.crop;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;

public class PickableCropBlock extends HarvestableCropBlock {

	public static final IntProperty AGE = Properties.AGE_3;

	public PickableCropBlock(Item cropItem, int resetGrowthTo) {
		super(cropItem, resetGrowthTo);
	}

	@Override
	public int getMaxAge() {
		return 3;
	}

	@Override
	public IntProperty getAgeProperty() {
		return AGE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//		super.appendProperties(builder); //TODO: this appends AGE_7 when I don't want it to, will that break things?
		builder.add(AGE);
	}
}
