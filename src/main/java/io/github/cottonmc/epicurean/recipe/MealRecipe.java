package io.github.cottonmc.epicurean.recipe;

import com.google.gson.JsonObject;
import io.github.cottonmc.epicurean.block.CookingInventory;
import io.github.cottonmc.epicurean.meal.FlavorGroup;
import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.crafting.CraftingRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MealRecipe implements CraftingRecipe {
	private final Identifier id;
	private final String group;
	private final ItemStack output;
	private final DefaultedList<Ingredient> base;
	private final DefaultedList<Ingredient> seasonings;

	public MealRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> base, DefaultedList<Ingredient> seasonings) {
		this.id = id;
		this.group = group;
		this.output = output;
		this.base = base;
		this.seasonings = seasonings;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		if (!(inv instanceof CookingInventory)) return false;
		RecipeFinder finder = new RecipeFinder();
		int foundBases = 0;

		for(int i = 0; i < CookingInventory.SECTION_SIZE; i++) {
			ItemStack stack = inv.getInvStack(i);
			if (!stack.isEmpty()) {
				++foundBases;
				finder.addItem(stack);
			}
		}

		//make sure the seasonings fit on the meal
		for (int i = CookingInventory.SECTION_SIZE; i < inv.getInvSize(); i++) {
			ItemStack stack = inv.getInvStack(i);
			boolean seasoningFound = false;
			for (Ingredient ing : seasonings) {
				ItemStack[] stacks = ing.getStackArray();
				for (ItemStack ingStack : stacks) {
					if (ingStack.getItem() == stack.getItem()) seasoningFound = true;
				}
			}
			if (!seasoningFound) return false;
		}

		return foundBases == this.base.size() && finder.findRecipe(this, null);
	}

	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return base;
	}

	public DefaultedList<Ingredient> getSeasonings() {
		return seasonings;
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack meal = this.output.copy();
		if (!meal.hasTag()) meal.setTag(new CompoundTag());
		int prominence = 0;
		List<ItemStack> ingredients = new ArrayList<>();
		for (int i = 0; i < inv.getInvSize(); i++) {
			if (!inv.getInvStack(i).isEmpty()) ingredients.add(inv.getInvStack(i));
		}
		List<ItemStack> seasonings = new ArrayList<>();
		for (int i = CookingInventory.SECTION_SIZE; i < inv.getInvSize(); i++) {
			if (!inv.getInvStack(i).isEmpty()) seasonings.add(inv.getInvStack(i));
		}
		for (ItemStack ingredient : ingredients) {
			Item item = ingredient.getItem();
			if (IngredientProfiles.MEAL_INGREDIENTS.containsKey(item)) {
				prominence = Math.max(prominence, IngredientProfiles.MEAL_INGREDIENTS.get(item).getImpact());
			}
		}
		//TODO: figure out how we want to calculate strength + duration
		meal.getTag().put("FlavorProfile", makeFlavorProfile(FlavorGroup.forImpact(prominence), 0, 200));
		meal.getTag().put("Seasonings", makeIngredientList(seasonings));
		return meal;
	}

	@Override
	public boolean fits(int x, int y) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return this.output;
	}

	@Override
	public Identifier getId() {
		return this.id;
	}

	@Environment(EnvType.CLIENT)
	public String getGroup() {
		return this.group;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return null;
	}

	public static CompoundTag makeFlavorProfile(FlavorGroup group, int strength, int duration) {
		CompoundTag tag = new CompoundTag();
		tag.putString("ProminentFlavor", group.toString());
		tag.putInt("Strength", strength);
		tag.putInt("Duration", duration);
		return tag;
	}

	public static CompoundTag makeIngredientList(List<ItemStack> ingredients) {
		CompoundTag tag = new CompoundTag();
		for (ItemStack stack : ingredients) {
			String name = Registry.ITEM.getId(stack.getItem()).toString();
			if (!tag.containsKey(name)) {
				tag.putInt(name, 1);
			} else {
				tag.putInt(name, tag.getInt(name) + 1);
			}
		}
		return tag;
	}
}
