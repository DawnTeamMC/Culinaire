package fr.hugman.culinaire.recipe.sandwich;

import fr.hugman.culinaire.item.sandwich.SandwichIngredient;
import fr.hugman.culinaire.recipe.sandwich.ingredient.SandwichCraftingIngredient;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
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
import java.util.Objects;

public class SandwichRecipeBuilder implements RecipeBuilder {
    private final HolderGetter<Item> items;
    private final RecipeCategory category;
    private Ingredient bread;
    private final List<Ingredient> baseIngredients = new ArrayList();
    private final List<SandwichCraftingIngredient> complements = new ArrayList();
    private int maxComplements = 2;
    private final ItemStackTemplate result;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    @Nullable
    private String group;

    private SandwichRecipeBuilder(
            HolderGetter<Item> items,
            RecipeCategory category,
            ItemStackTemplate result
    ) {
        this.category = category;
        this.items = items;
        this.result = result;
    }

    public static SandwichRecipeBuilder of(HolderGetter<Item> items, RecipeCategory category, ItemStackTemplate result) {
        return new SandwichRecipeBuilder(items, category, result);
    }

    public static SandwichRecipeBuilder of(HolderGetter<Item> items, RecipeCategory category, ItemLike result) {
        return new SandwichRecipeBuilder(items, category, new ItemStackTemplate(result.asItem(), 1));
    }

    public SandwichRecipeBuilder bread(TagKey<Item> tag) {
        return this.bread(Ingredient.of(this.items.getOrThrow(tag)));
    }

    public SandwichRecipeBuilder bread(ItemLike item) {
        return this.bread(Ingredient.of(item));
    }

    public SandwichRecipeBuilder bread(Ingredient ingredient) {
        this.bread = ingredient;
        return this;
    }

    public SandwichRecipeBuilder requires(TagKey<Item> tag) {
        return this.requires(Ingredient.of(this.items.getOrThrow(tag)));
    }

    public SandwichRecipeBuilder requires(ItemLike item) {
        return this.requires(item, 1);
    }

    public SandwichRecipeBuilder requires(ItemLike item, int count) {
        for (int i = 0; i < count; i++) {
            this.requires(Ingredient.of(item));
        }

        return this;
    }

    public SandwichRecipeBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    public SandwichRecipeBuilder requires(Ingredient ingredient, int count) {
        for (int i = 0; i < count; i++) {
            this.baseIngredients.add(ingredient);
        }

        return this;
    }

    public SandwichRecipeBuilder complement(ItemLike item, FoodProperties foodProperties) {
        this.complements.add(new SandwichCraftingIngredient(Ingredient.of(item), SandwichIngredient.of(foodProperties)));
        return this;
    }

    public SandwichRecipeBuilder complement(SandwichCraftingIngredient ingredient) {
        this.complements.add(ingredient);
        return this;
    }

    public SandwichRecipeBuilder maxComplements(int maxComplements) {
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
        SandwichRecipe recipe = new SandwichRecipe(
                RecipeBuilder.createCraftingCommonInfo(true),
                new SandwichRecipe.BookInfo(RecipeBuilder.determineCraftingBookCategory(this.category), Objects.requireNonNullElse(this.group, "")),
                this.bread,
                this.baseIngredients,
                this.complements,
                this.maxComplements,
                this.result
        );
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
    }
}
