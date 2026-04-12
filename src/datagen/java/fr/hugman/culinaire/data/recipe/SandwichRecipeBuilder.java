package fr.hugman.culinaire.data.recipe;

import fr.hugman.culinaire.recipe.SandwichRecipe;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public class SandwichRecipeBuilder {
    private final HolderGetter<Item> registryLookup;
    private final RecipeCategory category;
    private Ingredient bread;
    private Ingredient ingredientBlacklist;
    private final float nutritionModifierBase;
    private final float nutritionModifierBoosted;
    private final float saturationModifierBase;
    private final float saturationModifierBoosted;
    private final Map<Ingredient, Ingredient> ingredientAssociations;
    private final ItemStackTemplate result;

    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public SandwichRecipeBuilder(
            HolderGetter<Item> registryLookup, RecipeCategory category,
            Ingredient bread,
            Ingredient ingredientBlacklist,
            float nutritionModifierBase,
            float nutritionModifierBoosted,
            float saturationModifierBase,
            float saturationModifierBoosted,
            Map<Ingredient, Ingredient> ingredientAssociations,
            ItemStackTemplate result
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

    public static SandwichRecipeBuilder create(HolderGetter<Item> registryLookup, RecipeCategory category, Ingredient bread, Ingredient ingredientBlacklist, float nutritionModifierBase, float nutritionModifierBoosted, float saturationModifierBase, float saturationModifierBoosted, ItemStackTemplate result) {
        return new SandwichRecipeBuilder(registryLookup, category, bread, ingredientBlacklist, nutritionModifierBase, nutritionModifierBoosted, saturationModifierBase, saturationModifierBoosted, new HashMap<>(), result);
    }

    public static SandwichRecipeBuilder create(HolderGetter<Item> registryLookup, RecipeCategory category, float nutritionModifierBase, float nutritionModifierBoosted, float saturationModifierBase, float saturationModifierBoosted, ItemStackTemplate result) {
        return new SandwichRecipeBuilder(registryLookup, category, null, null, nutritionModifierBase, nutritionModifierBoosted, saturationModifierBase, saturationModifierBoosted, new HashMap<>(), result);
    }

    public SandwichRecipeBuilder bread(TagKey<Item> tagKey) {
        this.bread = Ingredient.of(this.registryLookup.getOrThrow(tagKey));
        return this;
    }

    public SandwichRecipeBuilder bread(Item... items) {
        this.bread = Ingredient.of(items);
        return this;
    }

    public SandwichRecipeBuilder blacklist(TagKey<Item> tagKey) {
        this.ingredientBlacklist = Ingredient.of(this.registryLookup.getOrThrow(tagKey));
        return this;
    }

    public SandwichRecipeBuilder blacklist(Item... items) {
        this.ingredientBlacklist = Ingredient.of(items);
        return this;
    }

    public SandwichRecipeBuilder criterion(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public SandwichRecipeBuilder association(Ingredient ingredient, Ingredient others) {
        this.ingredientAssociations.put(ingredient, others);
        return this;
    }

    public SandwichRecipeBuilder association(Item ingredient, Item... others) {
        return this.association(Ingredient.of(ingredient), Ingredient.of(others));
    }

    public void save(RecipeOutput exporter) {
        this.save(exporter, RecipeBuilder.getDefaultRecipeId(this.result));
    }

    public void save(RecipeOutput exporter, ResourceKey<Recipe<?>> recipeKey) {
        this.validate(recipeKey);
        Advancement.Builder builder = exporter.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey))
                .rewards(AdvancementRewards.Builder.recipe(recipeKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        SandwichRecipe sandwichRecipe = new SandwichRecipe(
                RecipeBuilder.determineCraftingBookCategory(this.category),
                this.bread,
                this.ingredientBlacklist,
                this.nutritionModifierBase,
                this.nutritionModifierBoosted,
                this.saturationModifierBase,
                this.saturationModifierBoosted,
                this.ingredientAssociations,
                this.result
        );
        exporter.accept(recipeKey, sandwichRecipe, builder.build(recipeKey.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void validate(ResourceKey<Recipe<?>> recipeKey) {
        if (this.bread == null) {
            throw new IllegalStateException("No bread for recipe " + recipeKey.identifier());
        }
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeKey.identifier());
        }
    }
}
