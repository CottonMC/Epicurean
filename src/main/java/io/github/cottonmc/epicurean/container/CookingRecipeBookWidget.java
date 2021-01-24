package io.github.cottonmc.epicurean.container;

import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

import java.util.Iterator;
import java.util.List;

public class CookingRecipeBookWidget extends RecipeBookWidget {

	public CookingRecipeBookWidget() {

	}

	public void showGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
		ItemStack out = recipe.getOutput();
		this.ghostSlots.setRecipe(recipe);
		this.ghostSlots.addSlot(Ingredient.ofStacks(out), slots.get(0).x, slots.get(0).y);
		DefaultedList<Ingredient> inputs = recipe.getPreviewInputs();

		Iterator<Ingredient> itr = inputs.iterator();

		for(int i = 1; i < 14; i++) {
			if (!itr.hasNext()) {
				return;
			}

			Ingredient ingredient = itr.next();
			if (!ingredient.isEmpty()) {
				Slot slot = slots.get(i);
				this.ghostSlots.addSlot(ingredient, slot.x, slot.y);
			}
		}
	}

}
