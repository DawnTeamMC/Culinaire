package fr.hugman.culinaire.recipe;

import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.item.CulinaireItems;
import fr.hugman.culinaire.tea.TeaHelper;
import fr.hugman.culinaire.tea.TeaType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TeaBagMakingRecipe extends SpecialCraftingRecipe {
    public static final Ingredient PAPER = Ingredient.ofItems(Items.PAPER);
    public static final Ingredient STRING = Ingredient.ofItems(Items.STRING);

    public TeaBagMakingRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        boolean hasPaper = false;
        boolean hasString = false;
        List<TeaType> bagTeaTypes = new ArrayList<>();
        for (int j = 0; j < input.size(); ++j) {
            ItemStack stack = input.getStackInSlot(j);
            if (!stack.isEmpty()) {
                if (PAPER.test(stack)) {
                    if (hasPaper) {
                        return false;
                    }
                    hasPaper = true;
                } else if (STRING.test(stack)) {
                    if (hasString) {
                        return false;
                    }
                    hasString = true;
                } else {
                    List<TeaType> ingredientTeaTypes = TeaHelper.getIngredientTypes(world.getRegistryManager(), stack);
                    if (ingredientTeaTypes.isEmpty()) {
                        return false;
                    } else {
                        for (TeaType teaType1 : ingredientTeaTypes) {
                            if (bagTeaTypes.stream().anyMatch(teaType2 -> teaType1.getFlavor() == teaType2.getFlavor())) {
                                TeaType teaType2 = bagTeaTypes.stream().filter(t -> t.getFlavor() == teaType1.getFlavor()).findFirst().get();
                                TeaType newTeaType = new TeaType(TeaType.Strength.byPotency(teaType1.getStrength().getPotency() + teaType2.getStrength().getPotency()), teaType1.getFlavor());
                                if (newTeaType.isCorrect()) {
                                    bagTeaTypes.remove(teaType2);
                                    bagTeaTypes.add(newTeaType);
                                } else {
                                    return false;
                                }
                            } else {
                                bagTeaTypes.add(teaType1);
                            }
                        }
                    }
                }
            }
        }
        int totalStrength = 0;
        for (TeaType teaType : bagTeaTypes) {
            totalStrength = totalStrength + teaType.getStrength().getPotency();
        }
        return hasPaper && hasString && totalStrength >= 1 && totalStrength <= 3 && bagTeaTypes.size() <= 2;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack givenStack = new ItemStack(CulinaireItems.TEA_BAG);
        List<TeaType> bagTeaTypes = new ArrayList<>();
        for (int j = 0; j < input.size(); ++j) {
            ItemStack stack = input.getStackInSlot(j);
            if (!stack.isEmpty()) {
                List<TeaType> ingredientTeaTypes = TeaHelper.getIngredientTypes(registries, stack);
                if (!ingredientTeaTypes.isEmpty()) {
                    for (TeaType teaType1 : ingredientTeaTypes) {
                        if (bagTeaTypes.stream().anyMatch(teaType2 -> teaType1.getFlavor() == teaType2.getFlavor())) {
                            TeaType teaType2 = bagTeaTypes.stream().filter(t -> t.getFlavor() == teaType1.getFlavor()).findFirst().get();
                            TeaType.Strength strength = TeaType.Strength.byPotency(teaType1.getStrength().getPotency() + teaType2.getStrength().getPotency());
                            bagTeaTypes.remove(teaType2);
                            bagTeaTypes.add(new TeaType(strength, teaType1.getFlavor()));
                        } else {
                            bagTeaTypes.add(teaType1);
                        }
                    }
                }
            }
        }
        givenStack.set(CulinaireComponentTypes.TEA_CONTENTS, bagTeaTypes);
        return givenStack;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return CulinaireRecipeSerializers.TEA_BAG_MAKING;
    }
}
