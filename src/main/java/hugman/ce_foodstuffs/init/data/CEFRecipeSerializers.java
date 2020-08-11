package hugman.ce_foodstuffs.init.data;

import hugman.ce_foodstuffs.CEFoodstuffs;
import hugman.ce_foodstuffs.objects.recipe.SandwichRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CEFRecipeSerializers {
	public static final SpecialRecipeSerializer<SandwichRecipe> SANDWICH_MAKING = register("sandwich_making", new SpecialRecipeSerializer<>(SandwichRecipe::new));

	private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(CEFoodstuffs.MOD_ID, name), serializer);
	}
}
