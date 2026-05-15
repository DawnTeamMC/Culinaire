package fr.hugman.culinaire.world.menu;

import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.recipe.CulinaireRecipePropertySets;
import fr.hugman.culinaire.world.menu.slot.SandwichMakingResultSlot;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.recipebook.ServerPlaceRecipe;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SandwichMakingMenu extends RecipeBookMenu {
    public static final int REQUIRED_INGREDIENTS_COUNT = 3;
    public static final int OPTIONAL_INGREDIENTS_COUNT = 3;
    public static final int SLOT_COUNT = 3 + REQUIRED_INGREDIENTS_COUNT + OPTIONAL_INGREDIENTS_COUNT;

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
        this.addSandwichMakingSlots();
        this.addStandardInventorySlots(inventory, 8, 84);
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    private void addSandwichMakingSlots() {
        var slotCount = 0;

        // Result
        this.addSlot(new SandwichMakingResultSlot(inventory.player, inputsContainer, resultContainer, slotCount++, 116, 44));

        // Top bread
        this.addSlot(new Slot(inputsContainer, slotCount++, 62, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return SandwichMakingMenu.this.breadTest.test(stack);
            }
        });

        // Required ingredients
        for(int i = 0; i < REQUIRED_INGREDIENTS_COUNT; ++i) {
            this.addSlot(new Slot(inputsContainer, slotCount++, 44 + i * 18, 35));
        }

        // Optional ingredients
        for(int i = 0; i < OPTIONAL_INGREDIENTS_COUNT; ++i) {
            this.addSlot(new Slot(inputsContainer, slotCount++, 44 + i * 18, 53));
        }

        // Bottom bread
        this.addSlot(new Slot(inputsContainer, slotCount++, 62, 71) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return SandwichMakingMenu.this.breadTest.test(stack);
            }
        });
    }

    protected static void slotChangedCraftingGrid(
            final AbstractContainerMenu menu,
            final ServerLevel level,
            final Player player,
            final CraftingInput input,
            final ResultContainer resultSlots,
            @Nullable final RecipeHolder<CraftingRecipe> recipeHint
    ) {
        ServerPlayer serverPlayer = (ServerPlayer)player;
        ItemStack result = ItemStack.EMPTY;
        Optional<RecipeHolder<CraftingRecipe>> maybeRecipe = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level, recipeHint);
        if (maybeRecipe.isPresent()) {
            RecipeHolder<CraftingRecipe> recipeHolder = maybeRecipe.get();
            CraftingRecipe craftingRecipe = recipeHolder.value();
            if (resultSlots.setRecipeUsed(serverPlayer, recipeHolder)) {
                ItemStack recipeResult = craftingRecipe.assemble(input);
                if (recipeResult.isItemEnabled(level.enabledFeatures())) {
                    result = recipeResult;
                }
            }
        }

        resultSlots.setItem(0, result);
        menu.setRemoteSlot(0, result);
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, result));
    }

    private CraftingInput createCraftingInput() {
        List<ItemStack> list = new java.util.ArrayList<>(java.util.Collections.nCopies(9, ItemStack.EMPTY));
        for (int i = 1; i < 9; i++) {
            list.set(i, this.inputsContainer.getItem(i));
        }
        return CraftingInput.of(3, 3, list);
    }

    public void beginPlacingRecipe() {
        this.placingRecipe = true;
    }

    public void finishPlacingRecipe(final ServerLevel level, final RecipeHolder<CraftingRecipe> recipe) {
        this.placingRecipe = false;
        slotChangedCraftingGrid(this, level, this.player, this.createCraftingInput(), this.resultContainer, recipe);
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
                    slotChangedCraftingGrid(this, serverLevel, this.player, this.createCraftingInput(), this.resultContainer, null);
                }
            });
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        //TODO
        return ItemStack.EMPTY;
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
        RecipeHolder<CraftingRecipe> typedRecipe = (RecipeHolder<CraftingRecipe>)recipe;
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
            public boolean recipeMatches(RecipeHolder<CraftingRecipe> recipe) {
                return recipe.value().matches(SandwichMakingMenu.this.createCraftingInput(), level);
            }
        }, 3, 3, inputSlots, inputSlots, inventory, typedRecipe, useMaxItems, allowDroppingItemsToClear);

        this.finishPlacingRecipe(level, typedRecipe);

        return place;
    }

    @Override
    public boolean canTakeItemForPickAll(final ItemStack carried, final Slot target) {
        return target.container != this.inputsContainer && super.canTakeItemForPickAll(carried, target);
    }

    public Slot getResultSlot() {
        return this.slots.get(SLOT_COUNT - 1);
    }

    public List<Slot> getInputGridSlots() {
        return this.slots.subList(1, 10);
    }


}
