package io.github.cottonmc.epicurean.block;

import io.github.cottonmc.epicurean.Epicurean;
import io.github.cottonmc.epicurean.container.CookingTableContainer;
import io.github.cottonmc.epicurean.item.EpicureanItems;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.container.BlockContext;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EpicureanBlocks {

	public static final Block COOKING_TABLE = register("cooking_table", new CookingTableBlock());

	public static final Identifier COOKING_CONTAINER = new Identifier(Epicurean.MOD_ID, "cooking");

	private static Block register(String name, Block block) {
		Registry.register(Registry.BLOCK, new Identifier(Epicurean.MOD_ID, name), block);
		BlockItem item = new BlockItem(block, EpicureanItems.defaultSettings());
		EpicureanItems.register(name, item);
		return block;
	}

	public static void init() {
		ContainerProviderRegistry.INSTANCE.registerFactory(COOKING_CONTAINER, (syncId, id, player, buf) -> new CookingTableContainer(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
	}
}
