package io.github.cottonmc.epicurean.item;

import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class SpecialFoodItem extends Item {

	public SpecialFoodItem(int hunger, float saturation, Item.Settings settings) {
		super(settings.food(new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation).build()));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (EpicureanGastronomy.config.edibleNuggets || EpicureanGastronomy.config.omnivoreEnabled) return super.use(world, player, hand);
		else return new TypedActionResult<>(ActionResult.PASS, stack);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return (EpicureanGastronomy.config.edibleNuggets || EpicureanGastronomy.config.omnivoreEnabled)? UseAction.EAT : UseAction.NONE;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity entity) {
		if (EpicureanGastronomy.config.edibleNuggets) return super.finishUsing(stack, world, entity);
		else if (EpicureanGastronomy.config.omnivoreEnabled) {
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entity;
				player.getHungerManager().add(EpicureanGastronomy.config.omnivoreFoodRestore, EpicureanGastronomy.config.omnivoreSaturationRestore);
				world.playSound(null, player.x, player.y, player.z, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
				player.incrementStat(Stats.USED.getOrCreateStat(this));
				if (player instanceof ServerPlayerEntity) {
					Criterions.CONSUME_ITEM.handle((ServerPlayerEntity) player, stack);
				}
			}
			stack.decrement(1);
		}
		return stack;
	}
}
