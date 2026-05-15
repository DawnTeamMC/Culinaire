package fr.hugman.culinaire.client.screen;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.client.screen.recipebook.SandwichMakingRecipeBookComponent;
import fr.hugman.culinaire.world.menu.SandwichMakingMenu;
import fr.hugman.culinaire.client.mixin.AbstractContainerScreenAccessor;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class SandwichMakingScreen extends AbstractRecipeBookScreen<SandwichMakingMenu> {
    private static final Identifier TEXTURE_LOCATION = Culinaire.id("textures/gui/container/sandwich_making.png");

    public SandwichMakingScreen(SandwichMakingMenu menu, Inventory inventory, Component title) {
        super(menu, new SandwichMakingRecipeBookComponent(menu), inventory, title);
        ((AbstractContainerScreenAccessor) this).setImageHeight(184);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 29;
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 5, this.height / 2 - 49); //TODO ?
    }

    @Override
    public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = this.leftPos;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        // render slots
    }
}
