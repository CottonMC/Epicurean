package io.github.cottonmc.epicurean;

import io.github.cottonmc.epicurean.block.EpicureanBlocks;
import io.github.cottonmc.epicurean.block.crop.EpicureanCrops;
import io.github.cottonmc.epicurean.item.EpicureanItems;
import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import io.github.cottonmc.epicurean.recipe.EpicureanRecipes;
import io.github.cottonmc.epicurean.util.EpicureanConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;

public class Epicurean implements ModInitializer {
    public static EpicureanConfig config;
    public static String MOD_ID = "epicurean";
    public static final ItemGroup EPICUREAN_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "epicurean_group"), () -> new ItemStack(EpicureanItems.JELLY));

    public static final Logger LOGGER = LogManager.getLogger("Epicurean");

    @Override
    public void onInitialize() {
        config = new EpicureanConfig();
//        config = ConfigManager.loadConfig(EpicureanConfig.class, "Epicurean.json5");
        if (config.omnivoreEnabled) {
            LOGGER.info("You're feeling hungry...");
            LOGGER.info("Be warned, this might cause weird behavior!");
        }
        if (config.useSaturationOnly) {
            LOGGER.info("Saturation-only mode activated.");
        }
        EpicureanItems.init();
        EpicureanBlocks.init();
        EpicureanCrops.init();
        EpicureanRecipes.init();
//        CropGeneration.registerCrops();
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new IngredientProfiles());

//        if (FabricLoader.getInstance().isModLoaded("skillcheck")) MealBooster.BOOSTERS.add(new SkillCheckMealBooster()); TODO: port SkillCheck back up
    }

}
