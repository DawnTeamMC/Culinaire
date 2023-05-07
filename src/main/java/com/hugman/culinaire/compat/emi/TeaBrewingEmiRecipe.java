package com.hugman.culinaire.compat.emi;

import com.hugman.culinaire.Culinaire;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeaBrewingEmiRecipe implements EmiRecipe {
    private static final Identifier DISPLAY_TEXTURE = Culinaire.id("textures/gui/rei/display.png");

    private final EmiIngredient input;
    private final EmiStack output;
    private final int teaColor;
    private final Identifier id;

    public TeaBrewingEmiRecipe(EmiIngredient input, EmiStack output, Identifier id, int teaColor) {
        this.input = input;
        this.output = output;
        this.id = id;
        this.teaColor = teaColor;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return CulinaireEMIPlugin.TEA_BREWING_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 70;
    }

    @Override
    public int getDisplayHeight() {
        return 61;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, 70, 60, (matrices, mouseX, mouseY, delta) -> {
            RenderSystem.setShaderTexture(0, DISPLAY_TEXTURE);

            // main texture
            DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 70, 60);

            // tea color texture
            float red = (teaColor >> 16 & 255) / 255.0F;
            float green = (teaColor >> 8 & 255) / 255.0F;
            float blue = (teaColor & 255) / 255.0F;
            RenderSystem.setShaderColor(red, green, blue, 1.0F);
            DrawableHelper.drawTexture(matrices, 1, 32, 70, 0, 46, 16);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            // fire texture
            DrawableHelper.drawTexture(matrices, 12, 52, 70, 16, 24, 9);

            // animated arrow texture
            int height = MathHelper.ceil(System.currentTimeMillis() / 250d % 26d);
            DrawableHelper.drawTexture(matrices, 35, 1, 70, 25, 7, height);
        });
        widgets.addSlot(input, 15, 0).drawBack(false);
        widgets.addSlot(output, 52, 31).drawBack(false).recipeContext(this);
    }
}
