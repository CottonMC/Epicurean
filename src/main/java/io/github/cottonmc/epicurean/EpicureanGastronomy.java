package io.github.cottonmc.epicurean;

import io.github.cottonmc.cotton.logging.ModLogger;
import io.github.cottonmc.epicurean.block.EpicureanBlocks;
import io.github.cottonmc.epicurean.block.EpicureanCrops;
import io.github.cottonmc.epicurean.item.EpicureanItems;
import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import io.github.cottonmc.epicurean.recipe.EpicureanRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class EpicureanGastronomy implements ModInitializer {
    public static EpicureanConfig config;
    public static String MOD_ID = "epicurean";
    public static final ItemGroup EPICUREAN_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "epicurean_group"), () -> new ItemStack(EpicureanItems.JELLY));

    public static final ModLogger LOGGER = new ModLogger("Epicurean Gastronomy");

    @Override
    public void onInitialize() {
        if (config == null) config = ConfigManager.load(EpicureanConfig.class);
        if (config.omnivoreEnabled) {
            LOGGER.info("You're feeling hungry...");
            LOGGER.info("Be warned, this might cause weird behavior!");
        }
        EpicureanItems.init();
        EpicureanBlocks.init();
        EpicureanCrops.init();
        EpicureanRecipes.init();
        ResourceManagerHelper.get(ResourceType.DATA).registerReloadListener(new IngredientProfiles());
    }

}
