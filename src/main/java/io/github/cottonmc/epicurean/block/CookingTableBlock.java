package io.github.cottonmc.epicurean.block;


import io.github.cottonmc.epicurean.container.CookingScreenHandlerFactory;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CookingTableBlock extends Block {

	public CookingTableBlock() {
		super(FabricBlockSettings.of(Material.WOOD)
				.breakByTool(FabricToolTags.PICKAXES)
				.sounds(BlockSoundGroup.STONE)
				.strength(2.0f, 6.0f));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) return ActionResult.SUCCESS;
		NamedScreenHandlerFactory factory = new CookingScreenHandlerFactory();
		player.openHandledScreen(factory);
//		player.increaseStat(new Identifier(EpicureanGastronomy.MOD_ID, "open_cooking_table"));
		return ActionResult.SUCCESS;
	}

}
