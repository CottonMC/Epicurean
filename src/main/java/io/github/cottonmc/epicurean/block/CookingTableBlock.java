package io.github.cottonmc.epicurean.block;


import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CookingTableBlock extends Block {

	public CookingTableBlock() {
		super(FabricBlockSettings.of(Material.WOOD)
				.breakByTool(FabricToolTags.PICKAXES)
				.sounds(BlockSoundGroup.STONE)
				.strength(2.0f, 6.0f)
				.build());
	}

	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) return true;
		ContainerProviderRegistry.INSTANCE.openContainer(EpicureanBlocks.COOKING_CONTAINER, player, buf -> {
			buf.writeBlockPos(pos);
		});
//		player.increaseStat(new Identifier(EpicureanGastronomy.MOD_ID, "open_cooking_table"));
		return true;
	}

}
