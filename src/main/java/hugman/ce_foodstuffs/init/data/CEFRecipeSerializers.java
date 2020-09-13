package hugman.ce_foodstuffs.init.data;

import hugman.ce_foodstuffs.CEFoodstuffs;
import hugman.ce_foodstuffs.objects.recipe.SandwichMakingRecipe;
import hugman.ce_foodstuffs.objects.recipe.TeaBagMakingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CEFRecipeSerializers {
	public static final SpecialRecipeSerializer<SandwichMakingRecipe> SANDWICH_MAKING = register("sandwich_making", new SpecialRecipeSerializer<>(SandwichMakingRecipe::new));
	public static final SpecialRecipeSerializer<TeaBagMakingRecipe> TEA_BAG_MAKING = register("tea_bag_making", new SpecialRecipeSerializer<>(TeaBagMakingRecipe::new));

	private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(CEFoodstuffs.MOD_ID, name), serializer);
	}
}
