package com.hugman.culinaire.objects.screen;

import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.objects.screen.handler.KettleScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class KettleScreen extends HandledScreen<KettleScreenHandler> {
	private static final Identifier TEXTURE = Culinaire.MOD_DATA.id("textures/gui/container/kettle.png");

	public KettleScreen(KettleScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {
		super.init();
		this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int brewTime = this.handler.getBrewTime();
		int fluidLevel = this.handler.getFluidLevel();
		int fluid = this.handler.getFluid();
		boolean isHot = this.handler.isHot();
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
		if(brewTime > 0) {
			int brewBarHeight = (int) (27.0F * (1.0F - (float) brewTime / 800.0F));
			if(brewBarHeight > 0) {
				this.drawTexture(matrices, i + 99, j + 17, 222, 0, 7, brewBarHeight);
			}
		}
		if(fluid == 0) {
			this.drawTexture(matrices, i + 65, j + 48, 176, 0, 46, 16);
		}
		else {
			if(fluidLevel > 0) {
				int teaColor;
				if(fluid == 2) {
					teaColor = this.handler.getTeaColor();
				}
				else {
					teaColor = 3694022;
				}
				float red = (float) (teaColor >> 16 & 255) / 255.0F;
				float green = (float) (teaColor >> 8 & 255) / 255.0F;
				float blue = (float) (teaColor & 255) / 255.0F;
				RenderSystem.setShaderColor(red, green, blue, 1.0F);
				int fluidHeight = (int) (12.0F * (float) fluidLevel / 3.0F) + 4;
				this.drawTexture(matrices, i + 65, j + 64 - fluidHeight, 176, 16, 46, fluidHeight);
			}
		}
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		if(isHot) {
			this.drawTexture(matrices, i + 76, j + 68, 176, 32, 24, 9);
		}
	}
}
