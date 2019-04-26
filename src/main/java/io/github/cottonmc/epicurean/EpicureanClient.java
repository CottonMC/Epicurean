package io.github.cottonmc.epicurean;

import io.github.cottonmc.epicurean.container.CookingTableScreen;
import io.github.cottonmc.epicurean.block.EpicureanBlocks;
import me.shedaniel.cloth.api.ConfigScreenBuilder;
import me.shedaniel.cloth.gui.ClothConfigScreen;
import me.shedaniel.cloth.gui.entries.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.lang.reflect.Method;

public class EpicureanClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientSpriteRegistryCallback.EVENT.register(((spriteAtlasTexture, registry) -> registry.register(new Identifier(EpicureanGastronomy.MOD_ID, "gui/cooking_table"))));

		ScreenProviderRegistry.INSTANCE.registerFactory(EpicureanBlocks.COOKING_CONTAINER, (syncId, identifier, player, buf) -> new CookingTableScreen(syncId, player));
		if (FabricLoader.getInstance().isModLoaded("modmenu")) {
			try {
				Class<?> clazz = Class.forName("io.github.prospector.modmenu.api.ModMenuApi");
				Method method = clazz.getMethod("addConfigOverride", String.class, Runnable.class);
				method.invoke(null, "epicurean", (Runnable) () -> {
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
					MinecraftClient.getInstance().openScreen(builder.build());
				});
			} catch (Exception e) {
				EpicureanGastronomy.LOGGER.error("Failed to add config screen to Mod Menu", e);
			}
		}
	}
}
