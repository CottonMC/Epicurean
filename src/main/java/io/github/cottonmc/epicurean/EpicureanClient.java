package io.github.cottonmc.epicurean;

import io.github.cottonmc.epicurean.block.crop.EpicureanCrops;
import io.github.cottonmc.epicurean.container.CookingTableScreen;
import io.github.cottonmc.epicurean.block.EpicureanBlocks;
import io.github.cottonmc.epicurean.meal.IngredientProfiles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class EpicureanClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenRegistry.register(EpicureanBlocks.COOKING_SCREEN, CookingTableScreen::new);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), EpicureanCrops.ONION_PLANT, EpicureanCrops.PEPPER_PLANT, EpicureanCrops.SOYBEAN_PLANT, EpicureanCrops.TOMATO_PLANT);

		ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltips) -> {
			if (IngredientProfiles.MEAL_INGREDIENTS.containsKey(stack.getItem())) {
				Text flavor = new TranslatableText("tooltip.epicurean.flavor." + IngredientProfiles.MEAL_INGREDIENTS.get(stack.getItem()).asString());
				tooltips.add(new TranslatableText("tooltip.epicurean.ingredient", flavor).formatted(Formatting.GRAY, Formatting.ITALIC));
			} else if (IngredientProfiles.DRESSINGS.containsKey(stack.getItem())) {
				Text flavor = new TranslatableText("tooltip.epicurean.flavor." + IngredientProfiles.DRESSINGS.get(stack.getItem()).asString());
				tooltips.add(new TranslatableText("tooltip.epicurean.dressing", flavor).formatted(Formatting.GRAY, Formatting.ITALIC));
			}
			if (stack.getItem().isFood()) {
				if (stack.hasTag())  {
					if (stack.getTag().contains("jellied")) {
						tooltips.add(new TranslatableText("tooltip.epicurean.jellied").formatted(Formatting.DARK_RED));
					} else if (stack.getTag().contains("super_jellied")) {
						tooltips.add(new TranslatableText("tooltip.epicurean.super_jellied").formatted(Formatting.GREEN));
					}
				}
			}
		});
	}
}
