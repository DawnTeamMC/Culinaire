package fr.hugman.culinaire.recipe;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.recipe.sandwich.SandwichCraftingRecipe;
import fr.hugman.culinaire.recipe.sandwich.SandwichRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CulinaireRecipeSerializers {
    public static final RecipeSerializer<SandwichRecipe> SANDWICH = of("sandwich", SandwichRecipe.SERIALIZER);
    public static final RecipeSerializer<SandwichCraftingRecipe> SANDWICH_CRAFTING = of("sandwich_crafting", SandwichCraftingRecipe.SERIALIZER);
    public static final RecipeSerializer<TeaBagRecipe> TEA_BAG = of("tea_bag", TeaBagRecipe.SERIALIZER);

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S of(String name, S serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Culinaire.id(name), serializer);
    }
}
