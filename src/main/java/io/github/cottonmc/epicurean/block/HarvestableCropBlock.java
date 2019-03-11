package io.github.cottonmc.epicurean.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextParameters;

import java.util.List;

public class HarvestableCropBlock extends CropBlock {
	public final Item cropItem;
	private final int resetGrowthTo;

	public HarvestableCropBlock(Item cropItem, int resetGrowthTo) {
		super(FabricBlockSettings.of(Material.PLANT).sounds(BlockSoundGroup.CROP).ticksRandomly().breakInstantly().build());
		this.cropItem = cropItem;
		this.resetGrowthTo = resetGrowthTo;
	}

	@Override
	protected ItemProvider getCropItem() {
		return cropItem;
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) return true;
		if (state.get(CropBlock.AGE) == 7) {
			LootContext.Builder ctx = new LootContext
					.Builder(world.getServer().getWorld(world.dimension.getType()))
					.put(LootContextParameters.POSITION, pos).put(LootContextParameters.TOOL, ItemStack.EMPTY);
			List<ItemStack> results = state.getDroppedStacks(ctx);
			for (ItemStack stack : results) {
				ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
				world.spawnEntity(entity);
			}
			world.setBlockState(pos, state.with(CropBlock.AGE, resetGrowthTo));
			return true;
		}
		return false;
	}
}
