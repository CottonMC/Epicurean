package io.github.cottonmc.epicurean;

import io.github.cottonmc.repackage.blue.endless.jankson.Jankson;
import io.github.cottonmc.repackage.blue.endless.jankson.JsonElement;
import io.github.cottonmc.repackage.blue.endless.jankson.JsonObject;
import io.github.cottonmc.repackage.blue.endless.jankson.impl.SyntaxError;
import me.shedaniel.cloth.api.ConfigScreenBuilder;
import me.shedaniel.cloth.gui.ClothConfigScreen;
import me.shedaniel.cloth.gui.entries.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class ConfigManager {
	private static String configName = "EpicureanGastronomy";
	public static <T> T load(Class<T> clazz){
		EpicureanGastronomy.LOGGER.info("Loading config!");
		try {
			File file = new File(FabricLoader.getInstance().getConfigDirectory().toString() + "/" + configName + ".json5");
			File oldConfig = new File(FabricLoader.getInstance().getConfigDirectory().toString() + "/Edibles.conf");
			Jankson jankson = Jankson.builder().build();

			//Carry old Edibles config over to new mod Id
			if (oldConfig.exists() && !file.exists()) {
				EpicureanGastronomy.LOGGER.info("Updating Edibles config!");
				try {
					JsonObject json = jankson.load(oldConfig);

					save(clazz.newInstance());
					T object = jankson.fromJson(json, clazz);
					FileOutputStream out = new FileOutputStream(file, false);
					String result = jankson
							.toJson(object)
							.toJson(true, true, 0);
					out.write(result.getBytes());
					out.flush();
					out.close();
					oldConfig.delete();
				}
				catch (IOException e) {
					EpicureanGastronomy.LOGGER.warn("Failed to upgrade config file: ", e);
				}
			}

			//Generate config file if it doesn't exist
			if(!file.exists()) {
				save(clazz.newInstance());
			}

			try {
				JsonObject json = jankson.load(file);
				T result = jankson.fromJson(json, clazz);

				//check if the config file is outdate. If so add new values
				JsonElement jsonElementNew = jankson.toJson(clazz.newInstance());
				if(jsonElementNew instanceof JsonObject){
					JsonObject jsonNew = (JsonObject) jsonElementNew;
					if(json.getDelta(jsonNew).size()>= 0){
						save(result);
					}
				}
				return result;
			}
			catch (IOException e) {
				EpicureanGastronomy.LOGGER.warn("Failed to load config file: ", e);
			}
		} catch (SyntaxError | InstantiationException | IllegalAccessException e) {
			EpicureanGastronomy.LOGGER.warn("Failed to load config file: ", e);
		}
		EpicureanGastronomy.LOGGER.error("Config not loaded!");
		return null;
	}

	public static void save(Object obj) {
		File configFile = new File(FabricLoader.getInstance().getConfigDirectory().toString() + "/" + configName + ".json5");
		Jankson jankson = Jankson.builder().build();
		String result = jankson
				.toJson(obj)
				.toJson(true, true, 0);
		try {
			if(!configFile.exists()) configFile.createNewFile();
			FileOutputStream out = new FileOutputStream(configFile, false);

			out.write(result.getBytes());
			out.flush();
			out.close();

		} catch (IOException e) {
			EpicureanGastronomy.LOGGER.warn("Failed to save config file: ", e);
		}
	}

	public static void getConfigScreen() {
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
