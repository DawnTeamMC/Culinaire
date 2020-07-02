package hugman.common_expansion.init.data;

import hugman.common_expansion.CommonExpansion;
import hugman.common_expansion.objects.recipe.SandwichRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CERecipeSerializers {
	public static final SpecialRecipeSerializer<SandwichRecipe> SANDWICH_MAKING = register("sandwich_making", new SpecialRecipeSerializer<>(SandwichRecipe::new));

	private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(CommonExpansion.MOD_ID, name), serializer);
	}
}
