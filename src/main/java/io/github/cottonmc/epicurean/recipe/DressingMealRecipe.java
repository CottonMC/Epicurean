package io.github.cottonmc.epicurean.recipe;

import io.github.cottonmc.epicurean.container.CookingInventory;
import io.github.cottonmc.epicurean.item.EpicureanItems;
import io.github.cottonmc.epicurean.item.MealItem;
import io.github.cottonmc.epicurean.meal.FlavorGroup;
import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class DressingMealRecipe extends MealRecipe {

	ItemStack meal = ItemStack.EMPTY;

	public DressingMealRecipe(Identifier id) {
		super(id, "", ItemStack.EMPTY, DefaultedList.create(), DefaultedList.create());
	}

	@Override
	public RecipeType<?> getType() {
		return EpicureanRecipes.MEAL;
	}

	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		meal = ItemStack.EMPTY;
		if (!(inv instanceof CookingInventory)) return false;
		ItemStack targetMeal = ItemStack.EMPTY;
		for(int i = 0; i < CookingInventory.SECTION_SIZE; i++) {
			ItemStack stack = inv.getInvStack(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof MealItem) {
					if (!targetMeal.isEmpty()) return false;
					else targetMeal = stack;
				}
			}
		}
		int dressings = 0;
		for (int i = CookingInventory.SECTION_SIZE; i < inv.getInvSize(); i++) {
			if (!inv.getInvStack(i).isEmpty()) {
				if (inv.getInvStack(i).getItem() == EpicureanItems.SALT || IngredientProfiles.DRESSINGS.containsKey(inv.getInvStack(i).getItem())) dressings++;
				else return false;
			}
		}
		CompoundTag profile = targetMeal.getOrCreateSubCompoundTag("FlavorProfile");
		if (profile.containsKey("Seasonings")) {
			CompoundTag seasonings = profile.getCompound("Seasonings");
			int existingSeasonings = 0;
			for (String seasoning : seasonings.getKeys()) {
				existingSeasonings += seasonings.getInt(seasoning);
			}
			if (existingSeasonings + dressings > 6) return false;
		}
		if (!targetMeal.isEmpty()) meal = targetMeal;
		return (!targetMeal.isEmpty() && dressings > 0);
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack meal = this.meal.copy();
		CompoundTag tag = meal.getTag();
		if (meal.hasTag() && tag.containsKey("FlavorProfile")) {
			CompoundTag profile = tag.getCompound("FlavorProfile");
			List<ItemStack> seasonings = new ArrayList<>();
			for (int i = CookingInventory.SECTION_SIZE; i < inv.getInvSize(); i++) {
				if (!inv.getInvStack(i).isEmpty()) seasonings.add(inv.getInvStack(i));
			}
			FlavorGroup prominent = FlavorGroup.forName(profile.getString("ProminentFlavor"));
			CompoundTag ingredients = profile.getCompound("Seasonings");
			List<StatusEffectInstance> effects = PotionUtil.getCustomPotionEffects(meal);
			tag.remove("CustomPotionEffects");
			int saltCount = 0;
			if (profile.containsKey("Salt")) saltCount += profile.getInt("Salt");
			for (ItemStack seasoning : seasonings) {
				Item item = seasoning.getItem();
				if (item == EpicureanItems.SALT) saltCount++;
				else {
					StatusEffect effect = IngredientProfiles.DRESSINGS.get(item).getEffect();
					effects = MealRecipe.addEffect(effects, new StatusEffectInstance(effect, IngredientProfiles.EFFECT_TIMES.getOrDefault(effect, 200)));
				}
			}
			for (String ingredient : ingredients.getKeys()) {
				for (int i = 0; i < ingredients.getInt(ingredient); i++) {
					seasonings.add(new ItemStack(Registry.ITEM.get(new Identifier(ingredient))));
				}
			}
			effects = MealRecipe.addSalt(effects, saltCount);
			tag.remove("FlavorProfile");
			PotionUtil.setCustomPotionEffects(meal, effects);
			tag.put("FlavorProfile", MealRecipe.makeFlavorProfile(prominent, seasonings, 0, 0));
		}
		return meal;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return EpicureanRecipes.DRESSING_MEAL_SERIALIZER;
	}
}
