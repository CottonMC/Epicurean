package io.github.cottonmc.epicurean;

import io.github.cottonmc.repackage.blue.endless.jankson.Jankson;
import io.github.cottonmc.repackage.blue.endless.jankson.JsonElement;
import io.github.cottonmc.repackage.blue.endless.jankson.JsonObject;
import io.github.cottonmc.repackage.blue.endless.jankson.impl.SyntaxError;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
}
