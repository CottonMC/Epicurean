package io.github.cottonmc.epicurean.container;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.cottonmc.epicurean.Epicurean;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class CookingTableScreen extends ContainerScreen<CookingTableContainer> /*implements RecipeBookProvider*/ {
	private static final Identifier TEXTURE = new Identifier(Epicurean.MOD_ID, "textures/gui/cooking_table.png");
	private static final Identifier RECIPE_BUTTON_TEX = new Identifier("textures/gui/recipe_button.png");
	private final CookingRecipeBookWidget recipeBookGui = new CookingRecipeBookWidget();
	private boolean isNarrow;

	public CookingTableScreen(int syncId, PlayerEntity player) {
		super(new CookingTableContainer(syncId, player.inventory), player.inventory, new TranslatableText("container.epicurean.cooking_table"));
	}

	@Override
	protected void init() {
		super.init();
		this.isNarrow = this.width < 379;
		//TODO: uncomment when there's a better way to make new recipe book groups
//		this.recipeBookGui.initialize(this.screenWidth, this.screenHeight, this.client, this.isNarrow, this.container);
//		this.x = this.recipeBookGui.findLeftEdge(this.isNarrow, this.screenWidth, this.width);
		this.x = (this.width - this.containerWidth) / 2;
//		this.listeners.add(this.recipeBookGui);
//		this.focusOn(this.recipeBookGui);
//		this.addButton(new RecipeBookButtonWidget(this.x + 144, this.y + 9, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEX, (buttonWidget_1) -> {
//			this.recipeBookGui.reset(this.isNarrow);
//			this.recipeBookGui.toggleOpen();
//			this.x = this.recipeBookGui.findLeftEdge(this.isNarrow, this.screenWidth, this.width);
//			((RecipeBookButtonWidget)buttonWidget_1).setPos(this.x + 144, this.y + 9);
//		}));
	}

//	public void update() {
//		super.update();
//		this.recipeBookGui.update();
//	}

	@Override
	protected void drawBackground(float var1, int var2, int var3) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(TEXTURE);
		int guiX = this.x;
		int guiY = (this.height - this.containerHeight) / 2;
		this.blit(guiX, guiY, 0, 0, this.containerWidth, this.containerHeight);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
//		if (this.recipeBookGui.isOpen() && this.isNarrow) {
			this.drawBackground(partialTicks, mouseX, mouseY);
//			this.recipeBookGui.render(mouseX, mouseY, partialTicks);
//		} else {
//			this.recipeBookGui.render(mouseX, mouseY, partialTicks);
			super.render(mouseX, mouseY, partialTicks);
//			this.recipeBookGui.drawGhostSlots(this.x, this.y, true, partialTicks);
//		}

		this.drawMouseoverTooltip(mouseX, mouseY);
//		this.recipeBookGui.drawTooltip(this.x, this.y, mouseX, mouseY);
//		this.focusOn(this.recipeBookGui);
	}

	@Override
	protected void drawMouseoverTooltip(int mouseX, int mouseY) {
		super.drawMouseoverTooltip(mouseX, mouseY);
		if (mouseX >= this.x + 26 && mouseY >= this.y+11
				&& mouseX <= this.x + 43 && mouseY <= this.y + 26) {
			renderTooltip(new LiteralText("tooltip.epicurean.table.base").asString(), mouseX, mouseY);
		}
		if (mouseX >= this.x + 94 && mouseY >= this.y+11
				&& mouseX <= this.x + 109 && mouseY <= this.y + 26) {
			renderTooltip(new LiteralText("tooltip.epicurean.table.seasoning").asString(), mouseX, mouseY);
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
