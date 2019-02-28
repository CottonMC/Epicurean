package io.github.cottonmc.epicurean;

import io.github.cottonmc.epicurean.item.EpicureanItems;
import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import io.github.cottonmc.epicurean.recipe.EpicureanRecipes;
import net.fabricmc.api.ModInitializer;

public class EpicureanGastronomy implements ModInitializer {
    public static EpicureanConfig config;
    public static String MOD_ID = "epicurean";

    @Override
    public void onInitialize() {
        if (config == null) config = ConfigManager.load(EpicureanConfig.class);
        if (config.omnivoreEnabled) {
            System.out.println("You're feeling hungry...");
            System.out.println("Be warned, this might cause weird behavior!");
        }
        EpicureanItems.init();
        EpicureanRecipes.init();
        IngredientProfiles.init();
    }

}
