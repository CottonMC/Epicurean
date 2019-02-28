package io.github.cottonmc.epicurean.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class DeprecatedItem extends Item {
	public DeprecatedItem() {
		super(new Item.Settings());
	}

	@Override
	public void buildTooltip(ItemStack stack, World world, List<TextComponent> tooltips, TooltipContext ctx) {
		tooltips.add(new TranslatableTextComponent("tooltip.epicurean.deprecated.0").applyFormat(TextFormat.RED));
		tooltips.add(new TranslatableTextComponent("tooltip.epicurean.deprecated.1").applyFormat(TextFormat.GRAY));
	}
}
