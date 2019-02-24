package io.github.cottonmc.epicurean_gastronomy.mixins;

import io.github.cottonmc.epicurean_gastronomy.EpicureanGastronomy;
import net.minecraft.block.*;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sortme.Hopper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(HopperBlockEntity.class)
public class MixinHopperHarvesting {

	@Inject(method = "extract(Lnet/minecraft/sortme/Hopper;)Z", at = @At("HEAD"), cancellable = true)
	private static void hopperHarvest(Hopper hopper, CallbackInfoReturnable cir) {
		if (EpicureanGastronomy.config.hopperHarvest) {
			World world = hopper.getWorld();
			BlockPos pos = new BlockPos(hopper.getHopperX(), hopper.getHopperY(), hopper.getHopperZ());
			BlockState state = world.getBlockState(pos.offset(Direction.UP, 2));
			if (state.getBlock() instanceof CropBlock) {
				if (harvestCrop(world, pos, state)) cir.setReturnValue(true);
			} else if (state.getBlock() instanceof SweetBerryBushBlock) {
				if (harvestBerries(world, pos, state)) cir.setReturnValue(true);
			} else if (state.getBlock() instanceof GourdBlock) {
				if (harvestGourd(world, pos, state)) cir.setReturnValue(true);
			} else if (world.getBlockState(pos.offset(Direction.UP)).getBlock() instanceof CocoaBlock) {
				if (harvestCocoa(world, pos)) cir.setReturnValue(true);
			} else if (state.getBlock() instanceof NetherWartBlock) {
				//Apparently this isn't an instanceof CropBlock so I gotta do this manually. Thanks, Mojang.
				if (harvestNetherWart(world, pos, state)) cir.setReturnValue(true);
			}
		}
	}

	private static boolean harvestCrop(World world, BlockPos pos, BlockState state) {
		CropBlock crop = (CropBlock) state.getBlock();
		if (crop.getCropAgeMaximum() == state.get(crop.getAgeProperty())) {
			System.out.println("Collection:");
			Inventory inv = HopperBlockEntity.getInventoryAt(world, pos);
			List<ItemStack> results = state.getDroppedStacks(getLootContext(world, pos));
			List<ItemStack> dropped = deepCopy(results);
			List<ItemStack> remaining = attemptCollect(inv, results);
			if (!dropped.equals(results)) {
				if (remaining.size() > 0) {
					spawnResults(world, pos.offset(Direction.UP, 2), remaining);
				}
				world.setBlockState(pos.offset(Direction.UP, 2), state.with(crop.getAgeProperty(), 0));
				return true;
			}
		}
		return false;
	}

	private static boolean harvestBerries(World world, BlockPos pos, BlockState state) {
		Inventory inv = HopperBlockEntity.getInventoryAt(world, pos);
		int age = state.get(SweetBerryBushBlock.AGE);
		if (age == 3) {
			int berriesToDrop = 2 + world.random.nextInt(2);
			List<ItemStack> results = new ArrayList<>();
			results.add(new ItemStack(Items.SWEET_BERRIES, berriesToDrop));
			List<ItemStack> dropped = deepCopy(results);
			List<ItemStack> remaining = attemptCollect(inv, results);
			if (!dropped.equals(results)) {
				if (remaining.size() > 0) {
					spawnResults(world, pos.offset(Direction.UP, 2), remaining);
				}
				world.setBlockState(pos.offset(Direction.UP, 2), state.with(SweetBerryBushBlock.AGE, 1));
				return true;
			}
		}
		return false;
	}

	private static boolean harvestGourd(World world, BlockPos pos, BlockState state) {
		Inventory inv = HopperBlockEntity.getInventoryAt(world, pos);
		BlockPos gourdPos = pos.offset(Direction.UP, 2);
		List<ItemStack> results = state.getDroppedStacks(getLootContext(world, pos));
		List<ItemStack> dropped = deepCopy(results);
		List<ItemStack> remaining = attemptCollect(inv, results);
		if (!dropped.equals(results)) {
			if (remaining.size() > 0) {
				spawnResults(world, gourdPos, remaining);
			}
			world.breakBlock(gourdPos, false);
			return true;
		}
		return false;
	}

	private static boolean harvestCocoa(World world, BlockPos pos) {
		BlockPos cocoaPos = pos.offset(Direction.UP);
		BlockState cocoaState = world.getBlockState(cocoaPos);
		int age = cocoaState.get(CocoaBlock.AGE);
		if (age == 2) {
			Inventory inv = HopperBlockEntity.getInventoryAt(world, pos);
			List<ItemStack> results = cocoaState.getDroppedStacks(getLootContext(world, cocoaPos));
			List<ItemStack> dropped = deepCopy(results);
			List<ItemStack> remaining = attemptCollect(inv, results);
			if (!dropped.equals(results)) {
				if (remaining.size() > 0) {
					spawnResults(world, cocoaPos, remaining);
				}
				world.setBlockState(cocoaPos, cocoaState.with(CocoaBlock.AGE, 0));
				return true;
			}
		}
		return false;
	}

	private static boolean harvestNetherWart(World world, BlockPos pos, BlockState state) {
		if (state.get(NetherWartBlock.AGE) == 3) {
			Inventory inv = HopperBlockEntity.getInventoryAt(world, pos);
			List<ItemStack> results = state.getDroppedStacks(getLootContext(world, pos));
			List<ItemStack> dropped = deepCopy(results);
			List<ItemStack> remaining = attemptCollect(inv, results);
			if (!dropped.equals(results)) {
				if (remaining.size() > 0) {
					spawnResults(world, pos.offset(Direction.UP, 2), remaining);
				}
				world.setBlockState(pos.offset(Direction.UP, 2), state.with(NetherWartBlock.AGE, 0));
				return true;
			}
		}
		return false;
	}

	private static LootContext.Builder getLootContext(World world, BlockPos pos) {
		return new LootContext
				.Builder(world.getServer().getWorld(world.dimension.getType()))
				.put(LootContextParameters.POSITION, pos).put(LootContextParameters.TOOL, ItemStack.EMPTY);
	}

	private static List<ItemStack> attemptCollect(Inventory inv, List<ItemStack> results) {
		List<ItemStack> remaining = new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			ItemStack insert = HopperBlockEntity.transfer(null, inv, results.get(i), null);
			remaining.add(insert);
		}
		return remaining;
	}

	private static List<ItemStack> deepCopy(List<ItemStack> original) {
		List<ItemStack> copied = new ArrayList<>();
		for (ItemStack stack : original) {
			copied.add(stack.copy());
		}
		return copied;
	}

	private static void spawnResults(World world, BlockPos spawnAt, List<ItemStack> toSpawn) {
		for (int i = 0; i < toSpawn.size(); i++) {
			world.spawnEntity(new ItemEntity(world, spawnAt.getX(), spawnAt.getY(), spawnAt.getZ(), toSpawn.get(i)));
		}
	}
}
