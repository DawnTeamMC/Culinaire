package fr.hugman.culinaire.recipe;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.recipe.sandwich.SandwichCraftingRecipe;
import fr.hugman.culinaire.recipe.sandwich.SandwichRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public class CulinaireRecipeTypes {
    public static final RecipeType<SandwichRecipe> SANDWICH = register("sandwich");

    static <T extends Recipe<?>> RecipeType<T> register(String name) {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, Culinaire.id(name), new RecipeType<T>() {
            public String toString() {
                return name;
            }
        });
    }
}
