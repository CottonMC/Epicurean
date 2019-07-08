package io.github.cottonmc.epicurean.mixins;

import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class MixinIngredientTooltips {

	@Inject(method = "appendTooltip", at = @At("HEAD"))
	public void addFlavorProfiles(ItemStack stack, World world, List<Text> tooltips, TooltipContext ctx, CallbackInfo ci) {
		if (IngredientProfiles.MEAL_INGREDIENTS.containsKey(stack.getItem())) {
			String flavor = new TranslatableText("tooltip.epicurean.flavor." + IngredientProfiles.MEAL_INGREDIENTS.get(stack.getItem()).asString()).asString();
			tooltips.add(new TranslatableText("tooltip.epicurean.ingredient", flavor).formatted(Formatting.GRAY, Formatting.ITALIC));
		} else if (IngredientProfiles.DRESSINGS.containsKey(stack.getItem())) {
			String flavor = new TranslatableText("tooltip.epicurean.flavor." + IngredientProfiles.DRESSINGS.get(stack.getItem()).asString()).asString();
			tooltips.add(new TranslatableText("tooltip.epicurean.dressing", flavor).formatted(Formatting.GRAY, Formatting.ITALIC));
		}
		if (stack.getItem().isFood()) {
			if (stack.hasTag())  {
				if (stack.getTag().containsKey("jellied")) {
					tooltips.add(new TranslatableText("tooltip.epicurean.jellied").formatted(Formatting.DARK_RED));
				} else if (stack.getTag().containsKey("super_jellied")) {
					tooltips.add(new TranslatableText("tooltip.epicurean.super_jellied").formatted(Formatting.GREEN));
				}
			}
		}
	}
}
