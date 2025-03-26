package fr.hugman.culinaire.data.recipe;

import fr.hugman.culinaire.recipe.TeaBagRecipe;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import fr.hugman.culinaire.tea.TeaType;
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
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

import java.util.LinkedHashMap;
import java.util.Map;

public class TeaBagRecipeJsonBuilder {
    private final RegistryEntryLookup<Item> items;
    private final RegistryEntryLookup<TeaType> teaTypes;

    private final RecipeCategory category;
    private final Ingredient paper;
    private final Ingredient string;
    private final Map<RegistryEntry<TeaType>, Ingredient> teaTypeIngredients = new LinkedHashMap<>();
    private final ItemStack result;

    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();

    public TeaBagRecipeJsonBuilder(RegistryEntryLookup<Item> items, RegistryEntryLookup<TeaType> teaTypes, RecipeCategory category, Ingredient paper, Ingredient string, ItemStack result) {
        this.items = items;
        this.teaTypes = teaTypes;
        this.category = category;
        this.paper = paper;
        this.string = string;
        this.result = result;
    }

    public static TeaBagRecipeJsonBuilder create(RegistryWrapper.WrapperLookup registries, RecipeCategory category, Ingredient paper, Ingredient string, ItemStack result) {
        return new TeaBagRecipeJsonBuilder(registries.getOrThrow(RegistryKeys.ITEM), registries.getOrThrow(CulinaireRegistryKeys.TEA_TYPE), category, paper, string, result);
    }

    public TeaBagRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public TeaBagRecipeJsonBuilder ingredient(RegistryKey<TeaType> teaType, Item... items) {
        this.teaTypeIngredients.put(this.teaTypes.getOrThrow(teaType), Ingredient.ofItems(items));
        return this;
    }

    public TeaBagRecipeJsonBuilder ingredient(RegistryKey<TeaType> teaType, TagKey<Item> tag) {
        this.teaTypeIngredients.put(this.teaTypes.getOrThrow(teaType), Ingredient.fromTag(this.items.getOrThrow(tag)));
        return this;
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
        TeaBagRecipe teaBagRecipe = new TeaBagRecipe(
                CraftingRecipeJsonBuilder.toCraftingCategory(this.category),
                this.paper,
                this.string,
                this.teaTypeIngredients,
                this.result
        );
        exporter.accept(recipeKey, teaBagRecipe, builder.build(recipeKey.getValue().withPrefixedPath("recipes/" + this.category.getName() + "/")));
    }

    private void validate(RegistryKey<Recipe<?>> recipeKey) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeKey.getValue());
        }
    }
}
