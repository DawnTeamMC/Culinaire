package com.hugman.culinaire.objects.recipe;

import com.hugman.culinaire.init.TeaBundle;
import com.hugman.culinaire.objects.tea.TeaPotency;
import com.hugman.culinaire.objects.tea.TeaType;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TeaRecipe extends SpecialCraftingRecipe {
	public final DefaultedList<Ingredient> baseIngredients;
	public final ItemStack resultStack;

	public TeaRecipe(Identifier id, DefaultedList<Ingredient> baseIngredients, ItemStack resultStack) {
		super(id);
		this.baseIngredients = baseIngredients;
		this.resultStack = resultStack;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TeaBundle.TEA_CRAFTING;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 3;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		boolean[] hasIngredients = new boolean[baseIngredients.size()];
		List<TeaType> bagTeaTypes = new ArrayList<>();
		baseLoop:
		for(int i = 0; i < inv.size(); ++i) {
			ItemStack stack = inv.getStack(i);
			if(!stack.isEmpty()) {
				for(int j = 0; j < baseIngredients.size(); ++j) {
					if(baseIngredients.get(j).test(stack)) {
						if(hasIngredients[j]) {
							return false;
						}
						hasIngredients[j] = true;
						continue baseLoop;
					}
				}
				List<TeaType> teaTypes = TeaType.getTypesOf(stack.getItem());
				if(teaTypes.isEmpty()) {
					return false;
				}
				else {
					for(TeaType teaType1 : teaTypes) {
						List<TeaType> sameTypes = bagTeaTypes.stream().filter(t -> t.flavor() == teaType1.flavor()).toList();
						if(!sameTypes.isEmpty()) {
							TeaType teaType2 = sameTypes.get(0);
							TeaPotency potency = teaType1.flavor().getPotency(teaType1.potency().value() + teaType2.potency().value());
							if(potency == null) return false;
							TeaType newTeaType = new TeaType(teaType1.flavor(), potency);
							bagTeaTypes.remove(teaType2);
							bagTeaTypes.add(newTeaType);
						}
						else {
							bagTeaTypes.add(teaType1);
						}
					}
				}
			}
		}
		int totalPotency = 0;
		for(TeaType teaType : bagTeaTypes) {
			totalPotency = totalPotency + teaType.potency().value();
		}

		for(boolean hasIngredient : hasIngredients) {
			if(!hasIngredient) {
				return false;
			}
		}

		return totalPotency >= 1 && totalPotency <= 3 && bagTeaTypes.size() <= 2;
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack givenStack = new ItemStack(TeaBundle.TEA_BAG);
		List<TeaType> bagTeaTypes = new ArrayList<>();
		for(int j = 0; j < inv.size(); ++j) {
			ItemStack stack = inv.getStack(j);
			if(!stack.isEmpty()) {
				List<TeaType> teaTypes = TeaType.getTypesOf(stack.getItem());
				if(!teaTypes.isEmpty()) {
					for(TeaType teaType1 : teaTypes) {
						List<TeaType> sameTypes = bagTeaTypes.stream().filter(t -> t.flavor() == teaType1.flavor()).toList();
						if(!sameTypes.isEmpty()) {
							TeaType teaType2 = sameTypes.get(0);
							TeaPotency potency = teaType1.flavor().getPotency(teaType1.potency().value() + teaType2.potency().value());
							TeaType newTeaType = new TeaType(teaType1.flavor(), potency);
							bagTeaTypes.remove(teaType2);
							bagTeaTypes.add(newTeaType);
						}
						else {
							bagTeaTypes.add(teaType1);
						}
					}
				}
			}
		}
		TeaType.addToStack(givenStack, bagTeaTypes);
		return givenStack;
	}
}
