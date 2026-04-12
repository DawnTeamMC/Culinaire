package fr.hugman.culinaire.data.provider;

import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.data.recipe.SandwichRecipeJsonBuilder;
import fr.hugman.culinaire.data.recipe.TeaBagRecipeJsonBuilder;
import fr.hugman.culinaire.item.CulinaireItems;
import fr.hugman.culinaire.tag.CulinaireItemTags;
import fr.hugman.culinaire.tea.TeaTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.concurrent.CompletableFuture;

public class CulinaireRecipeGenerator extends RecipeProvider {
    public CulinaireRecipeGenerator(HolderLookup.Provider registries, RecipeOutput exporter) {
        super(registries, exporter);
    }

    @Override
    public void buildRecipes() {
        // CANDIES
        this.shapeless(RecipeCategory.FOOD, CulinaireItems.DARK_CHOCOLATE_BOTTLE)
                .requires(Items.SUGAR)
                .requires(Items.COCOA_BEANS)
                .requires(Items.GLASS_BOTTLE)
                .unlockedBy(getHasName(Items.GLASS_BOTTLE), this.has(Items.GLASS_BOTTLE))
                .unlockedBy(getHasName(Items.SUGAR), this.has(Items.SUGAR))
                .unlockedBy(getHasName(Items.COCOA_BEANS), this.has(Items.COCOA_BEANS))
                .save(this.output);
        this.shapeless(RecipeCategory.FOOD, CulinaireItems.MILK_CHOCOLATE_BOTTLE)
                .requires(Items.SUGAR)
                .requires(Items.COCOA_BEANS)
                .requires(CulinaireItems.MILK_BOTTLE)
                .unlockedBy(getHasName(CulinaireItems.MILK_BOTTLE), this.has(CulinaireItems.MILK_BOTTLE))
                .unlockedBy(getHasName(Items.SUGAR), this.has(Items.SUGAR))
                .unlockedBy(getHasName(Items.COCOA_BEANS), this.has(Items.COCOA_BEANS))
                .save(this.output);
        this.shapeless(RecipeCategory.FOOD, CulinaireItems.WHITE_CHOCOLATE_BOTTLE)
                .requires(Items.SUGAR)
                .requires(Items.GLASS_BOTTLE)
                .unlockedBy(getHasName(Items.GLASS_BOTTLE), this.has(Items.GLASS_BOTTLE))
                .unlockedBy(getHasName(Items.SUGAR), this.has(Items.SUGAR))
                .save(this.output);

        this.shapeless(RecipeCategory.FOOD, CulinaireItems.DARK_CHOCOLATE_BAR, 2)
                .requires(CulinaireItems.DARK_CHOCOLATE_BOTTLE)
                .unlockedBy(getHasName(CulinaireItems.DARK_CHOCOLATE_BOTTLE), this.has(CulinaireItems.DARK_CHOCOLATE_BOTTLE))
                .save(this.output);
        this.shapeless(RecipeCategory.FOOD, CulinaireItems.MILK_CHOCOLATE_BAR, 2)
                .requires(CulinaireItems.MILK_CHOCOLATE_BOTTLE)
                .unlockedBy(getHasName(CulinaireItems.MILK_CHOCOLATE_BOTTLE), this.has(CulinaireItems.MILK_CHOCOLATE_BOTTLE))
                .save(this.output);
        this.shapeless(RecipeCategory.FOOD, CulinaireItems.WHITE_CHOCOLATE_BAR, 2)
                .requires(CulinaireItems.WHITE_CHOCOLATE_BOTTLE)
                .unlockedBy(getHasName(CulinaireItems.WHITE_CHOCOLATE_BOTTLE), this.has(CulinaireItems.WHITE_CHOCOLATE_BOTTLE))
                .save(this.output);

        this.shapeless(RecipeCategory.FOOD, CulinaireItems.DARK_CHOCOLATE_PIE)
                .requires(CulinaireItems.DARK_CHOCOLATE_BOTTLE)
                .requires(CulinaireItems.DARK_CHOCOLATE_BOTTLE)
                .requires(Items.SUGAR)
                .requires(Items.EGG)
                .unlockedBy(getHasName(CulinaireItems.DARK_CHOCOLATE_BOTTLE), this.has(CulinaireItems.DARK_CHOCOLATE_BOTTLE))
                .save(this.output);
        this.shapeless(RecipeCategory.FOOD, CulinaireItems.MILK_CHOCOLATE_PIE)
                .requires(CulinaireItems.MILK_CHOCOLATE_BOTTLE)
                .requires(CulinaireItems.MILK_CHOCOLATE_BOTTLE)
                .requires(Items.SUGAR)
                .requires(Items.EGG)
                .unlockedBy(getHasName(CulinaireItems.MILK_CHOCOLATE_BOTTLE), this.has(CulinaireItems.MILK_CHOCOLATE_BOTTLE))
                .save(this.output);
        this.shapeless(RecipeCategory.FOOD, CulinaireItems.WHITE_CHOCOLATE_PIE)
                .requires(CulinaireItems.WHITE_CHOCOLATE_BOTTLE)
                .requires(CulinaireItems.WHITE_CHOCOLATE_BOTTLE)
                .requires(Items.SUGAR)
                .requires(Items.EGG)
                .unlockedBy(getHasName(CulinaireItems.WHITE_CHOCOLATE_BOTTLE), this.has(CulinaireItems.WHITE_CHOCOLATE_BOTTLE))
                .save(this.output);

        this.shapeless(RecipeCategory.FOOD, CulinaireItems.MARSHMALLOW)
                .requires(Items.SUGAR)
                .unlockedBy(getHasName(Items.SUGAR), this.has(Items.SUGAR))
                .save(this.output);

        this.shaped(RecipeCategory.FOOD, CulinaireItems.MARSHMALLOW_ON_A_STICK)
                .define('X', CulinaireItems.MARSHMALLOW)
                .define('I', Items.STICK)
                .pattern(" X")
                .pattern("I ")
                .unlockedBy(getHasName(CulinaireItems.MARSHMALLOW), this.has(CulinaireItems.MARSHMALLOW));
        this.shaped(RecipeCategory.FOOD, CulinaireBlocks.CHEESE_WHEEL)
                .define('#', CulinaireItems.CHEESE)
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(CulinaireItems.CHEESE), this.has(CulinaireItems.CHEESE))
                .save(this.output);

        // TEA
        this.offerTeaBagRecipe();
        this.shaped(RecipeCategory.FOOD, CulinaireBlocks.KETTLE)
                .define('T', Items.IRON_TRAPDOOR)
                .define('#', Items.IRON_INGOT)
                .pattern(" T ")
                .pattern("# #")
                .pattern("###")
                .unlockedBy(getHasName(Items.IRON_INGOT), this.has(Items.IRON_INGOT))
                .save(this.output);

        // PASTRIES
        //TODO: croissant
        this.shapeless(RecipeCategory.FOOD, CulinaireItems.CHOUQUETTE, 2)
                .requires(Items.SUGAR)
                .requires(Items.WHEAT)
                .unlockedBy(getHasName(CulinaireItems.CHOUQUETTE), this.has(CulinaireItems.CHOUQUETTE))
                .save(this.output);
        this.shapeless(RecipeCategory.FOOD, CulinaireItems.APPLE_PIE)
                .requires(Items.APPLE)
                .requires(Items.APPLE)
                .requires(Items.SUGAR)
                .requires(Items.EGG)
                .unlockedBy(getHasName(Items.APPLE), this.has(Items.APPLE))
                .save(this.output);
        this.shapeless(RecipeCategory.FOOD, CulinaireItems.SWEET_BERRY_PIE)
                .requires(Items.SWEET_BERRIES)
                .requires(Items.SWEET_BERRIES)
                .requires(Items.SWEET_BERRIES)
                .requires(Items.SUGAR)
                .requires(Items.EGG)
                .unlockedBy(getHasName(Items.SWEET_BERRIES), this.has(Items.SWEET_BERRIES))
                .save(this.output);

        // SANDWICHES
        this.offerSandwichRecipe();

        // MEALS
        this.shapeless(RecipeCategory.FOOD, CulinaireItems.SALAD)
                .requires(CulinaireItems.LETTUCE)
                .requires(CulinaireItems.TOMATO)
                .requires(CulinaireItems.CHEESE)
                .requires(Items.BOWL)
                .unlockedBy(getHasName(CulinaireItems.LETTUCE), this.has(CulinaireItems.LETTUCE))
                .save(this.output);
        this.shapeless(RecipeCategory.FOOD, CulinaireItems.MASHED_POTATOES)
                .requires(Items.BAKED_POTATO)
                .requires(Items.BAKED_POTATO)
                .requires(Items.BOWL)
                .unlockedBy(getHasName(Items.BAKED_POTATO), this.has(Items.BAKED_POTATO))
                .save(this.output);
    }

    public void offerTeaBagRecipe() {
        TeaBagRecipeJsonBuilder.create(registries, RecipeCategory.FOOD, Ingredient.of(Items.PAPER), Ingredient.of(Items.STRING), new ItemStack(CulinaireItems.TEA_BAG))
                .criterion(getHasName(Items.PAPER), this.has(Items.PAPER))
                .ingredient(TeaTypes.GREEN, CulinaireItemTags.GREEN_TEA_INGREDIENTS)
                .ingredient(TeaTypes.WHITE, CulinaireItemTags.WHITE_TEA_INGREDIENTS)
                //TODO: other tea types
                .ingredient(TeaTypes.ENDER, Items.POPPED_CHORUS_FRUIT) //TODO: tag
                .save(this.output);
    }


    public void offerSandwichRecipe() {
        SandwichRecipeJsonBuilder.create(
                        registries.lookupOrThrow(Registries.ITEM),
                        RecipeCategory.FOOD,
                        0.5f,
                        1.0f,
                        0.2f,
                        0.5f,
                        new ItemStack(CulinaireItems.SANDWICH)
                )
                .bread(CulinaireItemTags.SANDWICH_BREAD)
                .blacklist(CulinaireItemTags.SANDWICH_INGREDIENT_BLACKLIST)
                .association(Items.APPLE, CulinaireItems.MILK_CHOCOLATE_BAR)
                .association(Items.COOKED_CHICKEN, Items.HONEY_BOTTLE)
                .association(Items.COOKED_BEEF, CulinaireItems.CHEESE)
                .association(Items.GOLDEN_APPLE, Items.DRIED_KELP)
                .association(CulinaireItems.MARSHMALLOW, CulinaireItems.MILK_CHOCOLATE_BAR, Items.HONEY_BOTTLE)
                .association(Items.RABBIT, Items.BEETROOT)
                .association(Items.SPIDER_EYE, CulinaireItems.DARK_CHOCOLATE_BAR)
                .association(CulinaireItems.TOMATO, CulinaireItems.CHEESE, CulinaireItems.LETTUCE)
                .criterion("has_bread", this.has(CulinaireItemTags.SANDWICH_BREAD))
                .save(this.output);
    }

    public static FabricRecipeProvider create(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        return new FabricRecipeProvider(fabricDataOutput, completableFuture) {
            @Override
            protected RecipeProvider createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput recipeExporter) {
                return new CulinaireRecipeGenerator(wrapperLookup, recipeExporter);
            }

            @Override
            public String getName() {
                return "Recipes";
            }
        };
    }
}
