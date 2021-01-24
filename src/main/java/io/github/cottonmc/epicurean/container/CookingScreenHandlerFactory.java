package io.github.cottonmc.epicurean.container;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class CookingScreenHandlerFactory implements NamedScreenHandlerFactory {
	@Override
	public Text getDisplayName() {
		return new TranslatableText("screen.epicurean.cooking");
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new CookingTableContainer(syncId, inv);
	}
}
