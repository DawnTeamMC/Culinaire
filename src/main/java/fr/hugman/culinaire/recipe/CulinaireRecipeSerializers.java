package fr.hugman.culinaire.recipe;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CulinaireRecipeSerializers {
    public static final RecipeSerializer<SandwichRecipe> SANDWICH_CRAFTING = of("sandwich_crafting", new SandwichRecipe.Serializer());
    public static final RecipeSerializer<TeaBagMakingRecipe> TEA_BAG_MAKING = of("tea_bag_making", new SpecialCraftingRecipe.SpecialRecipeSerializer<>(TeaBagMakingRecipe::new));

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S of(String name, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, Culinaire.id(name), serializer);
    }
}
