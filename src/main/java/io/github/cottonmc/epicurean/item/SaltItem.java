package io.github.cottonmc.epicurean.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class SaltItem extends Item {
	public SaltItem(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext ctx) {
		super.appendTooltip(stack, world, tooltip, ctx);
		tooltip.add(new TranslatableText("tooltip.epicurean.dressing", "§f§osalt").formatted(Formatting.GRAY, Formatting.ITALIC));
	}
}
