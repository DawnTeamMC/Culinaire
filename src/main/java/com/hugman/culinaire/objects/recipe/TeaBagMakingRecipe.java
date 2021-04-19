package com.hugman.culinaire.objects.recipe;

import com.hugman.culinaire.init.CulinaireItems;
import com.hugman.culinaire.objects.item.tea.TeaHelper;
import com.hugman.culinaire.objects.item.tea.TeaType;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TeaBagMakingRecipe extends SpecialCraftingRecipe {
	private static final Ingredient PAPER = Ingredient.ofItems(Items.PAPER);
	private static final Ingredient STRING = Ingredient.ofItems(Items.STRING);

	public TeaBagMakingRecipe(Identifier id) {
		super(id);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CulinaireItems.TEA_BAG_MAKING;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 3;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		boolean hasPaper = false;
		boolean hasString = false;
		List<TeaType> bagTeaTypes = new ArrayList<>();
		for(int j = 0; j < inv.size(); ++j) {
			ItemStack stack = inv.getStack(j);
			if(!stack.isEmpty()) {
				if(PAPER.test(stack)) {
					if(hasPaper) {
						return false;
					}
					hasPaper = true;
				}
				else if(STRING.test(stack)) {
					if(hasString) {
						return false;
					}
					hasString = true;
				}
				else {
					List<TeaType> ingredientTeaTypes = TeaHelper.getIngredientTypes(stack);
					if(ingredientTeaTypes.isEmpty()) {
						return false;
					}
					else {
						for(TeaType teaType1 : ingredientTeaTypes) {
							if(bagTeaTypes.stream().anyMatch(teaType2 -> teaType1.getFlavor() == teaType2.getFlavor())) {
								TeaType teaType2 = bagTeaTypes.stream().filter(t -> t.getFlavor() == teaType1.getFlavor()).findFirst().get();
								TeaType newTeaType = new TeaType(TeaType.Strength.byPotency(teaType1.getStrength().getPotency() + teaType2.getStrength().getPotency()), teaType1.getFlavor());
								if(newTeaType.isCorrect()) {
									bagTeaTypes.remove(teaType2);
									bagTeaTypes.add(newTeaType);
								}
								else {
									return false;
								}
							}
							else {
								bagTeaTypes.add(teaType1);
							}
						}
					}
				}
			}
		}
		int totalStrength = 0;
		for(TeaType teaType : bagTeaTypes) {
			totalStrength = totalStrength + teaType.getStrength().getPotency();
		}
		return hasPaper && hasString && totalStrength >= 1 && totalStrength <= 3 && bagTeaTypes.size() <= 2;
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack givenStack = new ItemStack(CulinaireItems.TEA_BAG);
		List<TeaType> bagTeaTypes = new ArrayList<>();
		for(int j = 0; j < inv.size(); ++j) {
			ItemStack stack = inv.getStack(j);
			if(!stack.isEmpty()) {
				List<TeaType> ingredientTeaTypes = TeaHelper.getIngredientTypes(stack);
				if(!ingredientTeaTypes.isEmpty()) {
					for(TeaType teaType1 : ingredientTeaTypes) {
						if(bagTeaTypes.stream().anyMatch(teaType2 -> teaType1.getFlavor() == teaType2.getFlavor())) {
							TeaType teaType2 = bagTeaTypes.stream().filter(t -> t.getFlavor() == teaType1.getFlavor()).findFirst().get();
							TeaType.Strength strength = TeaType.Strength.byPotency(teaType1.getStrength().getPotency() + teaType2.getStrength().getPotency());
							bagTeaTypes.remove(teaType2);
							bagTeaTypes.add(new TeaType(strength, teaType1.getFlavor()));
						}
						else {
							bagTeaTypes.add(teaType1);
						}
					}
				}
			}
		}
		givenStack = TeaHelper.appendTeaTypes(givenStack, bagTeaTypes);
		return givenStack;
	}
}
