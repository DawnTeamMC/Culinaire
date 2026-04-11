package fr.hugman.culinaire.data.recipe;

import fr.hugman.culinaire.recipe.SandwichRecipe;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SandwichRecipeJsonBuilder {
    private final RegistryEntryLookup<Item> registryLookup;
    private final RecipeCategory category;
    private Ingredient bread;
    private Ingredient ingredientBlacklist;
    private final float nutritionModifierBase;
    private final float nutritionModifierBoosted;
    private final float saturationModifierBase;
    private final float saturationModifierBoosted;
    private final Map<Ingredient, Ingredient> ingredientAssociations;
    private final ItemStack result;

    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();

    public SandwichRecipeJsonBuilder(
            RegistryEntryLookup<Item> registryLookup, RecipeCategory category,
            Ingredient bread,
            Ingredient ingredientBlacklist,
            float nutritionModifierBase,
            float nutritionModifierBoosted,
            float saturationModifierBase,
            float saturationModifierBoosted,
            Map<Ingredient, Ingredient> ingredientAssociations,
            ItemStack result
    ) {
        this.registryLookup = registryLookup;
        this.category = category;
        this.bread = bread;
        this.ingredientBlacklist = ingredientBlacklist;
        this.nutritionModifierBase = nutritionModifierBase;
        this.nutritionModifierBoosted = nutritionModifierBoosted;
        this.saturationModifierBase = saturationModifierBase;
        this.saturationModifierBoosted = saturationModifierBoosted;
        this.ingredientAssociations = ingredientAssociations;
        this.result = result;
    }

    public static SandwichRecipeJsonBuilder create(RegistryEntryLookup<Item> registryLookup, RecipeCategory category, Ingredient bread, Ingredient ingredientBlacklist, float nutritionModifierBase, float nutritionModifierBoosted, float saturationModifierBase, float saturationModifierBoosted, ItemStack result) {
        return new SandwichRecipeJsonBuilder(registryLookup, category, bread, ingredientBlacklist, nutritionModifierBase, nutritionModifierBoosted, saturationModifierBase, saturationModifierBoosted, new HashMap<>(), result);
    }

    public static SandwichRecipeJsonBuilder create(RegistryEntryLookup<Item> registryLookup, RecipeCategory category, float nutritionModifierBase, float nutritionModifierBoosted, float saturationModifierBase, float saturationModifierBoosted, ItemStack result) {
        return new SandwichRecipeJsonBuilder(registryLookup, category, null, null, nutritionModifierBase, nutritionModifierBoosted, saturationModifierBase, saturationModifierBoosted, new HashMap<>(), result);
    }

    public SandwichRecipeJsonBuilder bread(TagKey<Item> tagKey) {
        this.bread = Ingredient.fromTag(this.registryLookup.getOrThrow(tagKey));
        return this;
    }

    public SandwichRecipeJsonBuilder bread(Item... items) {
        this.bread = Ingredient.ofItems(items);
        return this;
    }

    public SandwichRecipeJsonBuilder blacklist(TagKey<Item> tagKey) {
        this.ingredientBlacklist = Ingredient.fromTag(this.registryLookup.getOrThrow(tagKey));
        return this;
    }

    public SandwichRecipeJsonBuilder blacklist(Item... items) {
        this.ingredientBlacklist = Ingredient.ofItems(items);
        return this;
    }

    public SandwichRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public SandwichRecipeJsonBuilder association(Ingredient ingredient, Ingredient others) {
        this.ingredientAssociations.put(ingredient, others);
        return this;
    }

    public SandwichRecipeJsonBuilder association(Item ingredient, Item... others) {
        return this.association(Ingredient.ofItems(ingredient), Ingredient.ofItems(others));
    }

    public void offerTo(RecipeExporter exporter) {
        this.offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, Registries.ITEM.getId(this.result.getItem())));
    }

    public void offerTo(RecipeExporter exporter, RegistryKey<Recipe<?>> recipeKey) {
        this.validate(recipeKey);
        Advancement.Builder builder = exporter.getAdvancementBuilder()
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeKey))
                .rewards(AdvancementRewards.Builder.recipe(recipeKey))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
        this.criteria.forEach(builder::criterion);
        SandwichRecipe sandwichRecipe = new SandwichRecipe(
                CraftingRecipeJsonBuilder.toCraftingCategory(this.category),
                this.bread,
                this.ingredientBlacklist,
                this.nutritionModifierBase,
                this.nutritionModifierBoosted,
                this.saturationModifierBase,
                this.saturationModifierBoosted,
                this.ingredientAssociations,
                this.result
        );
        exporter.accept(recipeKey, sandwichRecipe, builder.build(recipeKey.getValue().withPrefixedPath("recipes/" + this.category.getName() + "/")));
    }

    private void validate(RegistryKey<Recipe<?>> recipeKey) {
        if (this.bread == null) {
            throw new IllegalStateException("No bread for recipe " + recipeKey.getValue());
        }
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeKey.getValue());
        }
    }
}
