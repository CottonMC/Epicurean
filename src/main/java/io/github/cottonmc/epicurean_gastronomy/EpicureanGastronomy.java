package io.github.cottonmc.epicurean_gastronomy;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EpicureanGastronomy implements ModInitializer {
    public static EpiGastConfig config;
    public static String modId = "epicurean-gastronomy";

    public static final Item JELLY = register("jelly", new JellyItem(1, 0.25f));
    public static final Item SUPER_JELLY = register("super_jelly", new JellyItem(2, 0.3f));

    public static final Item DEP_JELLY = Registry.register(Registry.ITEM, "edibles:jelly", new DeprecatedItem());
    public static final Item DEP_SUPER_JELLY = Registry.register(Registry.ITEM, "edibles:super_jelly", new DeprecatedItem());

    public static RecipeType<AddJellyRecipe> ADD_JELLY = register("add_jelly");
    public static RecipeSerializer<AddJellyRecipe> ADD_JELLY_SERIALIZER = register("add_jelly", new SpecialRecipeSerializer<>(AddJellyRecipe::new));

    public static RecipeType<RemakeJellyRecipe> REMAKE_JELLY = register("remake_jelly");
    public static RecipeSerializer<RemakeJellyRecipe> REMAKE_JELLY_SERIALIZER = register("remake_jelly", new SpecialRecipeSerializer<>(RemakeJellyRecipe::new));

    public static Item register(String name, Item item) {
        Registry.register(Registry.ITEM, new Identifier(modId, name), item);
        return item;
    }

    public static Item mixRegister(String name, Item item) {
        Registry.register(Registry.ITEM, "minecraft:" + name, item);
        return item;
    }

    public static <T extends Recipe<?>> RecipeType<T> register(String id) {
        return Registry.register(Registry.RECIPE_TYPE, new Identifier(id), new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(modId, name), serializer);
    }

    @Override
    public void onInitialize() {
        if (config == null) config = ConfigManager.load(EpiGastConfig.class);
        if (config.omnivoreEnabled) {
            System.out.println("You're feeling hungry...");
            System.out.println("Be warned, this might cause weird behavior!");
        }

    }

}
