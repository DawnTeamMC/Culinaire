package fr.hugman.culinaire.data.provider;

import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
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
