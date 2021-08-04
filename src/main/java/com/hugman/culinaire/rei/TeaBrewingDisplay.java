package com.hugman.culinaire.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class TeaBrewingDisplay implements Display {
    private final EntryIngredient input;
    private final EntryStack<?> output;
    private final int teaColor;

    public TeaBrewingDisplay(ItemStack input, ItemStack output, int teaColor) {
        this.input = EntryIngredients.of(input);
        this.output = EntryStacks.of(output);
        this.teaColor = teaColor;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return Collections.singletonList(this.input);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return Collections.singletonList(EntryIngredient.of(this.output));
    }

    public int getTeaColor() {
        return this.teaColor;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CulinairePlugin.TEA_BREWING;
    }
}
