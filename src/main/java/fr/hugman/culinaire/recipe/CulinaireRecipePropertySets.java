package fr.hugman.culinaire.recipe;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipePropertySet;

public class CulinaireRecipePropertySets {
    public static final ResourceKey<RecipePropertySet> SANDWICH_BREAD = register("sandwich_bread");

    private static ResourceKey<RecipePropertySet> register(String name) {
        return ResourceKey.create(RecipePropertySet.TYPE_KEY, Culinaire.id(name));
    }
}
