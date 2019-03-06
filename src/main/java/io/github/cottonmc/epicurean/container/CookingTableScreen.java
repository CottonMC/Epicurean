package io.github.cottonmc.epicurean.container;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.cottonmc.epicurean.EpicureanGastronomy;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.GuiEventListener;
import net.minecraft.client.gui.ingame.RecipeBookProvider;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.widget.RecipeBookButtonWidget;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.Identifier;

public class CookingTableScreen extends ContainerScreen /*implements RecipeBookProvider*/ {
	private static final Identifier TEXTURE = new Identifier(EpicureanGastronomy.MOD_ID, "textures/gui/cooking_table.png");
	private static final Identifier RECIPE_BUTTON_TEX = new Identifier("textures/gui/recipe_button.png");
	private final RecipeBookGui recipeBookGui = new RecipeBookGui();
	private boolean isNarrow;

	public CookingTableScreen(int syncId, PlayerEntity player) {
		super(new CookingTableContainer(syncId, player.inventory), player.inventory, new TranslatableTextComponent("container.epicurean.cooking_table"));
	}

	@Override
	protected void onInitialized() {
		super.onInitialized();
		this.containerWidth = 176;
		this.containerHeight = 166;
//		this.isNarrow = this.width < 379;
		//TODO: reimpl once the recipe book is easily extendable
//		this.recipeBookGui.initialize(this.width, this.height, this.client, this.isNarrow, (CookingTableContainer)this.container);
//		this.left = this.recipeBookGui.findLeftEdge(this.isNarrow, this.width, this.containerWidth);
//		this.listeners.add(this.recipeBookGui);
//		this.addButton(new RecipeBookButtonWidget(this.left + 144, this.top + 9, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEX) {
//			public void onPressed(double double_1, double double_2) {
//				CookingTableScreen.this.recipeBookGui.reset(CookingTableScreen.this.isNarrow);
//				CookingTableScreen.this.recipeBookGui.toggleOpen();
//				CookingTableScreen.this.left = CookingTableScreen.this.recipeBookGui.findLeftEdge(CookingTableScreen.this.isNarrow, CookingTableScreen.this.width, CookingTableScreen.this.containerWidth);
//				this.setPos(CookingTableScreen.this.left + 5, CookingTableScreen.this.height / 2 - 49);
//			}
//		});
	}

//	public GuiEventListener getFocused() {
//		return this.recipeBookGui;
//	}

	public void update() {
		super.update();
//		this.recipeBookGui.update();
	}

	@Override
	protected void drawBackground(float var1, int var2, int var3) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(TEXTURE);
		int guiX = (this.width - this.containerWidth) / 2;
		int guiY = (this.height - this.containerHeight) / 2;
		this.drawTexturedRect(guiX, guiY, 0, 0, this.containerWidth, this.containerHeight);
	}

	@Override
	public void method_18326(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground();
//		if (this.recipeBookGui.isOpen() && this.isNarrow) {
//			this.drawBackground(partialTicks, mouseX, mouseY);
//			this.recipeBookGui.method_18326(mouseX, mouseY, partialTicks);
//		} else {
//			this.recipeBookGui.method_18326(mouseX, mouseY, partialTicks);
			super.method_18326(mouseX, mouseY, partialTicks);
//			this.recipeBookGui.drawGhostSlots(this.left, this.top, true, partialTicks);
//		}

		this.drawMouseoverTooltip(mouseX, mouseY);
//		this.recipeBookGui.drawTooltip(this.left, this.top, mouseX, mouseY);
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

	@Override
	protected void onMouseClick(Slot slot, int x, int y, SlotActionType action) {
		super.onMouseClick(slot, x, y, action);
//		this.recipeBookGui.slotClicked(slot);
	}

//	public void refreshRecipeBook() {
//		this.recipeBookGui.refresh();
//	}

	public void onClosed() {
//		this.recipeBookGui.close();
		super.onClosed();
	}

//	@Override
//	public RecipeBookGui getRecipeBookGui() {
//		return this.recipeBookGui;
//	}
}
