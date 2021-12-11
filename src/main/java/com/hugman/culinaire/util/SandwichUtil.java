package com.hugman.culinaire.util;

import com.hugman.culinaire.init.CulinaireFoodBundle;
import com.hugman.culinaire.init.data.CulinaireTags;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class SandwichUtil {
	public static final double MAX_HUNGER = 1.0f;
	public static final double MIN_HUNGER = 0.5f;
	public static final double MAX_SATURATION = 0.5f;
	public static final double MIN_SATURATION = 0.2f;

	public static final Ingredient VALID_BREAD = Ingredient.fromTag(CulinaireTags.Items.SANDWICH_BREAD);
	public static final Ingredient INGREDIENT_BLACKLIST = Ingredient.fromTag(CulinaireTags.Items.SANDWICH_BLACKLIST);
	public static final Map<Item, List<Item>> INGREDIENT_COMPLEMENTS = Map.ofEntries(
			Map.entry(Items.APPLE, List.of(CulinaireFoodBundle.MILK_CHOCOLATE_BAR)),
			Map.entry(Items.CHICKEN, List.of(Items.HONEY_BOTTLE)),
			Map.entry(Items.COOKED_BEEF, List.of(CulinaireFoodBundle.CHEESE)),
			Map.entry(Items.ENCHANTED_GOLDEN_APPLE, List.of(Items.BEETROOT)),
			Map.entry(Items.GOLDEN_APPLE, List.of(Items.DRIED_KELP)),
			Map.entry(CulinaireFoodBundle.MARSHMALLOW, List.of(CulinaireFoodBundle.MILK_CHOCOLATE_BAR, Items.HONEY_BOTTLE)),
			Map.entry(Items.RABBIT, List.of(Items.BEETROOT)),
			Map.entry(Items.SPIDER_EYE, List.of(CulinaireFoodBundle.DARK_CHOCOLATE_BAR)),
			Map.entry(CulinaireFoodBundle.TOMATO, List.of(CulinaireFoodBundle.CHEESE, CulinaireFoodBundle.LETTUCE, Items.COOKED_CHICKEN))
	);

	public static float getSaturationModifier(boolean complement, Item itemA, Item itemB) {
		float s = itemA.getFoodComponent().getSaturationModifier() + itemB.getFoodComponent().getSaturationModifier();
		return (float) (complement ? s * MAX_SATURATION : s * MIN_SATURATION);
	}

	public static float getSaturationModifier(boolean[] booTab, Item... items) {
		return getSaturationModifier(booTab, Arrays.stream(items).mapToDouble(item -> Objects.requireNonNull(item.getFoodComponent()).getSaturationModifier()).toArray());
	}

	private static float getSaturationModifier(boolean[] b, double[] modifiers) {
		float s = 0.0f;
		for(int i = 0; i < modifiers.length; i++) {
			s += modifiers[i] * (b[i] ? MAX_SATURATION : MIN_SATURATION);
		}
		return s;
	}

	public static int getHunger(boolean complement, Item itemA, Item itemB) {
		int h = itemA.getFoodComponent().getHunger() + itemB.getFoodComponent().getHunger();
		return (int) (complement ? h * MAX_HUNGER : h * MIN_HUNGER);
	}

	public static int getHunger(boolean[] booTab, Item... items) {
		return getHunger(booTab, Arrays.stream(items).mapToInt(item -> Objects.requireNonNull(item.getFoodComponent()).getHunger()).toArray());
	}

	private static int getHunger(boolean[] b, int[] hungers) {
		float h = 0.0f;
		for(int i = 0; i < hungers.length; i++) {
			h += hungers[i] * (b[i] ? MAX_HUNGER : MIN_HUNGER);
		}
		return MathHelper.fastFloor(h);
	}

	public static boolean areComplements(Item item1, Item item2) {
		boolean b = false;
		if(INGREDIENT_COMPLEMENTS.get(item1) != null) b = INGREDIENT_COMPLEMENTS.get(item1).contains(item2);
		if(INGREDIENT_COMPLEMENTS.get(item2) != null) b |= INGREDIENT_COMPLEMENTS.get(item2).contains(item1);
		return b;
	}

	public static boolean[] areComplements(Item... items) {
		boolean[] tab = new boolean[items.length];
		for(int i = 0; i < items.length; i++) {
			for(int j = i; j < items.length; j++) {
				if(i != j && !tab[j]) {
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
