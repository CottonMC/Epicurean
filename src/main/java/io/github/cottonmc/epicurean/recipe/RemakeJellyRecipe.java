package io.github.cottonmc.epicurean.recipe;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.crafting.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class RemakeJellyRecipe extends SpecialCraftingRecipe {

	private Identifier oldId;

	public RemakeJellyRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		oldId = null;
		if (inv == null) return false;
			for (int i = 0; i < inv.getInvSize(); i++) {
				ItemStack stack = inv.getInvStack(i);
				if (!stack.isEmpty()) {
					Identifier id = Registry.ITEM.getId(stack.getItem());
					if (oldId != null) return false;
					if (id.getNamespace().equals("edibles")) {
						oldId = id;
					}
				}
			}
		return oldId != null;
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		Identifier newId = new Identifier(EpicureanGastronomy.MOD_ID, oldId.getPath());
		return new ItemStack(Registry.ITEM.get(newId));
	}

	@Override
	public boolean fits(int i, int i1) {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return EpicureanRecipes.REMAKE_JELLY_SERIALIZER;
	}
}
