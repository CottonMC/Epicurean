package io.github.cottonmc.epicurean.mixins;

import com.google.common.collect.Lists;
import io.github.cottonmc.epicurean.container.CookingTableContainer;
import net.minecraft.client.recipe.book.ClientRecipeBook;
import net.minecraft.client.recipe.book.RecipeBookGroup;
import net.minecraft.container.CraftingContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class MixinRecipeBookGroups {

	@Inject(method = "getGroupsForContainer", at = @At("HEAD"), cancellable = true)
	private static void getCookingTableGroups(CraftingContainer<?> container, CallbackInfoReturnable cir) {
		if (container instanceof CookingTableContainer) {
			cir.setReturnValue(Lists.newArrayList(RecipeBookGroup.SEARCH,RecipeBookGroup.MISC));
		}
	}
}
