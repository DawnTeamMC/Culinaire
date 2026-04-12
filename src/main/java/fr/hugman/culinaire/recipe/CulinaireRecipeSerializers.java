package fr.hugman.culinaire.recipe;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CulinaireRecipeSerializers {
    public static final RecipeSerializer<SandwichRecipe> SANDWICH = of("sandwich", new SandwichRecipe.Serializer());
    public static final RecipeSerializer<TeaBagRecipe> TEA_BAG = of("tea_bag", new TeaBagRecipe.Serializer());

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S of(String name, S serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Culinaire.id(name), serializer);
    }
}
