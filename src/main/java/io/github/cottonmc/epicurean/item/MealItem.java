package io.github.cottonmc.epicurean.item;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.NbtType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormat;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.potion.PotionUtil;
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

	@Environment(EnvType.CLIENT)
	@Override
	public void buildTooltip(ItemStack stack, World world, List<Component> tooltip, TooltipContext ctx) {
		super.buildTooltip(stack, world, tooltip, ctx);
		PotionUtil.buildTooltip(stack, tooltip, 1.0F);
		if (!stack.hasTag()) return;
		if (stack.getTag().containsKey("FlavorProfile")) {
			if (Screen.hasShiftDown()) {
				CompoundTag profile = stack.getTag().getCompound("FlavorProfile");
				String flavor = new TranslatableComponent("tooltip.epicurean.flavor." + profile.getString("ProminentFlavor").toLowerCase()).getText();
				tooltip.add(new TranslatableComponent("tooltip.epicurean.meal.flavor", flavor).applyFormat(ChatFormat.GRAY));
				int hunger = 0;
				float saturation = 0;
				if (profile.containsKey("Hunger")) hunger = profile.getInt("Hunger");
				if (profile.containsKey("Saturation")) saturation = profile.getFloat("Saturation");
				float percentage = Math.round(saturation*100.0);
				if ((hunger != 0 || saturation != 0) && !FabricLoader.getInstance().isModLoaded("appleskin")) {
					tooltip.add(new TranslatableComponent("tooltip.epicurean.meal.restores"));
					if (hunger != 0 && !EpicureanGastronomy.config.useSaturationOnly) {
						tooltip.add(new TranslatableComponent("tooltip.epicurean.meal.hunger", hunger).applyFormat(ChatFormat.GRAY));
					}
					if (saturation != 0) {
						tooltip.add(new TranslatableComponent("tooltip.epicurean.meal.saturation", percentage).applyFormat(ChatFormat.GRAY));
					}
				}
				if (profile.containsKey("Seasonings")) {
					tooltip.add(new TranslatableComponent("tooltip.epicurean.meal.seasonings"));
					CompoundTag seasonings = profile.getCompound("Seasonings");
					for (String key : seasonings.getKeys()) {
						String name = Registry.ITEM.get(new Identifier(key)).getTranslationKey();
						int amount = seasonings.getInt(key);
						TranslatableComponent translation = new TranslatableComponent(name);
						tooltip.add(new TextComponent(" - "+amount + " x " + translation.getText()).applyFormat(ChatFormat.GRAY));
					}
				}
			} else {
				tooltip.add(new TranslatableComponent("tooltip.epicurean.readmore").applyFormat(ChatFormat.GREEN));
			}
		}

	}

	@Override
	public int getDynamicHunger(ItemStack itemStack, PlayerEntity playerEntity) {
		int ret = this.getFoodSetting().getHunger();
		if (itemStack.getOrCreateTag().containsKey("FlavorProfile", NbtType.COMPOUND)) {
			if (itemStack.getSubCompoundTag("FlavorProfile").containsKey("Hunger", NbtType.INT)) {
				ret += itemStack.getSubCompoundTag("FlavorProfile").getInt("Hunger");
			}
		}
		return ret;
	}

	@Override
	public float getDynamicSaturation(ItemStack itemStack, PlayerEntity playerEntity) {
		float ret = this.getFoodSetting().getSaturationModifier();
		if (itemStack.getOrCreateTag().containsKey("FlavorProfile", NbtType.COMPOUND)) {
			if (itemStack.getSubCompoundTag("FlavorProfile").containsKey("Saturation", NbtType.FLOAT)) {
				ret += itemStack.getSubCompoundTag("FlavorProfile").getFloat("Saturation");
			}
		}
		return ret;
	}
}
