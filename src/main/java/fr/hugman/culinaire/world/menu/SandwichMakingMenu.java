package fr.hugman.culinaire.world.menu;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.recipe.CulinaireRecipePropertySets;
import fr.hugman.culinaire.recipe.CulinaireRecipeTypes;
import fr.hugman.culinaire.recipe.sandwich.SandwichInput;
import fr.hugman.culinaire.recipe.sandwich.SandwichRecipe;
import fr.hugman.culinaire.world.menu.slot.SandwichMakingResultSlot;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class SandwichMakingMenu extends RecipeBookMenu {
    private static final Identifier EMPTY_SLOT_BREAD = Culinaire.id("container/slot/bread");

    public static final int SLOT_COUNT = 2 + SandwichInput.MAIN_INGREDIENTS_COUNT + SandwichInput.COMPLEMENTS_COUNT;

    private final ContainerLevelAccess access;
    private final Player player;

    private final Inventory inventory;
    private final Container inputsContainer;
    private final ResultContainer resultContainer = new ResultContainer();
    private boolean placingRecipe;

    private final RecipePropertySet breadTest;

    public SandwichMakingMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, inventory.player.level(), ContainerLevelAccess.NULL);
    }

    public SandwichMakingMenu(int containerId, Inventory inventory, Level level, ContainerLevelAccess access) {
        super(CulinaireMenuTypes.SANDWICH_MAKING, containerId);
        this.inventory = inventory;
        this.player = inventory.player;
        this.access = access;
        this.inputsContainer = new SimpleContainer(SLOT_COUNT) {
            @Override
            public void setChanged() {
                super.setChanged();
                SandwichMakingMenu.this.slotsChanged(this);
            }
        };
        this.breadTest = level.recipeAccess().propertySet(CulinaireRecipePropertySets.SANDWICH_BREAD);
        this.addSandwichMakingSlots(30, 17);
        this.addStandardInventorySlots(inventory, 8, 102);
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    private void addSandwichMakingSlots(int x, int y) {
        var slotCount = 0;

        // Result
        this.addSlot(new SandwichMakingResultSlot(inventory.player, inputsContainer, resultContainer, 0, x + 94, y + 27));

        // Top bread
        this.addSlot(new Slot(inputsContainer, slotCount++, x + 18, y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return SandwichMakingMenu.this.breadTest.test(stack);
            }

            @Override
            public Identifier getNoItemIcon() {
                return EMPTY_SLOT_BREAD;
            }
        });

        // Required ingredients
        for(int i = 0; i < SandwichInput.MAIN_INGREDIENTS_COUNT; ++i) {
            this.addSlot(new Slot(inputsContainer, slotCount++, x + i * 18, y + 18));
        }

        // Optional ingredients
        for(int i = 0; i < SandwichInput.COMPLEMENTS_COUNT; ++i) {
            this.addSlot(new Slot(inputsContainer, slotCount++, x + i * 18, y + 18 * 2));
        }

        // Bottom bread
        this.addSlot(new Slot(inputsContainer, slotCount++, x + 18, y + 18 * 3) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return SandwichMakingMenu.this.breadTest.test(stack);
            }

            @Override
            public Identifier getNoItemIcon() {
                return EMPTY_SLOT_BREAD;
            }
        });
    }

    protected static void slotChangedCraftingGrid(
            final AbstractContainerMenu menu,
            final ServerLevel level,
            final Player player,
            final SandwichInput input,
            final ResultContainer resultSlots,
            @Nullable final RecipeHolder<SandwichRecipe> recipeHint
    ) {
        ServerPlayer serverPlayer = (ServerPlayer)player;
        ItemStack result = ItemStack.EMPTY;
        Optional<RecipeHolder<SandwichRecipe>> maybeRecipe = level.getServer().getRecipeManager().getRecipeFor(CulinaireRecipeTypes.SANDWICH, input, level, recipeHint);
        if (maybeRecipe.isPresent()) {
            RecipeHolder<SandwichRecipe> recipeHolder = maybeRecipe.get();
            SandwichRecipe sandwichRecipe = recipeHolder.value();
            if (resultSlots.setRecipeUsed(serverPlayer, recipeHolder)) {
                ItemStack recipeResult = sandwichRecipe.assemble(input);
                if (recipeResult.isItemEnabled(level.enabledFeatures())) {
                    result = recipeResult;
                }
            }
        }

        resultSlots.setItem(0, result);
        menu.setRemoteSlot(0, result);
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, result));
    }

    private SandwichInput createInput() {
        int ingredientCount = SandwichInput.MAIN_INGREDIENTS_COUNT + SandwichInput.COMPLEMENTS_COUNT;
        List<ItemStack> ingredients = new ArrayList<>(ingredientCount);
        for (int i = 1; i < 1 + ingredientCount; i++) {
            ingredients.add(this.inputsContainer.getItem(i));
        }

        return new SandwichInput(
                this.inputsContainer.getItem(0),
                this.inputsContainer.getItem(1 + SandwichInput.MAIN_INGREDIENTS_COUNT + SandwichInput.COMPLEMENTS_COUNT),
                ingredients
        );
    }

    public void beginPlacingRecipe() {
        this.placingRecipe = true;
    }

    public void finishPlacingRecipe(final ServerLevel level, final RecipeHolder<SandwichRecipe> recipe) {
        this.placingRecipe = false;
        slotChangedCraftingGrid(this, level, this.player, this.createInput(), this.resultContainer, recipe);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
        if (this.inputsContainer instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible)this.inputsContainer).fillStackedContents(stackedContents);
        }
    }

    @Override
    public void slotsChanged(final Container container) {
        if (!this.placingRecipe) {
            this.access.execute((level, pos) -> {
                if (level instanceof ServerLevel serverLevel) {
                    slotChangedCraftingGrid(this, serverLevel, this.player, this.createInput(), this.resultContainer, null);
                }
            });
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        int minInventory = SLOT_COUNT + 1; // 0 is result
        int minHotbar = minInventory + 27;
        int maxInventory = minInventory + 36;

        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();
            // from result
            if (slotIndex == 0) {
                stack.getItem().onCraftedBy(stack, player);
                if (!this.moveItemStackTo(stack, minInventory, maxInventory, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stack, clicked);
            }
            // from inventory
            else if (slotIndex >= minInventory && slotIndex < maxInventory) {
                if (!this.moveItemStackTo(stack, 1, minInventory, false)) {
                    if (slotIndex < minHotbar) {
                        if (!this.moveItemStackTo(stack, minHotbar, maxInventory, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(stack, minInventory, minHotbar, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            // from anywhere else (recipe slots)
            else if (!this.moveItemStackTo(stack, minInventory, maxInventory, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == clicked.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
            if (slotIndex == 0) {
                player.drop(stack, false);
            }
        }

        return clicked;
    }

    @Override
    public void removed(final Player player) {
        super.removed(player);
        this.access.execute((level, pos) -> this.clearContainer(player, this.inputsContainer));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, CulinaireBlocks.SANDWICH_MAKING_TABLE);
    }

    @Override
    public PostPlaceAction handlePlacement(
            boolean useMaxItems,
            boolean allowDroppingItemsToClear,
            RecipeHolder<?> recipe,
            ServerLevel level,
            Inventory inventory
    ) {
        RecipeHolder<SandwichRecipe> typedRecipe = (RecipeHolder<SandwichRecipe>)recipe;
        this.beginPlacingRecipe();
        List<Slot> inputSlots = this.getInputGridSlots();
        var place = ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<>() {
            {
                Objects.requireNonNull(SandwichMakingMenu.this);
            }

            @Override
            public void fillCraftSlotsStackedContents(final StackedItemContents stackedContents) {
                SandwichMakingMenu.this.fillCraftSlotsStackedContents(stackedContents);
            }

            @Override
            public void clearCraftingContent() {
                SandwichMakingMenu.this.inputsContainer.clearContent();
                SandwichMakingMenu.this.resultContainer.clearContent();
            }

            @Override
            public boolean recipeMatches(RecipeHolder<SandwichRecipe> recipe) {
                return recipe.value().matches(SandwichMakingMenu.this.createInput(), level);
            }
        }, 3, 3, inputSlots, inputSlots, inventory, typedRecipe, useMaxItems, allowDroppingItemsToClear);

        this.finishPlacingRecipe(level, typedRecipe);

        return place;
    }

    @Override
    public boolean canTakeItemForPickAll(final ItemStack carried, final Slot target) {
        return target.container != this.inputsContainer && super.canTakeItemForPickAll(carried, target);
    }

    public List<Slot> getInputGridSlots() {
        return this.slots.subList(1, 9);
    }

    public Slot getTopBreadSlot() {
        return this.slots.get(1);
    }

    public Slot getBottomBreadSlot() {
        return this.slots.get(8);
    }

    public List<Slot> getIngredientSlots() {
        return this.slots.subList(2, 8);
    }

    public Slot getResultSlot() {
        return this.slots.getFirst();
    }
}
