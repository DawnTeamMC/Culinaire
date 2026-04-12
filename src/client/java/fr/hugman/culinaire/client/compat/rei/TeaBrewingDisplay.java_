package fr.hugman.culinaire.client.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        return CulinaireREIPlugin.TEA_BREWING;
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.empty();
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return null;
    }
}
