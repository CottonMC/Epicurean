package io.github.cottonmc.epicurean;

import io.github.cottonmc.epicurean.container.CookingTableScreen;
import io.github.cottonmc.epicurean.block.EpicureanBlocks;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.cloth.api.ConfigScreenBuilder;
import me.shedaniel.cloth.gui.ClothConfigScreen;
import me.shedaniel.cloth.gui.entries.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class EpicureanClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenProviderRegistry.INSTANCE.registerFactory(EpicureanBlocks.COOKING_CONTAINER, (syncId, identifier, player, buf) -> new CookingTableScreen(syncId, player));
	}
}
