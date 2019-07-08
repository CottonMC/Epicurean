package io.github.cottonmc.epicurean.container;

import io.github.cottonmc.epicurean.block.EpicureanBlocks;
import io.github.cottonmc.epicurean.recipe.EpicureanRecipes;
import io.github.cottonmc.epicurean.recipe.MealRecipe;
import net.minecraft.client.network.packet.GuiSlotUpdateS2CPacket;
import net.minecraft.container.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

public class CookingTableContainer extends CraftingContainer<CookingInventory> {
	private final CookingInventory cookingInv;
	private final CraftingResultInventory resultInv;
	private final BlockContext context;
	private final PlayerEntity player;

	public static final int RESULT_SLOT = 0;
	public static final int FIRST_PLAYER_SLOT = 13;
	public static final int FIRST_HOTBAR_SLOT = 40;
	public static final int SLOT_COUNT = 49;

	public CookingTableContainer(int syncId, PlayerInventory playerInv) {
		this(syncId, playerInv, BlockContext.create(playerInv.player.world, playerInv.player.getBlockPos()));
	}

	public CookingTableContainer(int syncId, PlayerInventory playerInv, BlockContext ctx) {
		super(null, syncId);
		this.cookingInv = new CookingInventory(this, playerInv.player);
		this.resultInv = new CraftingResultInventory();
		this.context = ctx;
		this.player = playerInv.player;
		this.addSlot(new CookingResultSlot(playerInv.player, this.cookingInv, this.resultInv, 0, 146, 40));

		//base slots
		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlot(new Slot(this.cookingInv, j + i * 3, 10 + j * 18, 31 + i * 18));
			}
		}

		//seasoning slots
		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlot(new Slot(this.cookingInv, 6 + j + i * 3, 76 + j * 18, 31 + i * 18));
			}
		}

		//player inventory
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		//player hotbar
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void populateRecipeFinder(RecipeFinder finder) {
		this.cookingInv.provideRecipeInputs(finder);
	}

	@Override
	public void clearCraftingSlots() {
		this.cookingInv.clear();
		this.resultInv.clear();
	}

	@Override
	public boolean matches(Recipe<? super CookingInventory> recipe) {
		return recipe.matches(this.cookingInv, this.player.world);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return 0;
	}

	@Override
	public int getCraftingWidth() {
		return this.cookingInv.getWidth();
	}

	@Override
	public int getCraftingHeight() {
		return this.cookingInv.getHeight();
	}

	@Override
	public int getCraftingSlotCount() {
		return 13;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return canUse(this.context, player, EpicureanBlocks.COOKING_TABLE);
	}

	public void onContentChanged(Inventory inv) {
		this.context.run((world, pos) -> {
			syncCraft(this.syncId, world, this.player, this.cookingInv, this.resultInv);
		});
	}

	public void close(PlayerEntity player) {
		super.close(player);
		this.context.run((world, pos) -> {
			this.dropInventory(player, world, this.cookingInv);
		});
	}

	protected static void syncCraft(int syncId, World world, PlayerEntity player, CookingInventory cookingInv, CraftingResultInventory resultInv) {
		if (!world.isClient) {
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
			ItemStack stack = ItemStack.EMPTY;
			Optional<MealRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(EpicureanRecipes.MEAL, cookingInv, world);
			if (optional.isPresent()) {
				MealRecipe recipe = optional.get();
				if (resultInv.shouldCraftRecipe(world, serverPlayer, recipe)) {
					stack = recipe.craft(cookingInv);
					stack.setCount(1);
				}
			}

			resultInv.setInvStack(0, stack);
			serverPlayer.networkHandler.sendPacket(new GuiSlotUpdateS2CPacket(syncId, 0, stack));
		}
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int slotNum) {
		ItemStack stackToTransfer = ItemStack.EMPTY;
		Slot slot = this.slotList.get(slotNum);
		if (slot != null && slot.hasStack()) {
			ItemStack stackInSlot = slot.getStack();
			stackToTransfer = stackInSlot.copy();
			if (slotNum == RESULT_SLOT) {
				this.context.run((world, pos) -> {
					stackInSlot.getItem().onCraft(stackInSlot, world, player);
				});
				if (!this.insertItem(stackInSlot, FIRST_PLAYER_SLOT, SLOT_COUNT, true)) {
					return ItemStack.EMPTY;
				}

				slot.onStackChanged(stackInSlot, stackToTransfer);
			} else if (slotNum >= FIRST_PLAYER_SLOT && slotNum < FIRST_HOTBAR_SLOT) {
				if (!this.insertItem(stackInSlot, FIRST_HOTBAR_SLOT, SLOT_COUNT, false)) {
					return ItemStack.EMPTY;
				}
			} else if (slotNum >= FIRST_HOTBAR_SLOT && slotNum < SLOT_COUNT) {
				if (!this.insertItem(stackInSlot, FIRST_PLAYER_SLOT, FIRST_HOTBAR_SLOT, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(stackInSlot, FIRST_PLAYER_SLOT, SLOT_COUNT, false)) {
				return ItemStack.EMPTY;
			}

			if (stackInSlot.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}

			if (stackInSlot.getCount() == stackToTransfer.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack slotToTake = slot.onTakeItem(player, stackInSlot);
			if (slotNum == RESULT_SLOT) {
				player.dropItem(slotToTake, false);
			}
		}

		return stackToTransfer;
	}

}
