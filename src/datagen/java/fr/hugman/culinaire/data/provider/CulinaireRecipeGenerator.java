package fr.hugman.culinaire.data.provider;

import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.data.recipe.SandwichRecipeJsonBuilder;
import fr.hugman.culinaire.data.recipe.TeaBagRecipeJsonBuilder;
import fr.hugman.culinaire.item.CulinaireItems;
import fr.hugman.culinaire.tag.CulinaireItemTags;
import fr.hugman.culinaire.tea.TeaTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class CulinaireRecipeGenerator extends RecipeGenerator {
    public CulinaireRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
    }

    @Override
    public void generate() {
        // CANDIES
        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.DARK_CHOCOLATE_BOTTLE)
                .input(Items.SUGAR)
                .input(Items.COCOA_BEANS)
                .input(Items.GLASS_BOTTLE)
                .criterion(hasItem(Items.GLASS_BOTTLE), this.conditionsFromItem(Items.GLASS_BOTTLE))
                .criterion(hasItem(Items.SUGAR), this.conditionsFromItem(Items.SUGAR))
                .criterion(hasItem(Items.COCOA_BEANS), this.conditionsFromItem(Items.COCOA_BEANS))
                .offerTo(this.exporter);
        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.MILK_CHOCOLATE_BOTTLE)
                .input(Items.SUGAR)
                .input(Items.COCOA_BEANS)
                .input(CulinaireItems.MILK_BOTTLE)
                .criterion(hasItem(CulinaireItems.MILK_BOTTLE), this.conditionsFromItem(CulinaireItems.MILK_BOTTLE))
                .criterion(hasItem(Items.SUGAR), this.conditionsFromItem(Items.SUGAR))
                .criterion(hasItem(Items.COCOA_BEANS), this.conditionsFromItem(Items.COCOA_BEANS))
                .offerTo(this.exporter);
        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.WHITE_CHOCOLATE_BOTTLE)
                .input(Items.SUGAR)
                .input(Items.GLASS_BOTTLE)
                .criterion(hasItem(Items.GLASS_BOTTLE), this.conditionsFromItem(Items.GLASS_BOTTLE))
                .criterion(hasItem(Items.SUGAR), this.conditionsFromItem(Items.SUGAR))
                .offerTo(this.exporter);

        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.DARK_CHOCOLATE_BAR, 2)
                .input(CulinaireItems.DARK_CHOCOLATE_BOTTLE)
                .criterion(hasItem(CulinaireItems.DARK_CHOCOLATE_BOTTLE), this.conditionsFromItem(CulinaireItems.DARK_CHOCOLATE_BOTTLE))
                .offerTo(this.exporter);
        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.MILK_CHOCOLATE_BAR, 2)
                .input(CulinaireItems.MILK_CHOCOLATE_BOTTLE)
                .criterion(hasItem(CulinaireItems.MILK_CHOCOLATE_BOTTLE), this.conditionsFromItem(CulinaireItems.MILK_CHOCOLATE_BOTTLE))
                .offerTo(this.exporter);
        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.WHITE_CHOCOLATE_BAR, 2)
                .input(CulinaireItems.WHITE_CHOCOLATE_BOTTLE)
                .criterion(hasItem(CulinaireItems.WHITE_CHOCOLATE_BOTTLE), this.conditionsFromItem(CulinaireItems.WHITE_CHOCOLATE_BOTTLE))
                .offerTo(this.exporter);

        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.DARK_CHOCOLATE_PIE)
                .input(CulinaireItems.DARK_CHOCOLATE_BOTTLE)
                .input(CulinaireItems.DARK_CHOCOLATE_BOTTLE)
                .input(Items.SUGAR)
                .input(Items.EGG)
                .criterion(hasItem(CulinaireItems.DARK_CHOCOLATE_BOTTLE), this.conditionsFromItem(CulinaireItems.DARK_CHOCOLATE_BOTTLE))
                .offerTo(this.exporter);
        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.MILK_CHOCOLATE_PIE)
                .input(CulinaireItems.MILK_CHOCOLATE_BOTTLE)
                .input(CulinaireItems.MILK_CHOCOLATE_BOTTLE)
                .input(Items.SUGAR)
                .input(Items.EGG)
                .criterion(hasItem(CulinaireItems.MILK_CHOCOLATE_BOTTLE), this.conditionsFromItem(CulinaireItems.MILK_CHOCOLATE_BOTTLE))
                .offerTo(this.exporter);
        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.WHITE_CHOCOLATE_PIE)
                .input(CulinaireItems.WHITE_CHOCOLATE_BOTTLE)
                .input(CulinaireItems.WHITE_CHOCOLATE_BOTTLE)
                .input(Items.SUGAR)
                .input(Items.EGG)
                .criterion(hasItem(CulinaireItems.WHITE_CHOCOLATE_BOTTLE), this.conditionsFromItem(CulinaireItems.WHITE_CHOCOLATE_BOTTLE))
                .offerTo(this.exporter);

        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.MARSHMALLOW)
                .input(Items.SUGAR)
                .criterion(hasItem(Items.SUGAR), this.conditionsFromItem(Items.SUGAR))
                .offerTo(this.exporter);

        this.createShaped(RecipeCategory.FOOD, CulinaireItems.MARSHMALLOW_ON_A_STICK)
                .input('X', CulinaireItems.MARSHMALLOW)
                .input('I', Items.STICK)
                .pattern(" X")
                .pattern("I ")
                .criterion(hasItem(CulinaireItems.MARSHMALLOW), this.conditionsFromItem(CulinaireItems.MARSHMALLOW));
        this.createShaped(RecipeCategory.FOOD, CulinaireBlocks.CHEESE_WHEEL)
                .input('#', CulinaireItems.CHEESE)
                .pattern("###")
                .pattern("###")
                .criterion(hasItem(CulinaireItems.CHEESE), this.conditionsFromItem(CulinaireItems.CHEESE))
                .offerTo(this.exporter);

        // TEA
        this.offerTeaBagRecipe();
        this.createShaped(RecipeCategory.FOOD, CulinaireBlocks.KETTLE)
                .input('T', Items.IRON_TRAPDOOR)
                .input('#', Items.IRON_INGOT)
                .pattern(" T ")
                .pattern("# #")
                .pattern("###")
                .criterion(hasItem(Items.IRON_INGOT), this.conditionsFromItem(Items.IRON_INGOT))
                .offerTo(this.exporter);

        // PASTRIES
        //TODO: croissant
        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.CHOUQUETTE, 2)
                .input(Items.SUGAR)
                .input(Items.WHEAT)
                .criterion(hasItem(CulinaireItems.CHOUQUETTE), this.conditionsFromItem(CulinaireItems.CHOUQUETTE))
                .offerTo(this.exporter);
        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.APPLE_PIE)
                .input(Items.APPLE)
                .input(Items.APPLE)
                .input(Items.SUGAR)
                .input(Items.EGG)
                .criterion(hasItem(Items.APPLE), this.conditionsFromItem(Items.APPLE))
                .offerTo(this.exporter);
        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.SWEET_BERRY_PIE)
                .input(Items.SWEET_BERRIES)
                .input(Items.SWEET_BERRIES)
                .input(Items.SWEET_BERRIES)
                .input(Items.SUGAR)
                .input(Items.EGG)
                .criterion(hasItem(Items.SWEET_BERRIES), this.conditionsFromItem(Items.SWEET_BERRIES))
                .offerTo(this.exporter);

        // SANDWICHES
        this.offerSandwichRecipe();

        // MEALS
        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.SALAD)
                .input(CulinaireItems.LETTUCE)
                .input(CulinaireItems.TOMATO)
                .input(CulinaireItems.CHEESE)
                .input(Items.BOWL)
                .criterion(hasItem(CulinaireItems.LETTUCE), this.conditionsFromItem(CulinaireItems.LETTUCE))
                .offerTo(this.exporter);
        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.MASHED_POTATOES)
                .input(Items.BAKED_POTATO)
                .input(Items.BAKED_POTATO)
                .input(Items.BOWL)
                .criterion(hasItem(Items.BAKED_POTATO), this.conditionsFromItem(Items.BAKED_POTATO))
                .offerTo(this.exporter);
    }

    public void offerTeaBagRecipe() {
        TeaBagRecipeJsonBuilder.create(registries, RecipeCategory.FOOD, Ingredient.ofItem(Items.PAPER), Ingredient.ofItem(Items.STRING), new ItemStack(CulinaireItems.TEA_BAG))
                .criterion(hasItem(Items.PAPER), this.conditionsFromItem(Items.PAPER))
                .ingredient(TeaTypes.GREEN, CulinaireItemTags.GREEN_TEA_INGREDIENTS)
                .ingredient(TeaTypes.WHITE, CulinaireItemTags.WHITE_TEA_INGREDIENTS)
                .offerTo(this.exporter);
    }


    public void offerSandwichRecipe() {
        SandwichRecipeJsonBuilder.create(
                        registries.getOrThrow(RegistryKeys.ITEM),
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
                .criterion("has_bread", this.conditionsFromTag(CulinaireItemTags.SANDWICH_BREAD))

                .offerTo(this.exporter);
    }

    public static FabricRecipeProvider create(FabricDataOutput fabricDataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        return new FabricRecipeProvider(fabricDataOutput, completableFuture) {
            @Override
            protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
                return new CulinaireRecipeGenerator(wrapperLookup, recipeExporter);
            }

            @Override
            public String getName() {
                return "Recipes";
            }
        };
    }
}
