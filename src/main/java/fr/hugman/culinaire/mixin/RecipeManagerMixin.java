package fr.hugman.culinaire.mixin;

import fr.hugman.culinaire.recipe.CulinaireRecipePropertySets;
import fr.hugman.culinaire.recipe.sandwich.SandwichCraftingRecipe;
import fr.hugman.culinaire.recipe.sandwich.SandwichRecipe;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipePropertySet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow
    @Final
    @Mutable
    private static Map<ResourceKey<RecipePropertySet>, RecipeManager.IngredientExtractor> RECIPE_PROPERTY_SETS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void injectCulinairePropertySets(CallbackInfo ci) {
        Map<ResourceKey<RecipePropertySet>, RecipeManager.IngredientExtractor> map = new HashMap<>(RECIPE_PROPERTY_SETS);
        map.put(CulinaireRecipePropertySets.SANDWICH_BREAD, recipe -> {
            if (recipe instanceof SandwichRecipe sandwichRecipe) {
                return Optional.of(sandwichRecipe.getBread());
            }
            if (recipe instanceof SandwichCraftingRecipe sandwichCraftingRecipe) {
                return Optional.of(sandwichCraftingRecipe.getBread());
            }
            return Optional.empty();
        });
        RECIPE_PROPERTY_SETS = Map.copyOf(map);
    }
}
