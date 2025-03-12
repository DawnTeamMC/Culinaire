package fr.hugman.culinaire.client.screen;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.screen.KettleScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class KettleScreen extends HandledScreen<KettleScreenHandler> {
    private static final Identifier TEXTURE = Culinaire.id("textures/gui/container/kettle.png");
    private static final Identifier EMPTY_FLUID_TEXTURE = Culinaire.id("container/kettle/empty_fluid");
    private static final Identifier FLUID_TEXTURE = Culinaire.id("textures/gui/sprites/container/kettle/fluid.png");
    private static final Identifier BREW_PROGRESS_TEXTURE = Culinaire.id("container/kettle/brew_progress");
    private static final Identifier FIRE_TEXTURE = Culinaire.id("container/kettle/fire");

    public KettleScreen(KettleScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int brewTime = this.handler.getBrewTime();
        int totalBrewTime = this.handler.getTotalBrewTime();
        int fluidLevel = this.handler.getFluidLevel();
        int fluid = this.handler.getFluid();
        boolean isHot = this.handler.isHot();
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
        if (brewTime > 0) {
            int brewBarHeight = (int) (27.0F * (1.0F - (float) brewTime / totalBrewTime));
            if (brewBarHeight > 0) {
                context.drawGuiTexture(RenderLayer::getGuiTextured, BREW_PROGRESS_TEXTURE, 7, 27, 0, 0, i + 99, j + 17, 7, brewBarHeight);
            }
        }
        if (fluid == 0) {
            context.drawGuiTexture(RenderLayer::getGuiTextured, EMPTY_FLUID_TEXTURE, i + 65, j + 48, 46, 16);
        } else {
            if (fluidLevel > 0) {
                int teaColor;
                if (fluid == 2) {
                    teaColor = this.handler.getTeaColor();
                } else {
                    teaColor = 3694022;
                }
                int fluidHeight = (int) (12.0F * (float) fluidLevel / 3.0F) + 4;
                // cannot use drawGuiTexture here: method that can cut does not have a color attribute
                context.drawTexture(RenderLayer::getGuiTextured, FLUID_TEXTURE, i + 65, j + 64 - fluidHeight, 0, 0, 46, fluidHeight, 46, 16, ColorHelper.fullAlpha(teaColor));
            }
        }
        if (isHot) {
            context.drawGuiTexture(RenderLayer::getGuiTextured, FIRE_TEXTURE, i + 76, j + 68, 24, 9);
        }
    }
}
