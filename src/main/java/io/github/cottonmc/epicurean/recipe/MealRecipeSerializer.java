package io.github.cottonmc.epicurean.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class MealRecipeSerializer implements RecipeSerializer<MealRecipe> {

	@Override
	public MealRecipe read(Identifier id, JsonObject json) {
		String group = JsonHelper.getString(json, "group", "");
		DefaultedList<Ingredient> base = getIngredients(JsonHelper.getArray(json, "ingredients"));
		DefaultedList<Ingredient> seasoning = getIngredients(JsonHelper.getArray(json, "seasonings"));
		if (base.isEmpty()) throw new JsonParseException("No base ingredients for meal!");
		else if (base.size() > 6) throw new JsonParseException("Too many base ingredients for meal!");
		else if (seasoning.size() > 6) throw new JsonParseException("Too many seasoning ingredients for meal!");
		else {
			ItemStack result = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
			return new MealRecipe(id, group, result, base, seasoning);
		}
	}

	@Override
	public MealRecipe read(Identifier id, PacketByteBuf buf) {
		String group = buf.readString(32767);
		int baseSize = buf.readVarInt();
		DefaultedList<Ingredient> base = DefaultedList.ofSize(baseSize, Ingredient.EMPTY);

		for(int i = 0; i < base.size(); i++) {
			base.set(i, Ingredient.fromPacket(buf));
		}

		int seasoningSize = buf.readVarInt();
		DefaultedList<Ingredient> seasoning = DefaultedList.ofSize(seasoningSize, Ingredient.EMPTY);

		for(int i = 0; i < seasoning.size(); i++) {
			seasoning.set(i, Ingredient.fromPacket(buf));
		}

		ItemStack output = buf.readItemStack();
		return new MealRecipe(id, group, output, base, seasoning);
	}

	@Override
	public void write(PacketByteBuf buf, MealRecipe recipe) {
		buf.writeString(recipe.getGroup());

		buf.writeVarInt(recipe.getPreviewInputs().size());
		for (Ingredient ingredient : recipe.getPreviewInputs()) {
			ingredient.write(buf);
		}

		buf.writeVarInt(recipe.getSeasonings().size());
		for (Ingredient ingredient : recipe.getSeasonings()) {
			ingredient.write(buf);
		}

		buf.writeItemStack(recipe.getOutput());
	}

	private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
		DefaultedList<Ingredient> ingredients = DefaultedList.of();

		for(int i = 0; i < json.size(); i++) {
			Ingredient ingredient = Ingredient.fromJson(json.get(i));
			if (!ingredient.isEmpty()) {
				ingredients.add(ingredient);
			}
		}

		return ingredients;
	}
}
