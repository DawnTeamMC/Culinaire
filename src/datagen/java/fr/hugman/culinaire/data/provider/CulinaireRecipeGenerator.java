package fr.hugman.culinaire.data.provider;

import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
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

        this.createShapeless(RecipeCategory.FOOD, CulinaireItems.SWEET_BERRY_PIE)
                .input(CulinaireItems.SWEET_BERRY_PIE)
                .input(CulinaireItems.SWEET_BERRY_PIE)
                .input(CulinaireItems.SWEET_BERRY_PIE)
                .input(Items.SUGAR)
                .input(Items.EGG)
                .criterion(hasItem(CulinaireItems.SWEET_BERRY_PIE), this.conditionsFromItem(CulinaireItems.SWEET_BERRY_PIE))
                .offerTo(this.exporter);
    }

    public void offerShapelessRecipe(RecipeCategory category, Item result, Item... ingredients) {
        var recipe = createShapeless(category, result);
        for (var ingredient : ingredients) {
            recipe.input(ingredient);
        }
        recipe.criterion(hasItem(ingredients[0]), this.conditionsFromItem(ingredients[0])).offerTo(this.exporter);
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
