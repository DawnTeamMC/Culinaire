package fr.hugman.culinaire.world.menu.slot;

import fr.hugman.culinaire.recipe.CulinaireRecipeTypes;
import fr.hugman.culinaire.recipe.sandwich.SandwichInput;
import fr.hugman.culinaire.recipe.sandwich.SandwichRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class SandwichMakingResultSlot extends Slot {
    private final Player player;
    private final Container inputSlots;
    private int removeCount;

    public SandwichMakingResultSlot(Player player, Container inputSlots, Container container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.player = player;
        this.inputSlots = inputSlots;
    }

    @Override
    public boolean mayPlace(final ItemStack itemStack) {
        return false;
    }

    @Override
    public ItemStack remove(final int amount) {
        if (this.hasItem()) {
            this.removeCount = this.removeCount + Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    protected void onQuickCraft(final ItemStack picked, final int count) {
        this.removeCount += count;
        this.checkTakeAchievements(picked);
    }

    @Override
    protected void checkTakeAchievements(final ItemStack carried) {
        if (this.removeCount > 0) {
            carried.onCraftedBy(this.player, this.removeCount);
        }

        /*
        if (this.container instanceof RecipeCraftingHolder recipeCraftingHolder) {
            recipeCraftingHolder.awardUsedRecipes(this.player, this.inputSlots.getItems());
        }

         */

        this.removeCount = 0;
    }

    private static NonNullList<ItemStack> copyAllInputItems(SandwichInput input) {
        NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int slot = 0; slot < result.size(); slot++) {
            result.set(slot, input.getItem(slot));
        }

        return result;
    }

    private NonNullList<ItemStack> getRemainingItems(SandwichInput input, Level level) {
        return level instanceof ServerLevel serverLevel
                ? serverLevel.recipeAccess()
                               .getRecipeFor(CulinaireRecipeTypes.SANDWICH, input, serverLevel)
                               .map(recipe -> recipe.value().getRemainingItems(input))
                               .orElseGet(() -> copyAllInputItems(input))
                : SandwichRecipe.defaultCraftingReminder(input);
    }

    private SandwichInput createInput() {
        int ingredientCount = SandwichInput.MAIN_INGREDIENTS_COUNT + SandwichInput.COMPLEMENTS_COUNT;
        List<ItemStack> ingredients = new ArrayList<>(ingredientCount);
        for (int i = 1; i < 1 + ingredientCount; i++) {
            ingredients.add(this.inputSlots.getItem(i));
        }

        return new SandwichInput(
                this.inputSlots.getItem(0),
                this.inputSlots.getItem(1 + SandwichInput.MAIN_INGREDIENTS_COUNT + SandwichInput.COMPLEMENTS_COUNT),
                ingredients
        );
    }

    @Override
    public void onTake(final Player player, final ItemStack carried) {
        this.checkTakeAchievements(carried);
        SandwichInput input = createInput();
        NonNullList<ItemStack> remaining = this.getRemainingItems(input, player.level());

        for (int slot = 0; slot < (2+SandwichInput.MAIN_INGREDIENTS_COUNT + SandwichInput.COMPLEMENTS_COUNT); slot++) {
            ItemStack itemStack = this.inputSlots.getItem(slot);
            ItemStack replacement = remaining.get(slot);
            if (!itemStack.isEmpty()) {
                this.inputSlots.removeItem(slot, 1);
                itemStack = this.inputSlots.getItem(slot);
            }

            if (!replacement.isEmpty()) {
                if (itemStack.isEmpty()) {
                    this.inputSlots.setItem(slot, replacement);
                } else if (ItemStack.isSameItemSameComponents(itemStack, replacement)) {
                    replacement.grow(itemStack.getCount());
                    this.inputSlots.setItem(slot, replacement);
                } else if (!this.player.getInventory().add(replacement)) {
                    this.player.drop(replacement, false);
                }
            }
        }
    }

    @Override
    public boolean isFake() {
        return true;
    }
}
