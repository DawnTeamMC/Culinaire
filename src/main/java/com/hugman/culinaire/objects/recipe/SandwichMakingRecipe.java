package com.hugman.culinaire.objects.recipe;

import com.hugman.culinaire.init.FoodBundle;
import com.hugman.culinaire.init.data.CulinaireTags;
import com.hugman.culinaire.objects.item.SandwichItem;
import com.hugman.culinaire.util.FoodUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class SandwichMakingRecipe extends SpecialCraftingRecipe {
	private static final double MAX_HUNGER = 1.0f;
	private static final double MIN_HUNGER = 0.5f;
	private static final double MAX_SATURATION = 0.5f;
	private static final double MIN_SATURATION = 0.2f;

	private static final Ingredient VALID_BREAD = Ingredient.fromTag(CulinaireTags.Items.SANDWICH_BREAD);
	private static final Ingredient INGREDIENT_BLACKLIST = Ingredient.fromTag(CulinaireTags.Items.SANDWICH_BLACKLIST);
	private static final Map<Item, List<Item>> INGREDIENT_COMPLEMENTS = Map.ofEntries(
			Map.entry(Items.APPLE, List.of(FoodBundle.MILK_CHOCOLATE_BAR)),
			Map.entry(Items.COOKED_CHICKEN, List.of(Items.HONEY_BOTTLE)),
			Map.entry(Items.COOKED_BEEF, List.of(FoodBundle.CHEESE)),
			Map.entry(Items.GOLDEN_APPLE, List.of(Items.DRIED_KELP)),
			Map.entry(FoodBundle.MARSHMALLOW, List.of(FoodBundle.MILK_CHOCOLATE_BAR, Items.HONEY_BOTTLE)),
			Map.entry(Items.RABBIT, List.of(Items.BEETROOT)),
			Map.entry(Items.SPIDER_EYE, List.of(FoodBundle.DARK_CHOCOLATE_BAR)),
			Map.entry(FoodBundle.TOMATO, List.of(FoodBundle.CHEESE, FoodBundle.LETTUCE, Items.COOKED_CHICKEN))
	);
	
	public SandwichMakingRecipe(Identifier identifier) {
		super(identifier);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return FoodBundle.SANDWICH_MAKING;
	}

	@Override
	public ItemStack getOutput() {
		return new ItemStack(FoodBundle.SANDWICH);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public boolean fits(int width, int height) {
		return width == 3 && height == 3;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		boolean hasBread = false;
		boolean hasOnlyIngredients = false;
		int[] emptySlots = new int[]{0, 2, 6, 8};
		for(int emptySlot : emptySlots) {
			ItemStack itemStack = inv.getStack(emptySlot);
			if(!itemStack.isEmpty()) {
				return false;
			}
		}
		ItemStack topMiddleStack = inv.getStack(1);
		ItemStack bottomMiddleStack = inv.getStack(7);
		if(!topMiddleStack.isEmpty() && !bottomMiddleStack.isEmpty()) {
			if(VALID_BREAD.test(topMiddleStack) && VALID_BREAD.test(bottomMiddleStack)) {
				hasBread = true;
			}
		}
		for(int i = 3; i < 6; ++i) {
			ItemStack itemStack = inv.getStack(i);
			if(!itemStack.isEmpty()) {
				if(itemStack.isFood() && !INGREDIENT_BLACKLIST.test(itemStack)) {
					hasOnlyIngredients = true;
				}
				else {
					return false;
				}
			}
		}
		return hasBread && hasOnlyIngredients;
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		// Get food items
		ItemStack[] slots = new ItemStack[3];
		int j = 0;
		for(int i = 3; i <= 5; i++) {
			ItemStack stack = inv.getStack(i);
			if(!stack.isEmpty()) {
				slots[j] = stack;
				j++;
			}
		}
		ItemStack[] food = new ItemStack[j];
		System.arraycopy(slots, 0, food, 0, food.length);

		ItemStack givenStack = new ItemStack(FoodBundle.SANDWICH);
		NbtCompound compoundTag = givenStack.getOrCreateSubNbt(SandwichItem.SANDWICH_DATA);
		boolean[] complements = getComplements(food);

		// Calculations
		float hunger = 0.0f;
		float saturationModifier = 0.0f;
		MutableText ingredientList = (MutableText) Text.of("");
		boolean hasGlint = false;
		for(int i = 0; i < food.length; i++) {
			hunger += FoodUtil.getHunger(food[i]) * (complements[i] ? MAX_HUNGER : MIN_HUNGER);
			saturationModifier += FoodUtil.getSaturationPoints(food[i]) * (complements[i] ? MAX_SATURATION : MIN_SATURATION);
			if(i != 0) ingredientList.append(", ");
			ingredientList.append(((MutableText)food[i].getName()).formatted(complements[i] ? Formatting.GREEN : Formatting.GRAY));
			if(food[i].getItem().hasGlint(food[i])) hasGlint = true;
		}

		// Transfer values to NBT
		compoundTag.putInt(SandwichItem.HUNGER, MathHelper.fastFloor(hunger));
		compoundTag.putFloat(SandwichItem.SATURATION_MODIFIER, saturationModifier);
		compoundTag.putString(SandwichItem.INGREDIENT_LIST, Text.Serializer.toJson(ingredientList));
		compoundTag.putBoolean(SandwichItem.HAS_GLINT, hasGlint);
		return givenStack;
	}

	private static boolean areComplements(ItemStack item1, ItemStack item2) {
		boolean b = false;
		if(INGREDIENT_COMPLEMENTS.get(item1.getItem()) != null) b = INGREDIENT_COMPLEMENTS.get(item1.getItem()).contains(item2.getItem());
		if(INGREDIENT_COMPLEMENTS.get(item2.getItem()) != null) b |= INGREDIENT_COMPLEMENTS.get(item2.getItem()).contains(item1.getItem());
		return b;
	}

	private static boolean[] getComplements(ItemStack... items) {
		if(items.length == 1) {
			return new boolean[] {false};
		}
		else if(items.length == 2) {
			boolean b = areComplements(items[0], items[1]);
			return new boolean[] {b, b};
		}
		else {
			boolean[] tab = new boolean[items.length];
			for(int i = 0; i < items.length; i++) {
				for(int j = i; j < items.length; j++) {
					if(i != j) {
						if(areComplements(items[i] , items[j])) {
							tab[i] = true;
							tab[j] = true;
						}
					}
				}
			}
			return tab;
		}
	}
}
