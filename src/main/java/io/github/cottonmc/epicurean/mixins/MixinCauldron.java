package io.github.cottonmc.epicurean.mixins;

import io.github.cottonmc.epicurean.item.EpicureanItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CauldronBlock.class)
public abstract class MixinCauldron extends Block {

	public MixinCauldron(Settings settings) {
		super(settings);
	}

	@Inject(method = "activate", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void harvestSalt(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result, CallbackInfoReturnable cir,
							ItemStack stack, int level) {
		Item item = stack.getItem();
		if (item == Items.NETHER_WART) {
			if (level > 0 && !world.isClient && world.getBlockState(pos.down()).getBlock() == Blocks.FIRE) {
				if (!player.abilities.creativeMode) {
					ItemStack salt = new ItemStack(EpicureanItems.SALT, 1);
					player.increaseStat(Stats.USE_CAULDRON);
					stack.subtractAmount(1);
					if (stack.isEmpty()) {
						player.setStackInHand(hand, salt);
					} else if (!player.inventory.insertStack(salt)) {
						player.dropItem(salt, false);
					}
				}
				world.playSound(null, pos, SoundEvents.BLOCK_NETHER_WART_BREAK, SoundCategory.BLOCK, 1.0f, 1.0f);
				((CauldronBlock)(Object)this).setLevel(world, pos, state, level - 1);
				cir.setReturnValue(true);
			}
		}
	}
}
