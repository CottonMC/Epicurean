package io.github.cottonmc.epicurean.recipe;

import com.google.gson.JsonObject;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class DressingMealSerializer<T extends DressingMealRecipe> implements RecipeSerializer<T> {
	private final Function<Identifier, T> id;

	public DressingMealSerializer(Function<Identifier, T> function_1) {
		this.id = function_1;
	}

	public T read(Identifier identifier_1, JsonObject jsonObject_1) {
		return this.id.apply(identifier_1);
	}

	public T read(Identifier identifier_1, PacketByteBuf packetByteBuf_1) {
		return this.id.apply(identifier_1);
	}

	public void write(PacketByteBuf packetByteBuf_1, T recipe_1) {
	}
}
