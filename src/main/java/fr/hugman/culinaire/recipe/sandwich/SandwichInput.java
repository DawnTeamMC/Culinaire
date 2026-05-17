package fr.hugman.culinaire.recipe.sandwich;

import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.ArrayList;
import java.util.List;

public class SandwichInput implements RecipeInput {
	public static final int MAIN_INGREDIENTS_COUNT = 3;
	public static final int COMPLEMENTS_COUNT = 3;

	public static final SandwichInput EMPTY = new SandwichInput(ItemStack.EMPTY, ItemStack.EMPTY, List.of());

	private final ItemStack topBread;
	private final ItemStack bottomBread;
	private final List<ItemStack> ingredients;

	private final StackedItemContents stackedContents = new StackedItemContents();
	private final int ingredientCount;

	public SandwichInput(ItemStack topBread, ItemStack bottomBread, List<ItemStack> ingredients) {
		this.topBread = topBread;
		this.bottomBread = bottomBread;
		this.ingredients = ingredients;
		int ingredientCount = 0;

		if (!topBread.isEmpty()) {
			ingredientCount++;
			this.stackedContents.accountStack(topBread, 1);
		}
		if (!bottomBread.isEmpty()) {
			ingredientCount++;
			this.stackedContents.accountStack(bottomBread, 1);
		}
		for (ItemStack item : ingredients) {
			if (!item.isEmpty()) {
				ingredientCount++;
				this.stackedContents.accountStack(item, 1);
			}
		}

		this.ingredientCount = ingredientCount;
	}

	public ItemStack getTopBread() {
		return topBread;
	}

	public ItemStack getBottomBread() {
		return bottomBread;
	}

	public List<ItemStack> getIngredients() {
		return ingredients;
	}


	@Override
	public ItemStack getItem(final int index) {
		if(index == 0) return this.topBread;
		if(index == 1) return this.bottomBread;
		return this.ingredients.get(index - 2);
	}

	@Override
	public int size() {
		return this.ingredients.size() + 2;
	}

	@Override
	public boolean isEmpty() {
		return this.ingredientCount == 0;
	}

	public StackedItemContents stackedContents() {
		return this.stackedContents;
	}

	public List<ItemStack> items() {
		List<ItemStack> items = new ArrayList<>();
		if (!this.topBread.isEmpty()) items.add(this.topBread);
		if (!this.bottomBread.isEmpty()) items.add(this.bottomBread);
		items.addAll(this.ingredients);
		return items;
	}
}
