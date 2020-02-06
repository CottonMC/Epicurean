package io.github.cottonmc.epicurean.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(NetherWartBlock.class)
public class MixinFertilizableNetherWart implements Fertilizable {
	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean b) {
		return state.get(NetherWartBlock.AGE) < 3;
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		int age = state.get(NetherWartBlock.AGE);
		if (age < 3) world.setBlockState(pos, state.with(NetherWartBlock.AGE, age + 1));
	}
}
