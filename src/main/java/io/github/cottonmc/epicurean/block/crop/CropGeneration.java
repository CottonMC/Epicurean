package io.github.cottonmc.epicurean.block.crop;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleStateProvider;

public class CropGeneration {
	public static void registerCrops() {
		for (Biome biome : Registry.BIOME) {
			if (biome.getTemperatureGroup() == Biome.TemperatureGroup.WARM) {
				biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
						Feature.RANDOM_PATCH.configure(
								createPlantFeature(
										EpicureanCrops.PEPPER_PLANT.getDefaultState().with(PickableCropBlock.AGE, 3),
										Blocks.GRASS_BLOCK, Blocks.SAND))
								.createDecoratedFeature(
										Decorator.CHANCE_HEIGHTMAP_DOUBLE.configure(
												new ChanceDecoratorConfig(1)
										)
								)
				);
			}
			if (biome.getPrecipitation() == Biome.Precipitation.RAIN && biome.getTemperatureGroup() == Biome.TemperatureGroup.MEDIUM) {
				biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
						Feature.RANDOM_PATCH.configure(
								createPlantFeature(
										EpicureanCrops.ONION_PLANT.getDefaultState().with(HarvestableCropBlock.AGE, 7),
										Blocks.GRASS_BLOCK))
								.createDecoratedFeature(
										Decorator.CHANCE_HEIGHTMAP_DOUBLE.configure(
												new ChanceDecoratorConfig(1)
										)
								)
				);
				biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
						Feature.RANDOM_PATCH.configure(
								createPlantFeature(
										EpicureanCrops.TOMATO_PLANT.getDefaultState().with(HarvestableCropBlock.AGE, 7),
										Blocks.GRASS_BLOCK))
								.createDecoratedFeature(
										Decorator.CHANCE_HEIGHTMAP_DOUBLE.configure(
												new ChanceDecoratorConfig(1)
										)
								)
				);
			}
		}
	}

	private static RandomPatchFeatureConfig createPlantFeature(BlockState state, Block...plantOn) {
		return new RandomPatchFeatureConfig.Builder(
				new SimpleStateProvider(state),
				new SimpleBlockPlacer()
		)
				.tries(64)
				.whitelist(ImmutableSet.copyOf(plantOn))
				.cannotProject()
				.build();
	}
}
