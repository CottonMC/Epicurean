package io.github.cottonmc.epicurean.mixins;

import io.github.cottonmc.epicurean.Epicurean;
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
	public void overhaulHunger(boolean alwaysEdible, CallbackInfoReturnable<Boolean> cir) {
		if (Epicurean.config.useSaturationOnly) cir.setReturnValue(true);
	}

	@Inject(method = "eatFood", at = @At("HEAD"))
	public void eatJelliedFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		PlayerEntity player = (PlayerEntity)(Object)this;
		if (stack.hasTag()) {
			if (stack.getTag().contains("jellied")) {
				player.getHungerManager().add(2, 0.25f);
			} else if (stack.getTag().contains("super_jellied")) {
				player.getHungerManager().add(4, 0.3f);
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 1200));
			}
		}
	}
}
