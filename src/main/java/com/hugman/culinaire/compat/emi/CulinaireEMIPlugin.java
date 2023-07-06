package com.hugman.culinaire.compat.emi;

import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.recipe.TeaBagMakingRecipe;
import com.hugman.culinaire.registry.content.TeaContent;
import com.hugman.culinaire.tea.TeaHelper;
import com.hugman.culinaire.tea.TeaType;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class CulinaireEMIPlugin implements EmiPlugin {
    public static final EmiRecipeCategory TEA_BREWING_CATEGORY =
            new EmiRecipeCategory(Culinaire.id("plugins/tea_brewing"), EmiStack.of(TeaContent.KETTLE)) {
                @Override
                public Text getName() {
                    return Text.translatable("rei_category.culinaire.tea_brewing");
                }
            };

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(TEA_BREWING_CATEGORY);
        registry.addWorkstation(TEA_BREWING_CATEGORY, EmiStack.of(TeaContent.KETTLE));

        registerTeaBagDisplays(registry);
        registerTeaBottleDisplays(registry);

        registry.setDefaultComparison(EmiStack.of(TeaContent.TEA_BAG), Comparison.compareNbt());
        registry.setDefaultComparison(EmiStack.of(TeaContent.TEA_BOTTLE), Comparison.compareNbt());

        registry.addRecipeHandler(TeaContent.KETTLE_SCREEN_HANDLER, new BrewingRecipeHandler());
    }

    private void registerTeaBagDisplays(EmiRegistry registry) {
        for (TeaType teaType : TeaHelper.getAllTypes()) {
            DefaultedList<EmiIngredient> inputs = DefaultedList.of();
            inputs.add(EmiIngredient.of(TeaBagMakingRecipe.PAPER));
            inputs.add(EmiIngredient.of(TeaBagMakingRecipe.STRING));
            EmiIngredient ingredient = EmiIngredient.of(teaType.getTag());
            EmiStack output = EmiStack.of(TeaHelper.appendTeaType(new ItemStack(TeaContent.TEA_BAG), teaType));
            if (!ingredient.isEmpty()) {
                inputs.add(ingredient);
                Identifier id = Culinaire.id(teaType.getStrength().getName() + "_" + teaType.getFlavor().getName() + "_tea_bag");
                registry.addRecipe(new EmiCraftingRecipe(inputs, output, id, true));
            }
            registry.addEmiStack(output);
        }
    }

    private void registerTeaBottleDisplays(EmiRegistry registry) {
        for (TeaType teaType : TeaHelper.getAllTypes()) {
            EmiIngredient input = EmiStack.of(TeaHelper.appendTeaType(new ItemStack(TeaContent.TEA_BAG), teaType));
            EmiStack output = EmiStack.of(TeaHelper.appendTeaType(new ItemStack(TeaContent.TEA_BOTTLE), teaType));
            Identifier id = Culinaire.id(teaType.getStrength().getName() + "_" + teaType.getFlavor().getName() + "_tea");
            registry.addRecipe(new TeaBrewingEmiRecipe(input, output, id, teaType.getFlavor().getColor()));
            registry.addEmiStack(output);
        }
    }
}
