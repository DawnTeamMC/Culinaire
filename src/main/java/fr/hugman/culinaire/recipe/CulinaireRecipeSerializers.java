package fr.hugman.culinaire.recipe;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CulinaireRecipeSerializers {
    public static final RecipeSerializer<SandwichRecipe> SANDWICH = of("sandwich", new SandwichRecipe.Serializer());
    public static final RecipeSerializer<TeaBagRecipe> TEA_BAG = of("tea_bag", new TeaBagRecipe.Serializer());

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S of(String name, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, Culinaire.id(name), serializer);
    }
}
