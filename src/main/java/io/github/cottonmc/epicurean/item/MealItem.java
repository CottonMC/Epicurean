package io.github.cottonmc.epicurean.item;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.NbtType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import squeek.appleskin.helpers.DynamicFood;

import java.util.Collections;
import java.util.List;

public class MealItem extends Item implements DynamicFood {

	public MealItem(int hunger, float saturation) {
		super(EpicureanItems.foodSettings(hunger, saturation));
	}

	@Override
	public boolean hasRecipeRemainder() {
		return false;
	}
	

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity entity) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)entity;
			for (StatusEffectInstance effect : getMealEffects(stack)) {
				player.addStatusEffect(effect);
			}
			if (!stack.hasTag()) return stack;
			if (stack.getTag().contains("FlavorProfile")) {
				CompoundTag profile = stack.getTag().getCompound("FlavorProfile");
				int addHunger = 0;
				float addSaturation = 0;
				if (profile.contains("Hunger")) addHunger = profile.getInt("Hunger");
				if (profile.contains("Saturation")) addSaturation = profile.getFloat("Saturation");
				player.getHungerManager().add(addHunger, addSaturation);
			}
		}
		super.finishUsing(stack, world, entity);
		return stack;
	}

	public static List<StatusEffectInstance> getMealEffects(ItemStack stack) {
		if (!stack.hasTag() || !stack.getTag().contains("CustomPotionEffects")) return Collections.singletonList(new StatusEffectInstance(StatusEffects.ABSORPTION, 200));
		return PotionUtil.getPotionEffects(stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext ctx) {
		super.appendTooltip(stack, world, tooltip, ctx);
		PotionUtil.buildTooltip(stack, tooltip, 1.0F);
		if (!stack.hasTag()) return;
		if (stack.getTag().contains("FlavorProfile")) {
			if (Screen.hasShiftDown()) {
				CompoundTag profile = stack.getTag().getCompound("FlavorProfile");
				String flavor = new TranslatableText("tooltip.epicurean.flavor." + profile.getString("ProminentFlavor").toLowerCase()).asString();
				tooltip.add(new TranslatableText("tooltip.epicurean.meal.flavor", flavor).formatted(Formatting.GRAY));
				int hunger = 0;
				float saturation = 0;
				if (profile.contains("Hunger")) hunger = profile.getInt("Hunger");
				if (profile.contains("Saturation")) saturation = profile.getFloat("Saturation");
				float percentage = Math.round(saturation*100.0);
				if ((hunger != 0 || saturation != 0) && !FabricLoader.getInstance().isModLoaded("appleskin")) {
					tooltip.add(new TranslatableText("tooltip.epicurean.meal.restores"));
					if (hunger != 0 && !EpicureanGastronomy.config.useSaturationOnly) {
						tooltip.add(new TranslatableText("tooltip.epicurean.meal.hunger", hunger).formatted(Formatting.GRAY));
					}
					if (saturation != 0) {
						tooltip.add(new TranslatableText("tooltip.epicurean.meal.saturation", percentage).formatted(Formatting.GRAY));
					}
				}
				if (profile.contains("Seasonings")) {
					tooltip.add(new TranslatableText("tooltip.epicurean.meal.seasonings"));
					CompoundTag seasonings = profile.getCompound("Seasonings");
					for (String key : seasonings.getKeys()) {
						String name = Registry.ITEM.get(new Identifier(key)).getTranslationKey();
						int amount = seasonings.getInt(key);
						TranslatableText translation = new TranslatableText(name);
						tooltip.add(new LiteralText(" - "+amount + " x " + translation.asString()).formatted(Formatting.GRAY));
					}
				}
			} else {
				tooltip.add(new TranslatableText("tooltip.epicurean.readmore").formatted(Formatting.GREEN));
			}
		}

	}

	@Override
	public int getDynamicHunger(ItemStack itemStack, PlayerEntity playerEntity) {
		int ret = this.getFoodComponent().getHunger();
		if (itemStack.getOrCreateTag().contains("FlavorProfile", NbtType.COMPOUND)) {
			if (itemStack.getSubTag("FlavorProfile").contains("Hunger", NbtType.INT)) {
				ret += itemStack.getSubTag("FlavorProfile").getInt("Hunger");
			}
		}
		return ret;
	}

	@Override
	public float getDynamicSaturation(ItemStack itemStack, PlayerEntity playerEntity) {
		float ret = this.getFoodComponent().getSaturationModifier();
		if (itemStack.getOrCreateTag().contains("FlavorProfile", NbtType.COMPOUND)) {
			if (itemStack.getSubTag("FlavorProfile").contains("Saturation", NbtType.FLOAT)) {
				ret += itemStack.getSubTag("FlavorProfile").getFloat("Saturation");
			}
		}
		return ret;
	}
}
