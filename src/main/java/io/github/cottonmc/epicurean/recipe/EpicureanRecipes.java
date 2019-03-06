package io.github.cottonmc.epicurean.recipe;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EpicureanRecipes {
	public static RecipeType<AddJellyRecipe> ADD_JELLY = register("add_jelly");
	public static RecipeSerializer<AddJellyRecipe> ADD_JELLY_SERIALIZER = register("add_jelly", new SpecialRecipeSerializer<>(AddJellyRecipe::new));

	public static RecipeType<MealRecipe> MEAL = register("meal");
	public static RecipeSerializer<MealRecipe> MEAL_SERIALIZER = register("meal", new MealRecipeSerializer());

	public static RecipeType<DressingMealRecipe> DRESSING_MEAL = register("add_dressing");
	public static RecipeSerializer<DressingMealRecipe> DRESSING_MEAL_SERIALIZER = register("add_dressing", new DressingMealSerializer<>(DressingMealRecipe::new));

	public static RecipeType<RemakeJellyRecipe> REMAKE_JELLY = register("remake_jelly");
	public static RecipeSerializer<RemakeJellyRecipe> REMAKE_JELLY_SERIALIZER = register("remake_jelly", new SpecialRecipeSerializer<>(RemakeJellyRecipe::new));

	public static <T extends Recipe<?>> RecipeType<T> register(String id) {
		return Registry.register(Registry.RECIPE_TYPE, new Identifier(id), new RecipeType<T>() {
			public String toString() {
				return id;
			}
		});
	}

	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(EpicureanGastronomy.MOD_ID, name), serializer);
	}

	public static void init() {

	}
}
