package io.github.cottonmc.epicurean.mixins;

import io.github.cottonmc.epicurean.ConfigManager;
import io.github.cottonmc.epicurean.EpicureanGastronomy;
import io.github.cottonmc.epicurean.EpicureanConfig;
import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(FoodItem.class)
public class MixinJelliedFood extends Item {
	@Shadow private boolean alwaysConsumable;

	public MixinJelliedFood(Settings settings) {
		super(settings);
	}

	@Inject(method = "onItemFinishedUsing",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getHungerManager()Lnet/minecraft/entity/player/HungerManager;"))
	public void eatJelliedFood(ItemStack stack, World world, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
		if (stack.hasTag()) {
			PlayerEntity player = (PlayerEntity)entity;
			if (stack.getTag().containsKey("jellied")) {
				player.getHungerManager().add(2, 0.5f);
			} else if (stack.getTag().containsKey("super_jellied")) {
				player.getHungerManager().add(4, 0.6f);
				player.addPotionEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 1200, 1));
			}
		}
	}

	@Override
	public void buildTooltip(ItemStack stack, World world, List<TextComponent> tooltips, TooltipContext ctx) {
		if (stack.hasTag())  {
			if (stack.getTag().containsKey("jellied")) {
				tooltips.add(new TranslatableTextComponent("tooltip.epicurean.jellied").applyFormat(TextFormat.DARK_RED));
			} else if (stack.getTag().containsKey("super_jellied")) {
				tooltips.add(new TranslatableTextComponent("tooltip.epicurean.super_jellied").applyFormat(TextFormat.GREEN));
			}
		}
		super.buildTooltip(stack, world, tooltips, ctx);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	public void overhaulHunger(int hunger, float saturation, boolean wolfFood, Item.Settings settings, CallbackInfo ci) {
		if (EpicureanGastronomy.config == null) EpicureanGastronomy.config = ConfigManager.load(EpicureanConfig.class);
		else {
			if (EpicureanGastronomy.config.useSaturationOnly) ((FoodItem) (Object) this).setAlwaysConsumable();
		}
	}

}
