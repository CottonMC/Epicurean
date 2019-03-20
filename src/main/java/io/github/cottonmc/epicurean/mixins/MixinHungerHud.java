package io.github.cottonmc.epicurean.mixins;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(InGameHud.class)
public abstract class MixinHungerHud {

	@Shadow protected abstract PlayerEntity getCameraPlayer();

	@ModifyVariable(method = "renderStatusBars", at = @At(value = "STORE"), ordinal = 2)
	public int getSaturationIntLevel(int orig) {
		if (EpicureanGastronomy.config.useSaturationOnly && !FabricLoader.getInstance().isModLoaded("appleskin")) return (int)Math.floor(this.getCameraPlayer().getHungerManager().getSaturationLevel());
		else return orig;
	}

}
