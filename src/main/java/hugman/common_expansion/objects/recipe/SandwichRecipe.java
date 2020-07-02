package hugman.common_expansion.objects.recipe;

import hugman.common_expansion.init.CEItems;
import hugman.common_expansion.init.data.CETags;
import hugman.common_expansion.objects.item.SandwichItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SandwichRecipe extends SpecialCraftingRecipe {
	private static final Ingredient BREAD = Ingredient.fromTag(CETags.Items.SANDWICH_BREAD);

	public SandwichRecipe(Identifier identifier) {
		super(identifier);
	}

	public ItemStack getOutput() {
		return new ItemStack(CEItems.SANDWICH);
	}

	public boolean matches(CraftingInventory craftingInventory, World world) {
		boolean hasBread = false;
		boolean hasAnIngredient = false;
		int[] emptySlots = new int[]{0, 2, 6, 8};
		for(int i = 0; i < emptySlots.length; i++) {
			ItemStack itemStack = craftingInventory.getStack(emptySlots[i]);
			if(!itemStack.isEmpty()) {
				return false;
			}
		}
		ItemStack topMiddleStack = craftingInventory.getStack(1);
		ItemStack bottomMiddleStack = craftingInventory.getStack(7);
		if(!topMiddleStack.isEmpty() && !bottomMiddleStack.isEmpty()) {
			if(BREAD.test(topMiddleStack) && BREAD.test(bottomMiddleStack)) {
				hasBread = true;
			}
		}
		for(int i = 3; i < 6; ++i) {
			ItemStack itemStack = craftingInventory.getStack(i);
			if(!itemStack.isEmpty()) {
				if(itemStack.isFood() && !BREAD.test(itemStack)) {
					hasAnIngredient = true;
				}
				else {
					return false;
				}
			}
		}
		return hasBread && hasAnIngredient;
	}

	public ItemStack craft(CraftingInventory craftingInventory) {
		ItemStack sandwich = new ItemStack(CEItems.SANDWICH);
		for(int j = 3; j < 6; ++j) {
			ItemStack ingredient = craftingInventory.getStack(j);
			if(!ingredient.isEmpty()) {
				SandwichItem.addIngredient(sandwich, ingredient);
			}
		}
		return sandwich;
	}

	@Environment(EnvType.CLIENT)
	public boolean fits(int width, int height) {
		return width == 3 && height == 3;
	}

	public RecipeSerializer<?> getSerializer() {
		return RecipeSerializer.FIREWORK_ROCKET;
	}
}
