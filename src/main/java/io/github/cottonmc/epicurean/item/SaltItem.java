package io.github.cottonmc.epicurean.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class SaltItem extends Item {
	public SaltItem(Settings settings) {
		super(settings);
	}

	@Override
	public void buildTooltip(ItemStack stack, World world, List<TextComponent> tooltip, TooltipContext ctx) {
		super.buildTooltip(stack, world, tooltip, ctx);
		tooltip.add(new TranslatableTextComponent("tooltip.epicurean.dressing", "§f§osalt").applyFormat(TextFormat.GRAY, TextFormat.ITALIC));
	}
}
