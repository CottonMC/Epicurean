package io.github.cottonmc.epicurean.recipe;

import com.google.gson.JsonObject;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class MealRecipeSerializer implements RecipeSerializer<MealRecipe> {
	
	@Override
	public MealRecipe read(Identifier var1, JsonObject var2) {
		return null;
	}

	@Override
	public MealRecipe read(Identifier var1, PacketByteBuf var2) {
		return null;
	}

	@Override
	public void write(PacketByteBuf var1, MealRecipe var2) {

	}
}
