package com.hugman.culinaire.objects.recipe;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.hugman.culinaire.init.CulinaireFoodBundle;
import com.hugman.culinaire.init.data.CulinaireTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class SandwichMakingRecipe extends SpecialCraftingRecipe {
	public static final ListMultimap<Item, Item> COMPLEMENTS = ArrayListMultimap.create();
	private static final Ingredient BREAD = Ingredient.fromTag(CulinaireTags.Items.SANDWICH_BREAD);
	private static final Ingredient BLACKLIST = Ingredient.fromTag(CulinaireTags.Items.SANDWICH_BLACKLIST);

	public SandwichMakingRecipe(Identifier identifier) {
		super(identifier);
		COMPLEMENTS.put(Items.APPLE, CulinaireFoodBundle.CHOCOLATE);
		COMPLEMENTS.put(Items.CHICKEN, Items.HONEY_BOTTLE);
		COMPLEMENTS.put(Items.COOKED_BEEF, CulinaireFoodBundle.CHEESE);
		COMPLEMENTS.put(Items.ENCHANTED_GOLDEN_APPLE, Items.BEETROOT);
		COMPLEMENTS.put(Items.GOLDEN_APPLE, Items.DRIED_KELP);
		COMPLEMENTS.put(CulinaireFoodBundle.MARSHMALLOW, CulinaireFoodBundle.CHOCOLATE);
		COMPLEMENTS.put(CulinaireFoodBundle.MARSHMALLOW, Items.HONEY_BOTTLE);
		COMPLEMENTS.put(Items.RABBIT, Items.BEETROOT);
		COMPLEMENTS.put(Items.SPIDER_EYE, CulinaireFoodBundle.CHOCOLATE);
		COMPLEMENTS.put(CulinaireFoodBundle.TOMATO, CulinaireFoodBundle.CHEESE);
		COMPLEMENTS.put(CulinaireFoodBundle.TOMATO, CulinaireFoodBundle.LETTUCE);
		COMPLEMENTS.put(CulinaireFoodBundle.TOMATO, Items.COOKED_CHICKEN);
	}

	public ItemStack getOutput() {
		return new ItemStack(CulinaireFoodBundle.SANDWICH);
	}

	public boolean matches(CraftingInventory inv, World world) {
		boolean hasBread = false;
		boolean hasAnIngredient = false;
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
			if(BREAD.test(topMiddleStack) && BREAD.test(bottomMiddleStack)) {
				hasBread = true;
			}
		}
		for(int i = 3; i < 6; ++i) {
			ItemStack itemStack = inv.getStack(i);
			if(!itemStack.isEmpty()) {
				if(itemStack.isFood() && !BLACKLIST.test(itemStack)) {
					hasAnIngredient = true;
				}
				else {
					return false;
				}
			}
		}
		return hasBread && hasAnIngredient;
	}

	public boolean areComplements(Item item1, Item item2) {
		return COMPLEMENTS.get(item1).contains(item2) || COMPLEMENTS.get(item2).contains(item1);
	}

	public ItemStack craft(CraftingInventory inv) {
		ItemStack givenStack = new ItemStack(CulinaireFoodBundle.SANDWICH);
		int hunger = 0;
		float saturation = 0;
		Item item1 = inv.getStack(3).getItem();
		Item item2 = inv.getStack(4).getItem();
		Item item3 = inv.getStack(5).getItem();
		NbtCompound ingredientTag1 = new NbtCompound();
		NbtCompound ingredientTag2 = new NbtCompound();
		NbtCompound ingredientTag3 = new NbtCompound();
		NbtList listTag = new NbtList();
		int hunger1 = 0;
		float saturation1 = 0;
		int hunger2 = 0;
		float saturation2 = 0;
		int hunger3 = 0;
		float saturation3 = 0;
		if(item1.isFood()) {
			hunger1 = item1.getFoodComponent().getHunger() / 3;
			saturation1 = item1.getFoodComponent().getSaturationModifier() / 3;
			ingredientTag1.putString("Item", Registry.ITEM.getId(item1).toString());
			listTag.add(ingredientTag1);
		}
		if(item2.isFood()) {
			hunger2 = item2.getFoodComponent().getHunger() / 3;
			saturation2 = item2.getFoodComponent().getSaturationModifier() / 3;
			ingredientTag2.putString("Item", Registry.ITEM.getId(item2).toString());
			listTag.add(ingredientTag2);
		}
		if(item3.isFood()) {
			hunger3 = item3.getFoodComponent().getHunger() / 3;
			saturation3 = item3.getFoodComponent().getSaturationModifier() / 3;
			ingredientTag3.putString("Item", Registry.ITEM.getId(item3).toString());
			listTag.add(ingredientTag3);
		}
		if(item1.isFood()) {
			if(item2.isFood()) {
				if(item3.isFood()) {
					boolean b1 = areComplements(item1, item2) && areComplements(item1, item3);
					boolean b2 = areComplements(item1, item3) && areComplements(item2, item3);
					boolean b3 = areComplements(item1, item2) && areComplements(item2, item3);
					if(b1 || b2 || b3) {
						// 1, 2, 3 present
						// 1, 2, 3 complements
						hunger = (hunger1 + hunger2 + hunger3) * 2;
						saturation = (saturation1 + saturation2 + saturation3) * 2;
						ingredientTag1.putBoolean("Complementary", true);
						ingredientTag2.putBoolean("Complementary", true);
						ingredientTag3.putBoolean("Complementary", true);
					}
					else if(areComplements(item1, item2)) {
						// 1, 2, 3 present
						// 1, 2 complements
						hunger = (hunger1 + hunger2) * 2 + hunger3;
						saturation = (saturation1 + saturation2) * 2 + saturation3;
						ingredientTag1.putBoolean("Complementary", true);
						ingredientTag2.putBoolean("Complementary", true);
						ingredientTag3.putBoolean("Complementary", false);
					}
					else if(areComplements(item1, item3)) {
						// 1, 2, 3 present
						// 1, 3 complements
						hunger = (hunger1 + hunger3) * 2 + hunger2;
						saturation = (saturation1 + saturation3) * 2 + saturation2;
						ingredientTag1.putBoolean("Complementary", true);
						ingredientTag2.putBoolean("Complementary", false);
						ingredientTag3.putBoolean("Complementary", true);
					}
					else if(areComplements(item2, item3)) {
						// 1, 2, 3 present
						// 2, 3 complements
						hunger = (hunger2 + hunger3) * 2 + hunger1;
						saturation = (saturation2 + saturation3) * 2 + saturation1;
						ingredientTag1.putBoolean("Complementary", false);
						ingredientTag2.putBoolean("Complementary", true);
						ingredientTag3.putBoolean("Complementary", true);
					}
					else {
						// 1, 2, 3 present
						// none complements
						hunger = hunger1 + hunger2 + hunger3;
						saturation = saturation1 + saturation2 + saturation3;
						ingredientTag1.putBoolean("Complementary", false);
						ingredientTag2.putBoolean("Complementary", false);
						ingredientTag3.putBoolean("Complementary", false);
					}
				}
				else {
					if(areComplements(item1, item2)) {
						// 1, 2 present
						// 1, 2 complements
						hunger = (hunger1 + hunger2) * 2;
						saturation = (saturation1 + saturation2) * 2;
						ingredientTag1.putBoolean("Complementary", true);
						ingredientTag2.putBoolean("Complementary", true);
					}
					else {
						// 1, 2 present
						// none complements
						hunger = hunger1 + hunger2;
						saturation = saturation1 + saturation2;
						ingredientTag1.putBoolean("Complementary", false);
						ingredientTag2.putBoolean("Complementary", false);
					}
				}
			}
			else if(item3.isFood()) {
				if(areComplements(item1, item3)) {
					// 1, 3 present
					// 1, 3 complements
					hunger = (hunger1 + hunger3) * 2;
					saturation = (saturation1 + saturation3) * 2;
					ingredientTag1.putBoolean("Complementary", true);
					ingredientTag3.putBoolean("Complementary", true);
				}
				else {
					// 1, 3 present
					// none complements
					hunger = hunger1 + hunger3;
					saturation = saturation1 + saturation3;
					ingredientTag1.putBoolean("Complementary", false);
					ingredientTag3.putBoolean("Complementary", false);
				}
			}
			else {
				// 1 present
				// none complements
				hunger = hunger1;
				saturation = saturation1;
				ingredientTag1.putBoolean("Complementary", false);
			}
		}
		else if(item2.isFood()) {
			if(item3.isFood()) {
				if(areComplements(item2, item3)) {
					// 2, 3 present
					// 2, 3 complements
					hunger = (hunger2 + hunger3) * 2;
					saturation = (saturation2 + saturation3) * 2;
					ingredientTag2.putBoolean("Complementary", true);
					ingredientTag3.putBoolean("Complementary", true);
				}
				else {
					// 2, 3 present
					// none complements
					hunger = hunger2 + hunger3;
					saturation = saturation2 + saturation3;
					ingredientTag2.putBoolean("Complementary", false);
					ingredientTag3.putBoolean("Complementary", false);
				}
			}
			else {
				// 2 present
				// none complements
				hunger = hunger2;
				saturation = saturation2;
				ingredientTag2.putBoolean("Complementary", false);
			}
		}
		else if(item3.isFood()) {
			// 3 present
			// none complements
			hunger = hunger3;
			saturation = saturation3;
			ingredientTag3.putBoolean("Complementary", false);
		}
		NbtCompound compoundTag = givenStack.getOrCreateSubTag("SandwichData");
		compoundTag.putInt("Hunger", hunger);
		compoundTag.putFloat("SaturationModifier", saturation);
		compoundTag.put("Ingredients", listTag);
		return givenStack;
	}

	@Environment(EnvType.CLIENT)
	public boolean fits(int width, int height) {
		return width == 3 && height == 3;
	}

	public RecipeSerializer<?> getSerializer() {
		return CulinaireFoodBundle.SANDWICH_MAKING;
	}
}
