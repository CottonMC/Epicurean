package io.github.cottonmc.epicurean.item;

import net.minecraft.ChatFormat;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.World;

import java.util.List;

public class SaltItem extends Item {
	public SaltItem(Settings settings) {
		super(settings);
	}

	@Override
	public void buildTooltip(ItemStack stack, World world, List<Component> tooltip, TooltipContext ctx) {
		super.buildTooltip(stack, world, tooltip, ctx);
		tooltip.add(new TranslatableComponent("tooltip.epicurean.dressing", "§f§osalt").applyFormat(ChatFormat.GRAY, ChatFormat.ITALIC));
	}
}
