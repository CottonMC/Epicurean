package io.github.cottonmc.epicurean;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.impl.SyntaxError;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConfigManager {
	private static String configName = "EpicureanGastronomy";
	public static <T> T load(Class<T> clazz){
		EpicureanGastronomy.LOGGER.info("Loading config!");
		try {
			File file = new File(FabricLoader.getInstance().getConfigDirectory().toString() + "/" + configName + ".conf");
			File oldConfig = new File(FabricLoader.getInstance().getConfigDirectory().toString() + "/Edibles.conf");
			Jankson jankson = Jankson.builder().build();

			//Carry old Edibles config over to new mod Id
			if (oldConfig.exists() && !file.exists()) {
				try {
					JsonObject json = jankson.load(oldConfig);

					saveDefault(clazz.newInstance());
					T object = jankson.fromJson(json, clazz);
					FileOutputStream out = new FileOutputStream(file, false);
					String result = jankson
							.toJson(object)
							.toJson(true, true, 0);
					out.write(result.getBytes());
					out.flush();
					out.close();
				}
				catch (IOException e) {
					EpicureanGastronomy.LOGGER.warn("Failed to upgrade config file: ", e);
				}
			}

			//Generate config file if it doesn't exist
			if(!file.exists()) {
				saveDefault(clazz.newInstance());
			}

			try {
				JsonObject json = jankson.load(file);

				return jankson.fromJson(json, clazz);
			}
			catch (IOException e) {
				EpicureanGastronomy.LOGGER.warn("Failed to load config file: ", e);
			}
		} catch (SyntaxError | InstantiationException | IllegalAccessException e) {
			EpicureanGastronomy.LOGGER.warn("Failed to load config file: ", e);
		}
		return null;
	}

	public static void saveDefault(Object obj) {
		File configFile = new File(FabricLoader.getInstance().getConfigDirectory().toString() + "/" + configName + ".conf");
		Jankson jankson = Jankson.builder().build();
		String result = jankson
				.toJson(obj) //The first call makes a JsonObject
				.toJson(true, true, 0);     //The second turns the JsonObject into a String -
		//in this case, preserving comments and pretty-printing with newlines
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
}
