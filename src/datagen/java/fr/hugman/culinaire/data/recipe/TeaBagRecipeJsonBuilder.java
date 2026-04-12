package fr.hugman.culinaire.data.recipe;

import fr.hugman.culinaire.recipe.TeaBagRecipe;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import fr.hugman.culinaire.tea.TeaType;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import java.util.LinkedHashMap;
import java.util.Map;

public class TeaBagRecipeJsonBuilder {
    private final HolderGetter<Item> items;
    private final HolderGetter<TeaType> teaTypes;

    private final RecipeCategory category;
    private final Ingredient paper;
    private final Ingredient string;
    private final Map<Holder<TeaType>, Ingredient> teaTypeIngredients = new LinkedHashMap<>();
    private final ItemStack result;

    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public TeaBagRecipeJsonBuilder(HolderGetter<Item> items, HolderGetter<TeaType> teaTypes, RecipeCategory category, Ingredient paper, Ingredient string, ItemStack result) {
        this.items = items;
        this.teaTypes = teaTypes;
        this.category = category;
        this.paper = paper;
        this.string = string;
        this.result = result;
    }

    public static TeaBagRecipeJsonBuilder create(HolderLookup.Provider registries, RecipeCategory category, Ingredient paper, Ingredient string, ItemStack result) {
        return new TeaBagRecipeJsonBuilder(registries.lookupOrThrow(Registries.ITEM), registries.lookupOrThrow(CulinaireRegistryKeys.TEA_TYPE), category, paper, string, result);
    }

    public TeaBagRecipeJsonBuilder criterion(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public TeaBagRecipeJsonBuilder ingredient(ResourceKey<TeaType> teaType, Item... items) {
        this.teaTypeIngredients.put(this.teaTypes.getOrThrow(teaType), Ingredient.of(items));
        return this;
    }

    public TeaBagRecipeJsonBuilder ingredient(ResourceKey<TeaType> teaType, TagKey<Item> tag) {
        this.teaTypeIngredients.put(this.teaTypes.getOrThrow(teaType), Ingredient.of(this.items.getOrThrow(tag)));
        return this;
    }

    public void save(RecipeOutput exporter) {
        this.save(exporter, ResourceKey.create(Registries.RECIPE, BuiltInRegistries.ITEM.getKey(this.result.getItem())));
    }

    public void save(RecipeOutput exporter, ResourceKey<Recipe<?>> recipeKey) {
        this.validate(recipeKey);
        Advancement.Builder builder = exporter.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey))
                .rewards(AdvancementRewards.Builder.recipe(recipeKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        TeaBagRecipe teaBagRecipe = new TeaBagRecipe(
                RecipeBuilder.determineBookCategory(this.category),
                this.paper,
                this.string,
                this.teaTypeIngredients,
                this.result
        );
        exporter.accept(recipeKey, teaBagRecipe, builder.build(recipeKey.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void validate(ResourceKey<Recipe<?>> recipeKey) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeKey.identifier());
        }
    }
}
