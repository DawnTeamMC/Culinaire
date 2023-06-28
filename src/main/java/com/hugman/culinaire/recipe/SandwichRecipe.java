package com.hugman.culinaire.recipe;

import com.hugman.culinaire.item.SandwichItem;
import com.hugman.culinaire.registry.content.MealContent;
import com.hugman.culinaire.util.FoodUtil;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Map;

public class SandwichRecipe extends SpecialCraftingRecipe {
    public final Ingredient bread;
    public final Ingredient ingredientBlacklist;
    public final float hungerModifierBase;
    public final float hungerModifierBoosted;
    public final float saturationModifierBase;
    public final float saturationModifierBoosted;
    public final Map<Ingredient, Ingredient> ingredientAssociations;
    public final ItemStack resultItem;

    public SandwichRecipe(Identifier identifier, CraftingRecipeCategory category, Ingredient bread, Ingredient ingredientBlacklist, float hungerModifierBase, float hungerModifierBoosted, float saturationModifierBase, float saturationModifierBoosted, Map<Ingredient, Ingredient> ingredientAssociations, ItemStack resultItem) {
        super(identifier, category);
        this.bread = bread;
        this.ingredientBlacklist = ingredientBlacklist;
        this.hungerModifierBase = hungerModifierBase;
        this.hungerModifierBoosted = hungerModifierBoosted;
        this.saturationModifierBase = saturationModifierBase;
        this.saturationModifierBoosted = saturationModifierBoosted;
        this.ingredientAssociations = ingredientAssociations;
        this.resultItem = resultItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MealContent.SANDWICH_CRAFTING;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager drm) {
        return resultItem;
    }

    @Override
    public boolean fits(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public boolean matches(RecipeInputInventory inv, World world) {
        boolean hasBread = false;
        boolean hasOnlyIngredients = false;
        int[] emptySlots = new int[]{0, 2, 6, 8};
        for (int emptySlot : emptySlots) {
            ItemStack itemStack = inv.getStack(emptySlot);
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        ItemStack topMiddleStack = inv.getStack(1);
        ItemStack bottomMiddleStack = inv.getStack(7);
        if (!topMiddleStack.isEmpty() && !bottomMiddleStack.isEmpty()) {
            if (bread.test(topMiddleStack) && bread.test(bottomMiddleStack)) {
                hasBread = true;
            }
        }
        for (int i = 3; i < 6; ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.isFood() && !ingredientBlacklist.test(itemStack)) {
                    hasOnlyIngredients = true;
                } else {
                    return false;
                }
            }
        }
        return hasBread && hasOnlyIngredients;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inv, DynamicRegistryManager registryManager) {
        // Get food items
        ItemStack[] slots = new ItemStack[3];
        int j = 0;
        for (int i = 3; i <= 5; i++) {
            ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty()) {
                slots[j] = stack;
                j++;
            }
        }
        ItemStack[] food = new ItemStack[j];
        System.arraycopy(slots, 0, food, 0, food.length);

        ItemStack givenStack = resultItem.copy();
        NbtCompound compoundTag = givenStack.getOrCreateSubNbt(SandwichItem.SANDWICH_DATA);
        boolean[] associations = getAssociations(food);

        // Calculations
        float hunger = 0.0f;
        float saturationModifier = 0.0f;
        MutableText ingredientList = (MutableText) Text.of("");
        boolean hasGlint = false;
        for (int i = 0; i < food.length; i++) {
            hunger += FoodUtil.getHunger(food[i]) * (associations[i] ? hungerModifierBoosted : hungerModifierBase);
            saturationModifier += FoodUtil.getSaturationPoints(food[i]) * (associations[i] ? saturationModifierBoosted : saturationModifierBase);
            if (i != 0) ingredientList.append(", ");
            ingredientList.append(((MutableText) food[i].getName()).formatted(associations[i] ? Formatting.GREEN : Formatting.GRAY));
            if (food[i].getItem().hasGlint(food[i])) hasGlint = true;
        }

        // Transfer values to NBT
        compoundTag.putInt(SandwichItem.HUNGER, MathHelper.floor(hunger));
        compoundTag.putFloat(SandwichItem.SATURATION_MODIFIER, saturationModifier);
        compoundTag.putString(SandwichItem.INGREDIENT_LIST, Text.Serializer.toJson(ingredientList));
        compoundTag.putBoolean(SandwichItem.HAS_GLINT, hasGlint);
        return givenStack;
    }

    private boolean areAssociated(ItemStack item1, ItemStack item2) {
        boolean b = false;
        for (Ingredient ingredient : ingredientAssociations.keySet()) {
            if (ingredient.test(item1)) b = ingredientAssociations.get(ingredient).test(item2);
            if (ingredient.test(item2)) b |= ingredientAssociations.get(ingredient).test(item1);
        }
        return b;
    }

    private boolean[] getAssociations(ItemStack... items) {
        if (items.length == 1) {
            return new boolean[]{false};
        } else if (items.length == 2) {
            boolean b = areAssociated(items[0], items[1]);
            return new boolean[]{b, b};
        } else {
            boolean[] tab = new boolean[items.length];
            for (int i = 0; i < items.length; i++) {
                for (int j = i; j < items.length; j++) {
                    if (i != j) {
                        if (areAssociated(items[i], items[j])) {
                            tab[i] = true;
                            tab[j] = true;
                        }
                    }
                }
            }
            return tab;
        }
    }
}
