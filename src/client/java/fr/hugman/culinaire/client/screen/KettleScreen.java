package fr.hugman.culinaire.client.screen;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.screen.KettleScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;

public class KettleScreen extends AbstractContainerScreen<KettleScreenHandler> {
    private static final Identifier TEXTURE = Culinaire.id("textures/gui/container/kettle.png");
    private static final Identifier EMPTY_FLUID_TEXTURE = Culinaire.id("container/kettle/empty_fluid");
    private static final Identifier FLUID_TEXTURE = Culinaire.id("textures/gui/sprites/container/kettle/fluid.png");
    private static final Identifier BREW_PROGRESS_TEXTURE = Culinaire.id("container/kettle/brew_progress");
    private static final Identifier FIRE_TEXTURE = Culinaire.id("container/kettle/fire");

    public KettleScreen(KettleScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int brewTime = this.menu.getBrewTime();
        int totalBrewTime = this.menu.getTotalBrewTime();
        int fluidLevel = this.menu.getFluidLevel();
        int fluid = this.menu.getFluid();
        boolean isHot = this.menu.isHot();
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        if (brewTime > 0) {
            int brewBarHeight = (int) (27.0F * (1.0F - (float) brewTime / totalBrewTime));
            if (brewBarHeight > 0) {
                context.blitSprite(RenderPipelines.GUI_TEXTURED, BREW_PROGRESS_TEXTURE, 7, 27, 0, 0, i + 99, j + 17, 7, brewBarHeight);
            }
        }
        if (fluid == 0) {
            context.blitSprite(RenderPipelines.GUI_TEXTURED, EMPTY_FLUID_TEXTURE, i + 65, j + 48, 46, 16);
        } else {
            if (fluidLevel > 0) {
                int teaColor;
                if (fluid == 2) {
                    teaColor = this.menu.getTeaColor();
                    //FIXME the color is not correct for some reason
                } else {
                    teaColor = -13083194;
                }
                int fluidHeight = (int) (12.0F * (float) fluidLevel / 3.0F) + 4;
                // cannot use drawGuiTexture here: method that can cut does not have a color attribute
                context.blit(RenderPipelines.GUI_TEXTURED, FLUID_TEXTURE, i + 65, j + 64 - fluidHeight, 0, 0, 46, fluidHeight, 46, 16, ARGB.opaque(teaColor));
            }
        }
        if (isHot) {
            context.blitSprite(RenderPipelines.GUI_TEXTURED, FIRE_TEXTURE, i + 76, j + 68, 24, 9);
        }
    }
}
