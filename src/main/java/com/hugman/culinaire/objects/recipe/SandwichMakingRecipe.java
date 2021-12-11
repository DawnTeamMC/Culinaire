package com.hugman.culinaire.objects.recipe;

import com.hugman.culinaire.init.CulinaireFoodBundle;
import com.hugman.culinaire.util.SandwichUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class SandwichMakingRecipe extends SpecialCraftingRecipe {

	public SandwichMakingRecipe(Identifier identifier) {
		super(identifier);
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
			if(SandwichUtil.VALID_BREAD.test(topMiddleStack) && SandwichUtil.VALID_BREAD.test(bottomMiddleStack)) {
				hasBread = true;
			}
		}
		for(int i = 3; i < 6; ++i) {
			ItemStack itemStack = inv.getStack(i);
			if(!itemStack.isEmpty()) {
				if(itemStack.isFood() && !SandwichUtil.INGREDIENT_BLACKLIST.test(itemStack)) {
					hasAnIngredient = true;
				}
				else {
					return false;
				}
			}
		}
		return hasBread && hasAnIngredient;
	}

	public ItemStack craft(CraftingInventory inv) {
		ItemStack givenStack = new ItemStack(CulinaireFoodBundle.SANDWICH);
		int hunger = 0;
		float saturation = 0.0f;
		Item[] items = new Item[]{inv.getStack(3).getItem(), inv.getStack(4).getItem(), inv.getStack(5).getItem()};
		NbtCompound[] ingredientTags = new NbtCompound[]{new NbtCompound(), new NbtCompound(), new NbtCompound()};
		NbtList listTag = new NbtList();
		for(int i = 0; i < 3; i++) {
			if(items[i].isFood()) {
				ingredientTags[i].putString("Item", Registry.ITEM.getId(items[i]).toString());
				listTag.add(ingredientTags[i]);
			}
		}
		if(items[0].isFood()) {
			if(items[1].isFood()) {
				if(items[2].isFood()) {
					// XXX are food
					boolean[] booTab = SandwichUtil.areComplements(items[0], items[1], items[2]);
					hunger = SandwichUtil.getHunger(booTab, items[0], items[1], items[2]);
					saturation = SandwichUtil.getSaturationModifier(booTab, items[0], items[1], items[2]);
					for(int i = 0; i < 3; i++) {
						ingredientTags[i].putBoolean("Complementary", booTab[i]);
					}
				}
				else {
					// XXO are food
					boolean b = SandwichUtil.areComplements(items[0], items[1]);
					hunger = SandwichUtil.getHunger(b, items[0], items[1]);
					saturation = SandwichUtil.getSaturationModifier(b, items[0], items[1]);
					ingredientTags[0].putBoolean("Complementary", b);
					ingredientTags[1].putBoolean("Complementary", b);
				}
			}
			else if(items[2].isFood()) {
				// XOX are food
				boolean b = SandwichUtil.areComplements(items[0], items[2]);
				hunger = SandwichUtil.getHunger(b, items[0], items[2]);
				saturation = SandwichUtil.getSaturationModifier(b, items[0], items[2]);
				ingredientTags[0].putBoolean("Complementary", b);
				ingredientTags[2].putBoolean("Complementary", b);
			}
			else {
				// XOO are food
				hunger = (int) (items[0].getFoodComponent().getHunger() * SandwichUtil.MIN_HUNGER);
				saturation = (float) (items[0].getFoodComponent().getSaturationModifier() * SandwichUtil.MIN_SATURATION);
				ingredientTags[0].putBoolean("Complementary", false);
			}
		}
		else if(items[1].isFood()) {
			if(items[2].isFood()) {
				// OXX are food
				boolean b = SandwichUtil.areComplements(items[1], items[2]);
				hunger = SandwichUtil.getHunger(b, items[1], items[2]);
				saturation = SandwichUtil.getSaturationModifier(b, items[1], items[2]);
				ingredientTags[1].putBoolean("Complementary", b);
				ingredientTags[2].putBoolean("Complementary", b);
			}
			else {
				// OXO are food
				hunger = (int) (items[1].getFoodComponent().getHunger() * SandwichUtil.MIN_HUNGER);
				saturation = (float) (items[1].getFoodComponent().getSaturationModifier() * SandwichUtil.MIN_SATURATION);
				ingredientTags[1].putBoolean("Complementary", false);
			}
		}
		else if(items[2].isFood()) {
			// OOX are food
			hunger = (int) (items[2].getFoodComponent().getHunger() * SandwichUtil.MIN_HUNGER);
			saturation = (float) (items[2].getFoodComponent().getSaturationModifier() * SandwichUtil.MIN_SATURATION);
			ingredientTags[2].putBoolean("Complementary", false);
		}
		NbtCompound compoundTag = givenStack.getOrCreateSubNbt("SandwichData");
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
