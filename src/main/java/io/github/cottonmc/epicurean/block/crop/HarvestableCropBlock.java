package io.github.cottonmc.epicurean.block.crop;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextParameters;

import java.util.List;

public class HarvestableCropBlock extends CropBlock {
	public final Item cropItem;
	private final int resetGrowthTo;
	public static final IntProperty AGE = Properties.AGE_7;

	public HarvestableCropBlock(Item cropItem, int resetGrowthTo) {
		super(FabricBlockSettings.of(Material.PLANT).sounds(BlockSoundGroup.CROP).ticksRandomly().breakInstantly().build().noCollision());
		this.cropItem = cropItem;
		this.resetGrowthTo = resetGrowthTo;
		this.setDefaultState((this.stateFactory.getDefaultState()).with(this.getAgeProperty(), 0));
	}

	@Override
	public IntProperty getAgeProperty() {
		return super.getAgeProperty();
	}

	@Override
	protected ItemConvertible getSeedsItem() {
		return cropItem;
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) return true;
		if (getAge(state) >= getMaxAge()) {
			if (world.getBlockState(pos.offset(Direction.DOWN)).getBlock() == Blocks.FARMLAND) {
				LootContext.Builder ctx = new LootContext
						.Builder(world.getServer().getWorld(world.dimension.getType()))
						.put(LootContextParameters.POSITION, pos).put(LootContextParameters.TOOL, ItemStack.EMPTY);
				List<ItemStack> results = state.getDroppedStacks(ctx);
				for (ItemStack stack : results) {
					ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
					world.spawnEntity(entity);
				}
				world.setBlockState(pos, state.with(getAgeProperty(), resetGrowthTo));
			} else {
				world.breakBlock(pos, true);
			}
			return true;
		}
		return false;
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
}
