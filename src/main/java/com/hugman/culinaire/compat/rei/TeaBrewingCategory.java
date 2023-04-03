package com.hugman.culinaire.compat.rei;

import com.google.common.collect.Lists;
import com.hugman.culinaire.registry.content.TeaContent;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class TeaBrewingCategory implements DisplayCategory<TeaBrewingDisplay> {

    @Override
    public CategoryIdentifier<? extends TeaBrewingDisplay> getCategoryIdentifier() {
        return CulinaireREIPlugin.TEA_BREWING;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(TeaContent.KETTLE);
    }

    @Override
    public Text getTitle() {
        return Text.translatable("rei_category.culinaire.tea_brewing");
    }

    @Override
    public List<Widget> setupDisplay(TeaBrewingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 24, bounds.getCenterY() - 30);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
            RenderSystem.setShaderTexture(0, CulinaireREIPlugin.getDisplayTexture());

            // main texture
            DrawableHelper.drawTexture(matrices, startPoint.x, startPoint.y, 0, 0, 70, 60);

            // tea color texture
            int teaColor = display.getTeaColor();
            float red = (teaColor >> 16 & 255) / 255.0F;
            float green = (teaColor >> 8 & 255) / 255.0F;
            float blue = (teaColor & 255) / 255.0F;
            RenderSystem.setShaderColor(red, green, blue, 1.0F);
            DrawableHelper.drawTexture(matrices, startPoint.x + 1, startPoint.y + 32, 70, 0, 46, 16);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            // fire texture
            DrawableHelper.drawTexture(matrices, startPoint.x + 12, startPoint.y + 52, 70, 16, 24, 9);

            // animated arrow texture
            int height = MathHelper.ceil(System.currentTimeMillis() / 250d % 26d);
            DrawableHelper.drawTexture(matrices, startPoint.x + 35, startPoint.y + 1, 70, 25, 7, height);
        }));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 16, startPoint.y + 1)).entries(display.getInputEntries().get(0)).disableBackground().markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 53, startPoint.y + 32)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 68;
    }
}
