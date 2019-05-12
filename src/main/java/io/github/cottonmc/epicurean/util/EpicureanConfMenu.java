package io.github.cottonmc.epicurean.util;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.cloth.api.ConfigScreenBuilder;
import me.shedaniel.cloth.gui.ClothConfigScreen;
import me.shedaniel.cloth.gui.entries.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;

import java.util.function.Function;

public class EpicureanConfMenu implements ModMenuApi {

	@Override
	public String getModId() {
		return EpicureanGastronomy.MOD_ID;
	}

	@Override
	public Function<Screen, ? extends Screen> getConfigScreenFactory() {
		return (prevScreen) -> {
			ClothConfigScreen.Builder builder = new ClothConfigScreen.Builder(MinecraftClient.getInstance().currentScreen, "Epicurean Gastronomy Config", null);
			builder.addCategories("Primary Features", "Fun Features");
			ConfigScreenBuilder.CategoryBuilder primaryFeatures = builder.getCategory("Primary Features");
			primaryFeatures.addOption(new BooleanListEntry("Hopper Harvesting", EpicureanGastronomy.config.hopperHarvest, "text.cloth-config.reset_value", () -> false, null));
			primaryFeatures.addOption(new BooleanListEntry("Saturation Only", EpicureanGastronomy.config.useSaturationOnly, "text.cloth-config.reset_value", () -> false, null));
			primaryFeatures.addOption(new IntegerListEntry("Natural Regen Speed", EpicureanGastronomy.config.naturalRegenSpeed, "text.cloth-config.reset_value", () -> 15, null));
			primaryFeatures.addOption(new DoubleListEntry("Seasoning Efficiency", EpicureanGastronomy.config.seasoningEfficiency, "text.cloth-config.reset_value", () -> 0.3d, null));
			ConfigScreenBuilder.CategoryBuilder funFeatures = builder.getCategory("Fun Features");
			funFeatures.addOption(new BooleanListEntry("Edible Nuggets", EpicureanGastronomy.config.edibleNuggets, "text.cloth-config.reset_value", () -> false, null));
			funFeatures.addOption(new BooleanListEntry("Omnivore Mode", EpicureanGastronomy.config.omnivoreEnabled, "text.cloth-config.reset_value", () -> false, null));
			funFeatures.addOption(new IntegerSliderEntry("Omnivore Hunger", 0, 20, EpicureanGastronomy.config.omnivoreFoodRestore, "text.cloth-config.reset_value", () -> 2, null));
			funFeatures.addOption(new FloatListEntry("Omnivore Saturation", EpicureanGastronomy.config.omnivoreSaturationRestore, "text.cloth-config.reset_value", () -> 0.25f, null));
			funFeatures.addOption(new DoubleListEntry("Omnivore Item Damage", EpicureanGastronomy.config.omnivoreItemDamage, "text.cloth-config.reset_value", () -> 30d, null));
			return builder.build();
		};
	}
}
