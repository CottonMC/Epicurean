package io.github.cottonmc.epicurean.util;

public class EpicureanConfMenu /*implements ModMenuApi*/ {

//	@Override
//	public String getModId() {
//		return EpicureanGastronomy.MOD_ID;
//	}
//
//	@Override
//	public Function<Screen, ? extends Screen> getConfigScreenFactory() {
//		return (prevScreen) -> {
//			ClothConfigScreen.Builder builder = new ClothConfigScreen.Builder(MinecraftClient.getInstance().currentScreen, "Epicurean Gastronomy Config", (config) -> ConfigManager.saveConfig(EpicureanGastronomy.config, "EpicureanGastronomy.json5"));
//			builder.addCategories("Primary Features", "Fun Features");
//			ConfigScreenBuilder.CategoryBuilder primaryFeatures = builder.getCategory("Primary Features");
//			primaryFeatures.addOption(new BooleanListEntry("Hopper Harvesting", EpicureanGastronomy.config.hopperHarvest, "text.cloth-config.reset_value", () -> false, (value) -> EpicureanGastronomy.config.hopperHarvest = value));
//			primaryFeatures.addOption(new BooleanListEntry("Saturation Only", EpicureanGastronomy.config.useSaturationOnly, "text.cloth-config.reset_value", () -> false, (value) -> EpicureanGastronomy.config.useSaturationOnly = value));
//			primaryFeatures.addOption(new IntegerListEntry("Natural Regen Speed", EpicureanGastronomy.config.naturalRegenSpeed, "text.cloth-config.reset_value", () -> 15, (value) -> EpicureanGastronomy.config.naturalRegenSpeed = value));
//			primaryFeatures.addOption(new DoubleListEntry("Seasoning Efficiency", EpicureanGastronomy.config.seasoningEfficiency, "text.cloth-config.reset_value", () -> 0.3d, (value) -> EpicureanGastronomy.config.seasoningEfficiency = value));
//			primaryFeatures.addOption(new BooleanListEntry("Any Salt in Meals", EpicureanGastronomy.config.useSaltTag, "text.cloth-config.reset_value", () -> false, (value) -> EpicureanGastronomy.config.useSaltTag = value));
//			ConfigScreenBuilder.CategoryBuilder funFeatures = builder.getCategory("Fun Features");
//			funFeatures.addOption(new BooleanListEntry("Edible Nuggets", EpicureanGastronomy.config.edibleNuggets, "text.cloth-config.reset_value", () -> false, (value) -> EpicureanGastronomy.config.edibleNuggets = value));
//			funFeatures.addOption(new BooleanListEntry("Omnivore Mode", EpicureanGastronomy.config.omnivoreEnabled, "text.cloth-config.reset_value", () -> false, (value) -> EpicureanGastronomy.config.omnivoreEnabled = value));
//			funFeatures.addOption(new IntegerSliderEntry("Omnivore Hunger", 0, 20, EpicureanGastronomy.config.omnivoreFoodRestore, "text.cloth-config.reset_value", () -> 2, (value) -> EpicureanGastronomy.config.omnivoreFoodRestore = value));
//			funFeatures.addOption(new FloatListEntry("Omnivore Saturation", EpicureanGastronomy.config.omnivoreSaturationRestore, "text.cloth-config.reset_value", () -> 0.25f, (value) -> EpicureanGastronomy.config.omnivoreSaturationRestore = value));
//			funFeatures.addOption(new DoubleListEntry("Omnivore Item Damage", EpicureanGastronomy.config.omnivoreItemDamage, "text.cloth-config.reset_value", () -> 30d, (value) -> EpicureanGastronomy.config.omnivoreItemDamage = value));
//			return builder.build();
//		};
//	}
}
