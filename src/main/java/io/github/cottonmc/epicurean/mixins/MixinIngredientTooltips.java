package io.github.cottonmc.epicurean.mixins;

import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import net.minecraft.ChatFormat;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class MixinIngredientTooltips {

	@Inject(method = "buildTooltip", at = @At("HEAD"))
	public void addFlavorProfiles(ItemStack stack, World world, List<Component> tooltips, TooltipContext ctx, CallbackInfo ci) {
		if (IngredientProfiles.MEAL_INGREDIENTS.containsKey(stack.getItem())) {
			String flavor = new TranslatableComponent("tooltip.epicurean.flavor." + IngredientProfiles.MEAL_INGREDIENTS.get(stack.getItem()).asString()).getText();
			tooltips.add(new TranslatableComponent("tooltip.epicurean.ingredient", flavor).applyFormat(ChatFormat.GRAY, ChatFormat.ITALIC));
		} else if (IngredientProfiles.DRESSINGS.containsKey(stack.getItem())) {
			String flavor = new TranslatableComponent("tooltip.epicurean.flavor." + IngredientProfiles.DRESSINGS.get(stack.getItem()).asString()).getText();
			tooltips.add(new TranslatableComponent("tooltip.epicurean.dressing", flavor).applyFormat(ChatFormat.GRAY, ChatFormat.ITALIC));
		}
		if (stack.getItem().isFood()) {
			if (stack.hasTag())  {
				if (stack.getTag().containsKey("jellied")) {
					tooltips.add(new TranslatableComponent("tooltip.epicurean.jellied").applyFormat(ChatFormat.DARK_RED));
				} else if (stack.getTag().containsKey("super_jellied")) {
					tooltips.add(new TranslatableComponent("tooltip.epicurean.super_jellied").applyFormat(ChatFormat.GREEN));
				}
			}
		}
	}
}
