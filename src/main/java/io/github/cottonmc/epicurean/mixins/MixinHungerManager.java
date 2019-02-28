package io.github.cottonmc.epicurean.mixins;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class MixinHungerManager {

	@Shadow private int foodLevel;

	@Inject(method = "update", at = @At("TAIL"))
	public void updateOverhauledHunger(PlayerEntity entity, CallbackInfo ci) {
		if (EpicureanGastronomy.config.useSaturationOnly) this.foodLevel = 20;
	}

	@ModifyConstant(method = "update", constant = @Constant(intValue = 10))
	public int slowNaturalRegen(int orig) {
		return EpicureanGastronomy.config.naturalRegenSpeed;
	}

	@ModifyConstant(method = "update", constant = @Constant(floatValue = 6.0F))
	public float boostRegenExhaustion(float orig) {
		return 4.0f;
	}
}
