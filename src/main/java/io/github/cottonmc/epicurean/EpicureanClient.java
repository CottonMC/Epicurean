package io.github.cottonmc.epicurean;

import io.github.cottonmc.epicurean.block.crop.EpicureanCrops;
import io.github.cottonmc.epicurean.container.CookingTableScreen;
import io.github.cottonmc.epicurean.block.EpicureanBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.render.RenderLayer;

public class EpicureanClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenProviderRegistry.INSTANCE.registerFactory(EpicureanBlocks.COOKING_CONTAINER, (syncId, identifier, player, buf) -> new CookingTableScreen(syncId, player));
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), EpicureanCrops.ONION_PLANT, EpicureanCrops.PEPPER_PLANT, EpicureanCrops.SOYBEAN_PLANT, EpicureanCrops.TOMATO_PLANT);
	}
}
