package io.github.cottonmc.epicurean.mixins;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import squeek.appleskin.helpers.DynamicFood;

import javax.annotation.Nullable;

@Mixin(Item.class)
public abstract class MixinOmnivore implements DynamicFood {

	@Shadow @Nullable
	public abstract FoodComponent getFoodComponent();

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

	@Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
	public void getOmnivoreOnItemFinishedUsing(ItemStack stack, World world, LivingEntity entity, CallbackInfoReturnable cir) {
		if (stack.getItem() == Items.CAKE) {
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entity;
				player.getHungerManager().add(14, 2.8f);
				world.playSound(null, player.x, player.y, player.z, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.8F);
				player.increaseStat(Stats.EAT_CAKE_SLICE, 7);
				if (player instanceof ServerPlayerEntity) {
					Criterions.CONSUME_ITEM.handle((ServerPlayerEntity) player, stack);
				}
			}
			stack.decrement(1);
			cir.setReturnValue(stack);
		} else if (EpicureanGastronomy.config.omnivoreEnabled) {
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entity;
				player.getHungerManager().add(EpicureanGastronomy.config.omnivoreFoodRestore, EpicureanGastronomy.config.omnivoreSaturationRestore);
				world.playSound(null, player.x, player.y, player.z, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
				player.incrementStat(Stats.USED.getOrCreateStat((Item)(Object)this));
				if (player instanceof ServerPlayerEntity) {
					Criterions.CONSUME_ITEM.handle((ServerPlayerEntity) player, stack);
				}
			}
			if (stack.getItem() == Items.TNT || stack.getItem() == Items.TNT_MINECART) { // TNT/TNT minecart
				world.createExplosion(null, entity.x, entity.y+1, entity.z, 1.5f, Explosion.DestructionType.NONE);
			}

			double damageConf = EpicureanGastronomy.config.omnivoreItemDamage;
			int damage = (int)damageConf;
			if (damageConf < 1 && damageConf > 0) {
				damage = (int)Math.ceil(damageConf*stack.getItem().getMaxDamage());
			}
			if (stack.isDamageable() && damage > 0) stack.damage(damage, entity, (user) -> user.sendToolBreakStatus(entity.getActiveHand()));
			else stack.decrement(1);
			cir.setReturnValue(stack);
		}
	}

	@Override
	public int getDynamicHunger(ItemStack stack, PlayerEntity player) {
		if (stack.getItem().isFood()) {
			int base = this.getFoodComponent().getHunger();
			CompoundTag tag = stack.getOrCreateTag();
			if (tag.containsKey("jellied")) return base + 2;
			else if (tag.containsKey("super_jellied")) return base + 4;
			else return base;
		} else if (EpicureanGastronomy.config.omnivoreEnabled) {
			return EpicureanGastronomy.config.omnivoreFoodRestore;
		}
		return 0;
	}

	@Override
	public float getDynamicSaturation(ItemStack stack, PlayerEntity player) {
		if (stack.getItem().isFood()) {
			float base = this.getFoodComponent().getSaturationModifier();
			CompoundTag tag = stack.getOrCreateTag();
			if (tag.containsKey("jellied")) return base + 0.25f;
			else if (tag.containsKey("super_jellied")) return base + 0.3f;
			else return base;
		} else if (EpicureanGastronomy.config.omnivoreEnabled) {
			return EpicureanGastronomy.config.omnivoreSaturationRestore;
		}
		return 0f;
	}
}
