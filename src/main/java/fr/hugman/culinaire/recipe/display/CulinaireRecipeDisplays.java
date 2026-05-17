package fr.hugman.culinaire.recipe.display;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.recipe.sandwich.SandwichRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

public class CulinaireRecipeDisplays {
    public static final RecipeDisplay.Type<?> SANDWICH = register("sandwich", SandwichRecipeDisplay.TYPE);

    static RecipeDisplay.Type<?> register(String name, RecipeDisplay.Type<?> type) {
        return Registry.register(BuiltInRegistries.RECIPE_DISPLAY, Culinaire.id(name), type);
    }
}
