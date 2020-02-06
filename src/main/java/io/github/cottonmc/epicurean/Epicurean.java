package io.github.cottonmc.epicurean;

import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.logging.ModLogger;
import io.github.cottonmc.epicurean.block.crop.CropGeneration;
import io.github.cottonmc.epicurean.block.EpicureanBlocks;
import io.github.cottonmc.epicurean.block.crop.EpicureanCrops;
import io.github.cottonmc.epicurean.item.EpicureanItems;
import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import io.github.cottonmc.epicurean.meal.MealBooster;
import io.github.cottonmc.epicurean.meal.SkillCheckMealBooster;
import io.github.cottonmc.epicurean.recipe.EpicureanRecipes;
import io.github.cottonmc.epicurean.util.EpicureanConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.client.ItemTooltipCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class Epicurean implements ModInitializer {
    public static EpicureanConfig config;
    public static String MOD_ID = "epicurean";
    public static final ItemGroup EPICUREAN_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "epicurean_group"), () -> new ItemStack(EpicureanItems.JELLY));

    public static final ModLogger LOGGER = new ModLogger(MOD_ID, "Epicurean");

    @Override
    public void onInitialize() {
        config = ConfigManager.loadConfig(EpicureanConfig.class, "Epicurean.json5");
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
        CropGeneration.registerCrops();
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new IngredientProfiles());

        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltips) -> {
            if (IngredientProfiles.MEAL_INGREDIENTS.containsKey(stack.getItem())) {
                String flavor = new TranslatableText("tooltip.epicurean.flavor." + IngredientProfiles.MEAL_INGREDIENTS.get(stack.getItem()).asString()).asString();
                tooltips.add(new TranslatableText("tooltip.epicurean.ingredient", flavor).formatted(Formatting.GRAY, Formatting.ITALIC));
            } else if (IngredientProfiles.DRESSINGS.containsKey(stack.getItem())) {
                String flavor = new TranslatableText("tooltip.epicurean.flavor." + IngredientProfiles.DRESSINGS.get(stack.getItem()).asString()).asString();
                tooltips.add(new TranslatableText("tooltip.epicurean.dressing", flavor).formatted(Formatting.GRAY, Formatting.ITALIC));
            }
            if (stack.getItem().isFood()) {
                if (stack.hasTag())  {
                    if (stack.getTag().contains("jellied")) {
                        tooltips.add(new TranslatableText("tooltip.epicurean.jellied").formatted(Formatting.DARK_RED));
                    } else if (stack.getTag().contains("super_jellied")) {
                        tooltips.add(new TranslatableText("tooltip.epicurean.super_jellied").formatted(Formatting.GREEN));
                    }
                }
            }
        });

//        if (FabricLoader.getInstance().isModLoaded("skillcheck")) MealBooster.BOOSTERS.add(new SkillCheckMealBooster());
    }

}
