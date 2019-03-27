package io.github.cottonmc.epicurean.container;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.ingame.RecipeBookProvider;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.widget.RecipeBookButtonWidget;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.Identifier;

public class CookingTableScreen extends ContainerScreen<CookingTableContainer> /*implements RecipeBookProvider*/ {
	private static final Identifier TEXTURE = new Identifier(EpicureanGastronomy.MOD_ID, "textures/gui/cooking_table.png");
	private static final Identifier RECIPE_BUTTON_TEX = new Identifier("textures/gui/recipe_button.png");
	private final CookingRecipeBookScreen recipeBookGui = new CookingRecipeBookScreen();
	private boolean isNarrow;

	public CookingTableScreen(int syncId, PlayerEntity player) {
		super(new CookingTableContainer(syncId, player.inventory), player.inventory, new TranslatableTextComponent("container.epicurean.cooking_table"));
	}

	@Override
	protected void onInitialized() {
		super.onInitialized();
		this.isNarrow = this.screenWidth < 379;
		//TODO: uncomment when there's a better way to make new recipe book groups
//		this.recipeBookGui.initialize(this.screenWidth, this.screenHeight, this.client, this.isNarrow, this.container);
		this.left = this.recipeBookGui.findLeftEdge(this.isNarrow, this.screenWidth, this.width);
//		this.listeners.add(this.recipeBookGui);
//		this.focusOn(this.recipeBookGui);
//		this.addButton(new RecipeBookButtonWidget(this.left + 144, this.top + 9, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEX, (buttonWidget_1) -> {
//			this.recipeBookGui.reset(this.isNarrow);
//			this.recipeBookGui.toggleOpen();
//			this.left = this.recipeBookGui.findLeftEdge(this.isNarrow, this.screenWidth, this.width);
//			((RecipeBookButtonWidget)buttonWidget_1).setPos(this.left + 144, this.top + 9);
//		}));
	}

//	public void update() {
//		super.update();
//		this.recipeBookGui.update();
//	}

	@Override
	protected void drawBackground(float var1, int var2, int var3) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(TEXTURE);
		int guiX = this.left;
		int guiY = (this.screenHeight - this.height) / 2;
		this.drawTexturedRect(guiX, guiY, 0, 0, this.width, this.height);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground();
//		if (this.recipeBookGui.isOpen() && this.isNarrow) {
			this.drawBackground(partialTicks, mouseX, mouseY);
//			this.recipeBookGui.render(mouseX, mouseY, partialTicks);
//		} else {
//			this.recipeBookGui.render(mouseX, mouseY, partialTicks);
			super.render(mouseX, mouseY, partialTicks);
//			this.recipeBookGui.drawGhostSlots(this.left, this.top, true, partialTicks);
//		}

		this.drawMouseoverTooltip(mouseX, mouseY);
//		this.recipeBookGui.drawTooltip(this.left, this.top, mouseX, mouseY);
//		this.focusOn(this.recipeBookGui);
	}

	@Override
	protected void drawMouseoverTooltip(int mouseX, int mouseY) {
		super.drawMouseoverTooltip(mouseX, mouseY);
		if (mouseX >= this.left + 26 && mouseY >= this.top+11
				&& mouseX <= this.left + 43 && mouseY <= this.top + 26) {
			drawTooltip(new TranslatableTextComponent("tooltip.epicurean.table.base").getText(), mouseX, mouseY);
		}
		if (mouseX >= this.left + 94 && mouseY >= this.top+11
				&& mouseX <= this.left + 109 && mouseY <= this.top + 26) {
			drawTooltip(new TranslatableTextComponent("tooltip.epicurean.table.seasoning").getText(), mouseX, mouseY);
		}
	}

//	@Override
//	protected void onMouseClick(Slot slot, int x, int y, SlotActionType action) {
//		super.onMouseClick(slot, x, y, action);
//		this.recipeBookGui.slotClicked(slot);
//	}

//	public void refreshRecipeBook() {
//		this.recipeBookGui.refresh();
//	}

//	public void onClosed() {
//		this.recipeBookGui.close();
//		super.onClosed();
//	}

//	@Override
//	public RecipeBookGui getRecipeBookGui() {
//		return this.recipeBookGui;
//	}
}
