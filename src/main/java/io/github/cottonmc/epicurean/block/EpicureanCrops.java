package io.github.cottonmc.epicurean.block;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import io.github.cottonmc.epicurean.item.EpicureanItems;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EpicureanCrops {

	public static final Block TOMATO_PLANT = register("tomato", new PickableCropBlock(EpicureanItems.TOMATO, 1));
	public static final Block PEPPER_PLANT = register("pepper", new PickableCropBlock(EpicureanItems.PEPPER, 1));
	public static final Block ONION_PLANT = register("onion", new HarvestableCropBlock(EpicureanItems.ONION, 0));

	public static Block register(String name, Block block) {
		Registry.register(Registry.BLOCK, new Identifier(EpicureanGastronomy.MOD_ID, name), block);
		return block;
	}

	public static void init() {
	}
}
