package io.github.cottonmc.epicurean.mixins;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEating extends LivingEntity {

	protected MixinPlayerEating(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "canConsume", at = @At("HEAD"), cancellable = true)
	public void overhaulHunger(boolean alwaysEdible, CallbackInfoReturnable cir) {
		if (EpicureanGastronomy.config.useSaturationOnly) cir.setReturnValue(true);
	}

	@Inject(method = "method_18866", at = @At("HEAD"))
	public void eatJelliedFood(World world, ItemStack stack, CallbackInfoReturnable cir) {
		PlayerEntity player = (PlayerEntity)(Object)this;
		if (stack.hasTag()) {
			if (stack.getTag().containsKey("jellied")) {
				player.getHungerManager().add(2, 0.25f);
			} else if (stack.getTag().containsKey("super_jellied")) {
				player.getHungerManager().add(4, 0.3f);
				player.addPotionEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 1200));
			}
		}
	}
}
