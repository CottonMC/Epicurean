package io.github.cottonmc.epicurean;

import io.github.cottonmc.epicurean.container.CookingTableScreen;
import io.github.cottonmc.epicurean.block.EpicureanBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;

public class EpicureanClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenProviderRegistry.INSTANCE.registerFactory(EpicureanBlocks.COOKING_CONTAINER, (syncId, identifier, player, buf) -> new CookingTableScreen(syncId, player));
	}
}
