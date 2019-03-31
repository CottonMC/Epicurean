package io.github.cottonmc.epicurean.recipe;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import io.github.cottonmc.epicurean.container.CookingInventory;
import io.github.cottonmc.epicurean.item.EpicureanItems;
import io.github.cottonmc.epicurean.item.Seasoning;
import io.github.cottonmc.epicurean.meal.FlavorGroup;
import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.crafting.CraftingRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
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
	public RecipeType<?> getType() {
		return EpicureanRecipes.MEAL;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		if (!(inv instanceof CookingInventory)) return false;
		RecipeFinder finder = new RecipeFinder();
		int foundBases = 0;

		for (int i = 0; i < CookingInventory.SECTION_SIZE; i++) {
			ItemStack stack = inv.getInvStack(i);
			if (!stack.isEmpty()) {
				++foundBases;
				finder.addItem(stack);
			}
		}

		//make sure the seasonings fit on the meal
		for (int i = CookingInventory.SECTION_SIZE; i < inv.getInvSize(); i++) {
			ItemStack stack = inv.getInvStack(i);
			if (stack.isEmpty() || stack.getItem() == EpicureanItems.SALT) continue;
			boolean seasoningFound = false;
			for (Ingredient ing : seasonings) {
				if (ing.method_8093(stack)) seasoningFound = true;
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
		for (int i = 0; i < CookingInventory.SECTION_SIZE; i++) {
			if (!inv.getInvStack(i).isEmpty()) ingredients.add(inv.getInvStack(i));
		}
		List<ItemStack> seasonings = new ArrayList<>();
		for (int i = CookingInventory.SECTION_SIZE; i < inv.getInvSize(); i++) {
			if (!inv.getInvStack(i).isEmpty()) seasonings.add(inv.getInvStack(i));
		}
		List<StatusEffectInstance> effects = new ArrayList<>();
		for (ItemStack ingredient : ingredients) {
			Item item = ingredient.getItem();
			if (IngredientProfiles.MEAL_INGREDIENTS.containsKey(item)) {
				prominence = Math.max(prominence, IngredientProfiles.MEAL_INGREDIENTS.get(item).getImpact());
			}
		}
		for (ItemStack seasoning : seasonings) {
			Item item = seasoning.getItem();
			if (item instanceof Seasoning) {
				if (IngredientProfiles.MEAL_INGREDIENTS.containsKey(item))
					prominence = Math.max(prominence, IngredientProfiles.MEAL_INGREDIENTS.get(item).getImpact());
				if (((Seasoning) item).getBonusEffect(seasoning) != null)
					effects = addEffect(effects, ((Seasoning) item).getBonusEffect(seasoning));
			} else if (IngredientProfiles.MEAL_INGREDIENTS.containsKey(item)) {
				prominence = Math.max(prominence, IngredientProfiles.MEAL_INGREDIENTS.get(item).getImpact());
			} else if (IngredientProfiles.DRESSINGS.containsKey(item)) {
				StatusEffect effectToAdd = IngredientProfiles.DRESSINGS.get(item).getEffect();
				int timeToAdd = IngredientProfiles.EFFECT_TIMES.getOrDefault(effectToAdd, 1800);
				effects = addEffect(effects, new StatusEffectInstance(effectToAdd, timeToAdd));
			}
		}

		meal.getTag().put("FlavorProfile", makeFlavorProfile(FlavorGroup.forImpact(prominence), seasonings));
		StatusEffect effectToAdd = FlavorGroup.forImpact(prominence).getEffect();
		int timeToAdd = IngredientProfiles.EFFECT_TIMES.getOrDefault(effectToAdd, 1800);
		effects = addEffect(effects, new StatusEffectInstance(effectToAdd, timeToAdd));
		effects = addSalt(effects, countSalt(seasonings));
		PotionUtil.setCustomPotionEffects(meal, effects);
		return meal;
	}

	@Override
	public boolean fits(int x, int y) {
		return x * y <= CookingInventory.SECTION_SIZE * 2;
	}

	@Override
	public ItemStack getOutput() {
		return this.output;
	}

	@Override
	public Identifier getId() {
		return this.id;
	}

	public String getGroup() {
		return this.group;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return EpicureanRecipes.MEAL_SERIALIZER;
	}

	public static CompoundTag makeFlavorProfile(FlavorGroup group, List<ItemStack> seasonings) {
		CompoundTag tag = new CompoundTag();
		tag.putString("ProminentFlavor", group.toString());
		if (!seasonings.isEmpty()) {
			tag.put("Seasonings", makeIngredientList(seasonings));
			tag.putInt("Hunger", getHungerAmount(seasonings));
			tag.putFloat("Saturation", getSaturationAmount(seasonings));
			tag.putInt("Salt", countSalt(seasonings));
		}
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

	public static int getHungerAmount(List<ItemStack> ingredients) {
		int hunger = 0;
		int seasoningBonus = 0;
		for (ItemStack stack : ingredients) {
			if (stack.getItem().isFood()) {
				hunger += stack.getItem().getFoodSetting().getHunger();
			} else if (stack.getItem() instanceof Seasoning) {
				seasoningBonus += ((Seasoning) stack.getItem()).getHungerRestored(stack);
			}
		}
		hunger = (int) Math.ceil((hunger * EpicureanGastronomy.config.seasoningEfficiency) + seasoningBonus);
		return hunger;
	}

	public static float getSaturationAmount(List<ItemStack> ingredients) {
		float saturation = 0;
		float seasoningBonus = 0;
		for (ItemStack stack : ingredients) {
			if (stack.getItem().isFood()) {
				saturation += stack.getItem().getFoodSetting().getSaturationModifier();
			} else if (stack.getItem() instanceof Seasoning) {
				seasoningBonus += ((Seasoning) stack.getItem()).getSaturationModifier(stack);
			}
		}
		return (float) ((saturation * EpicureanGastronomy.config.seasoningEfficiency) + seasoningBonus);
	}

	public static int countSalt(List<ItemStack> seasonings) {
		int salt = 0;
		for (ItemStack stack : seasonings) {
			if (stack.getItem() == EpicureanItems.SALT) salt++;
		}
		return salt;
	}

	public static List<StatusEffectInstance> addEffect(List<StatusEffectInstance> currentEffects, StatusEffectInstance effectToAdd) {
		StatusEffect effect = effectToAdd.getEffectType();
		boolean replaced = false;
		List<StatusEffectInstance> effects = new ArrayList<>(currentEffects);
		for (StatusEffectInstance inst : currentEffects) {
			if (inst.getEffectType() == effect) {
				StatusEffectInstance instCopy = new StatusEffectInstance(inst.getEffectType(), inst.getAmplifier(), inst.getDuration());
				effects.remove(inst);
				int newDuration = inst.getDuration() + (effectToAdd.getDuration() / 2);
				int newAmp = Math.max(inst.getAmplifier(), effectToAdd.getAmplifier());
				if (newDuration >= 2 * IngredientProfiles.EFFECT_TIMES.getOrDefault(inst.getEffectType(), 200)) {
					newAmp++;
					newDuration /= 2;
				}
				StatusEffectInstance newInst = new StatusEffectInstance(instCopy.getEffectType(), newDuration, newAmp);
				effects.add(newInst);
				replaced = true;
			}
		}
		if (!replaced) effects.add(effectToAdd);
		return effects;
	}

	public static List<StatusEffectInstance> addSalt(List<StatusEffectInstance> currentEffects, int saltCount) {
		List<StatusEffectInstance> effects = new ArrayList<>(currentEffects);
		int effectCount = effects.size();
		if (saltCount == 0) return currentEffects;
		else if (saltCount == effectCount + 1) {
			for (StatusEffectInstance effect : currentEffects) {
				effects = addEffect(effects, new StatusEffectInstance(effect.getEffectType(), effect.getDuration() / 2, effect.getAmplifier() + 1));
			}
		} else if (saltCount == effectCount) {
			for (StatusEffectInstance effect : currentEffects) {
				effects = addEffect(effects, new StatusEffectInstance(effect.getEffectType(), effect.getDuration() / 4));
			}
		} else if (saltCount < effectCount) {
			for (StatusEffectInstance effect : currentEffects) {
				effects = addEffect(effects, new StatusEffectInstance(effect.getEffectType(), effect.getDuration() / 6));
			}
		} else {
			effects.clear();
			for (StatusEffectInstance effect : currentEffects) {
				effects = addEffect(effects, new StatusEffectInstance(effect.getEffectType(), (3*effect.getDuration()) / 4));
			}
		}
		return effects;
	}
}
