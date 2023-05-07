package com.hugman.culinaire.compat.emi;

import com.hugman.culinaire.screen.handler.KettleScreenHandler;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.screen.slot.Slot;

import java.util.List;

public class BrewingRecipeHandler implements StandardRecipeHandler<KettleScreenHandler> {
    @Override
    public List<Slot> getInputSources(KettleScreenHandler handler) {
        return handler.slots;
    }

    @Override
    public List<Slot> getCraftingSlots(KettleScreenHandler handler) {
        return List.of(handler.getSlot(0));
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return CulinaireEMIPlugin.TEA_BREWING_CATEGORY.equals(recipe.getCategory());
    }
}
