package fr.hugman.culinaire.recipe.sandwich;

import fr.hugman.culinaire.item.sandwich.SandwichIngredient;
import fr.hugman.culinaire.recipe.sandwich.ingredient.SandwichIngredientProvider;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SandwichCraftingRecipeBuilder implements RecipeBuilder {
    private final HolderGetter<Item> items;
    private final RecipeCategory category;
    private Ingredient bread;
    private final List<Ingredient> baseIngredients = new ArrayList();
    private final List<SandwichIngredientProvider> complements = new ArrayList();
    private int maxComplements = 2;
    private final ItemStackTemplate result;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    @Nullable
    private String group;

    private SandwichCraftingRecipeBuilder(
            HolderGetter<Item> items,
            RecipeCategory category,
            ItemStackTemplate result
    ) {
        this.category = category;
        this.items = items;
        this.result = result;
    }

    public static SandwichCraftingRecipeBuilder of(HolderGetter<Item> items, RecipeCategory category, ItemStackTemplate result) {
        return new SandwichCraftingRecipeBuilder(items, category, result);
    }

    public static SandwichCraftingRecipeBuilder of(HolderGetter<Item> items, RecipeCategory category, ItemLike result) {
        return new SandwichCraftingRecipeBuilder(items, category, new ItemStackTemplate(result.asItem(), 1));
    }

    public SandwichCraftingRecipeBuilder bread(TagKey<Item> tag) {
        return this.bread(Ingredient.of(this.items.getOrThrow(tag)));
    }

    public SandwichCraftingRecipeBuilder bread(ItemLike item) {
        return this.bread(Ingredient.of(item));
    }

    public SandwichCraftingRecipeBuilder bread(Ingredient ingredient) {
        this.bread = ingredient;
        return this;
    }

    public SandwichCraftingRecipeBuilder requires(TagKey<Item> tag) {
        return this.requires(Ingredient.of(this.items.getOrThrow(tag)));
    }

    public SandwichCraftingRecipeBuilder requires(ItemLike item) {
        return this.requires(item, 1);
    }

    public SandwichCraftingRecipeBuilder requires(ItemLike item, int count) {
        for (int i = 0; i < count; i++) {
            this.requires(Ingredient.of(item));
        }

        return this;
    }

    public SandwichCraftingRecipeBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    public SandwichCraftingRecipeBuilder requires(Ingredient ingredient, int count) {
        for (int i = 0; i < count; i++) {
            this.baseIngredients.add(ingredient);
        }

        return this;
    }

    public SandwichCraftingRecipeBuilder complement(ItemLike item, FoodProperties foodProperties) {
        this.complements.add(new SandwichIngredientProvider(Ingredient.of(item), SandwichIngredient.of(foodProperties)));
        return this;
    }

    public SandwichCraftingRecipeBuilder complement(SandwichIngredientProvider ingredient) {
        this.complements.add(ingredient);
        return this;
    }

    public SandwichCraftingRecipeBuilder maxComplements(int maxComplements) {
        this.maxComplements = maxComplements;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(this.result);
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        SandwichCraftingRecipe recipe = new SandwichCraftingRecipe(
                RecipeBuilder.createCraftingCommonInfo(true),
                RecipeBuilder.createCraftingBookInfo(this.category, this.group),
                this.bread,
                this.baseIngredients,
                this.complements,
                this.maxComplements,
                this.result
        );
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
    }
}
