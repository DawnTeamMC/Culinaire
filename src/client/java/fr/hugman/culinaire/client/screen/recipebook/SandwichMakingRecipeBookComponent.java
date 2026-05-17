package fr.hugman.culinaire.client.screen.recipebook;

import fr.hugman.culinaire.recipe.display.SandwichRecipeDisplay;
import fr.hugman.culinaire.world.menu.SandwichMakingMenu;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.display.*;

import java.util.List;

public class SandwichMakingRecipeBookComponent extends RecipeBookComponent<SandwichMakingMenu> {
	private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
			Identifier.withDefaultNamespace("recipe_book/filter_enabled"),
			Identifier.withDefaultNamespace("recipe_book/filter_disabled"),
			Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted"),
			Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted")
	);
	private static final Component ONLY_CRAFTABLES_TOOLTIP = Component.translatable("gui.recipebook.toggleRecipes.craftable");
	private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
			new RecipeBookComponent.TabInfo(SearchRecipeBookCategory.CRAFTING),
			new RecipeBookComponent.TabInfo(Items.IRON_AXE, Items.GOLDEN_SWORD, RecipeBookCategories.CRAFTING_EQUIPMENT),
			new RecipeBookComponent.TabInfo(Items.BRICKS, RecipeBookCategories.CRAFTING_BUILDING_BLOCKS),
			new RecipeBookComponent.TabInfo(Items.LAVA_BUCKET, Items.APPLE, RecipeBookCategories.CRAFTING_MISC),
			new RecipeBookComponent.TabInfo(Items.REDSTONE, RecipeBookCategories.CRAFTING_REDSTONE)
	);

	public SandwichMakingRecipeBookComponent(final SandwichMakingMenu menu) {
		super(menu, TABS);
	}

	@Override
	protected WidgetSprites getFilterButtonTextures() {
		return FILTER_SPRITES;
	}

	@Override
	protected boolean isCraftingSlot(final Slot slot) {
		return switch (slot.index) {
			case 0, 1, 2, 3, 4, 5, 6, 7, 8 -> true;
			default -> false;
		};
	}

	@Override
	protected void fillGhostRecipe(final GhostSlots ghostSlots, final RecipeDisplay recipe, final ContextMap context) {
		ghostSlots.setResult(this.menu.getResultSlot(), context, recipe.result());

		if(recipe instanceof SandwichRecipeDisplay sandwich) {
			ghostSlots.setInput(this.menu.getTopBreadSlot(), context, sandwich.bread());
			ghostSlots.setInput(this.menu.getBottomBreadSlot(), context, sandwich.bread());

			List<Slot> inputSlots = this.menu.getIngredientSlots();
			int slotCount = Math.min(sandwich.ingredients().size(), inputSlots.size());

			for (int i = 0; i < slotCount; i++) {
				ghostSlots.setInput((Slot)inputSlots.get(i), context, (SlotDisplay) sandwich.ingredients().get(i));
			}
		}
	}

	@Override
	protected Component getRecipeFilterName() {
		return ONLY_CRAFTABLES_TOOLTIP;
	}

	@Override
	protected void selectMatchingRecipes(final RecipeCollection collection, final StackedItemContents stackedContents) {
		collection.selectRecipes(stackedContents, display -> display instanceof SandwichRecipeDisplay);
	}
}
