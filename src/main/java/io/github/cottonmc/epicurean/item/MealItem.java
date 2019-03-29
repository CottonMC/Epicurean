package io.github.cottonmc.epicurean.item;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItemSetting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class MealItem extends Item {

	public MealItem(int hunger, float saturation) {
		super(EpicureanItems.DEFAULT_SETTINGS.food(new FoodItemSetting.Builder().hunger(hunger).saturationModifier(saturation).build()));
	}

	@Override
	public ItemStack onItemFinishedUsing(ItemStack stack, World world, LivingEntity entity) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)entity;
			for (StatusEffectInstance effect : getMealEffects(stack)) {
				player.addPotionEffect(effect);
			}
			if (!stack.hasTag()) return stack;
			if (stack.getTag().containsKey("FlavorProfile")) {
				CompoundTag profile = stack.getTag().getCompound("FlavorProfile");
				int addHunger = 0;
				float addSaturation = 0;
				if (profile.containsKey("Hunger")) addHunger = profile.getInt("Hunger");
				if (profile.containsKey("Saturation")) addSaturation = profile.getFloat("Saturation");
				player.getHungerManager().add(addHunger, addSaturation);
			}
		}
		super.onItemFinishedUsing(stack, world, entity);
		return stack;
	}

	public static List<StatusEffectInstance> getMealEffects(ItemStack stack) {
		if (!stack.hasTag() || !stack.getTag().containsKey("CustomPotionEffects")) return Collections.singletonList(new StatusEffectInstance(StatusEffects.ABSORPTION, 200));
		return PotionUtil.getPotionEffects(stack);
	}

	@Override
	public void buildTooltip(ItemStack stack, World world, List<TextComponent> tooltip, TooltipContext ctx) {
		super.buildTooltip(stack, world, tooltip, ctx);
		PotionUtil.buildTooltip(stack, tooltip, 1.0F);
		if (!stack.hasTag()) return;
		if (stack.getTag().containsKey("FlavorProfile")) {
			if (Screen.hasShiftDown()) {
				CompoundTag profile = stack.getTag().getCompound("FlavorProfile");
				String flavor = new TranslatableTextComponent("tooltip.epicurean.flavor." + profile.getString("ProminentFlavor").toLowerCase()).getText();
				tooltip.add(new TranslatableTextComponent("tooltip.epicurean.meal.flavor", flavor).applyFormat(TextFormat.GRAY));
				int hunger = 0;
				float saturation = 0;
				if (profile.containsKey("Hunger")) hunger = profile.getInt("Hunger");
				if (profile.containsKey("Saturation")) saturation = profile.getFloat("Saturation");
				float percentage = Math.round(saturation*100.0);
				if (hunger != 0 || saturation != 0) {
					tooltip.add(new TranslatableTextComponent("tooltip.epicurean.meal.restores"));
					if (hunger != 0 && !EpicureanGastronomy.config.useSaturationOnly) {
						tooltip.add(new TranslatableTextComponent("tooltip.epicurean.meal.hunger", hunger).applyFormat(TextFormat.GRAY));
					}
					if (saturation != 0) {
						tooltip.add(new TranslatableTextComponent("tooltip.epicurean.meal.saturation", percentage).applyFormat(TextFormat.GRAY));
					}
				}
				if (profile.containsKey("Seasonings")) {
					tooltip.add(new TranslatableTextComponent("tooltip.epicurean.meal.seasonings"));
					CompoundTag seasonings = profile.getCompound("Seasonings");
					for (String key : seasonings.getKeys()) {
						String name = Registry.ITEM.get(new Identifier(key)).getTranslationKey();
						int amount = seasonings.getInt(key);
						TranslatableTextComponent translation = new TranslatableTextComponent(name);
						tooltip.add(new StringTextComponent(" - "+amount + " x " + translation.getText()).applyFormat(TextFormat.GRAY));
					}
				}
			} else {
				tooltip.add(new TranslatableTextComponent("tooltip.epicurean.readmore").applyFormat(TextFormat.GREEN));
			}
		}

	}
}
