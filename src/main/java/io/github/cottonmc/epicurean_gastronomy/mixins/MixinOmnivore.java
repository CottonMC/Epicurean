package io.github.cottonmc.epicurean_gastronomy.mixins;

import io.github.cottonmc.epicurean_gastronomy.EpicureanGastronomy;
import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinOmnivore {

	@Inject(method = "getUseAction", at = @At("RETURN"), cancellable = true)
	public void getOmnivoreUseAction(ItemStack stack, CallbackInfoReturnable cir) {
		if (EpicureanGastronomy.config.omnivoreEnabled) cir.setReturnValue(UseAction.EAT);
	}

	@Inject(method = "getMaxUseTime", at = @At("RETURN"), cancellable = true)
	public void getOmnivoreUseTime(ItemStack stack, CallbackInfoReturnable cir) {
		if (EpicureanGastronomy.config.omnivoreEnabled) cir.setReturnValue(48);
	}

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	public void omnivoreUse(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable cir) {
		if (EpicureanGastronomy.config.omnivoreEnabled) {
			ItemStack stack = player.getStackInHand(hand);
			if (player.canConsume(EpicureanGastronomy.config.useSaturationOnly)) {
				player.setCurrentHand(hand);
				cir.setReturnValue(new TypedActionResult(ActionResult.SUCCESS, stack));
			} else {
				cir.setReturnValue(new TypedActionResult(ActionResult.FAIL, stack));
			}
		}
	}

	@Inject(method = "onItemFinishedUsing", at = @At("HEAD"), cancellable = true)
	public void getOmnivoreOnItemFinishedUsing(ItemStack stack, World world, LivingEntity entity, CallbackInfoReturnable cir) {
		if (stack.getItem() == Items.field_17534) { // cake
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entity;
				player.getHungerManager().add(14, 2.8f);
				world.playSound(null, player.x, player.y, player.z, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYER, 0.5F, world.random.nextFloat() * 0.1F + 0.8F);
				player.incrementStat(Stats.CUSTOM.getOrCreateStat(Stats.EAT_CAKE_SLICE), 7);
				if (player instanceof ServerPlayerEntity) {
					Criterions.CONSUME_ITEM.handle((ServerPlayerEntity) player, stack);
				}
			}
			stack.subtractAmount(1);
			cir.setReturnValue(stack);
		} else if (EpicureanGastronomy.config.omnivoreEnabled) {
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entity;
				player.getHungerManager().add(EpicureanGastronomy.config.omnivoreFoodRestore, EpicureanGastronomy.config.omnivoreSaturationRestore);
				world.playSound(null, player.x, player.y, player.z, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYER, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
				player.incrementStat(Stats.USED.getOrCreateStat((Item)(Object)this));
				if (player instanceof ServerPlayerEntity) {
					Criterions.CONSUME_ITEM.handle((ServerPlayerEntity) player, stack);
				}
			}
			if (stack.getItem() == Items.field_8626 || stack.getItem() == Items.TNT_MINECART) { // TNT/TNT minecart
				world.createExplosion(null, entity.x, entity.y+1, entity.z, 1.5f, false);
			}

			double damageConf = EpicureanGastronomy.config.omnivoreItemDamage;
			int damage = (int)damageConf;
			if (damageConf < 1 && damageConf > 0) {
				damage = (int)Math.ceil(damageConf*stack.getItem().getDurability());
			}
			if (stack.hasDurability() && damage > 0) stack.applyDamage(damage, entity);
			else stack.subtractAmount(1);
			cir.setReturnValue(stack);
		}
	}

}
