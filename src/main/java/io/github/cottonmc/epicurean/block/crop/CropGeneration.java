package io.github.cottonmc.epicurean.block.crop;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.WildCropFeature;

public class CropGeneration {
	public static void registerCrops() {
		for (Biome biome : Registry.BIOME) {
			if (biome.getTemperatureGroup() == Biome.TemperatureGroup.WARM) {
				biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Biome.configureFeature(
						new DesertCropFeature(DefaultFeatureConfig::deserialize, EpicureanCrops.PEPPER_PLANT.getDefaultState().with(PickableCropBlock.AGE, 3)),
						FeatureConfig.DEFAULT,
						Decorator.COUNT_HEIGHTMAP_DOUBLE,
						new CountDecoratorConfig(1)
				));
			}
			if (biome.getPrecipitation() == Biome.Precipitation.RAIN && biome.getTemperatureGroup() == Biome.TemperatureGroup.MEDIUM) {
				biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Biome.configureFeature(
						new WildCropFeature(DefaultFeatureConfig::deserialize, EpicureanCrops.ONION_PLANT.getDefaultState().with(HarvestableCropBlock.AGE, 7)),
						FeatureConfig.DEFAULT,
						Decorator.COUNT_HEIGHTMAP_DOUBLE,
						new CountDecoratorConfig(1)
				));
				biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Biome.configureFeature(
						new WildCropFeature(DefaultFeatureConfig::deserialize, EpicureanCrops.TOMATO_PLANT.getDefaultState().with(HarvestableCropBlock.AGE, 7)),
						FeatureConfig.DEFAULT,
						Decorator.COUNT_HEIGHTMAP_DOUBLE,
						new CountDecoratorConfig(1)
				));
			}
		}
	}
}
