package io.github.cottonmc.epicurean.block.crop;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.WildCropFeature;

import java.util.Random;
import java.util.function.Function;

public class DesertCropFeature extends WildCropFeature {
	public DesertCropFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> config, BlockState state) {
		super(config, state);
	}

	@Override
	public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> gen, Random random, BlockPos pos, DefaultFeatureConfig config) {
		int placed = 0;

		for(int i = 0; i < 64; i++) {
			BlockPos placePos = pos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
			if (world.isAir(placePos) && (world.getBlockState(placePos.down()).getBlock() == Blocks.SAND || world.getBlockState(placePos.down()).getBlock() == Blocks.GRASS_BLOCK)) {
				world.setBlockState(placePos, this.crop, 2);
				placed++;
			}
		}

		return placed > 0;
	}
}
