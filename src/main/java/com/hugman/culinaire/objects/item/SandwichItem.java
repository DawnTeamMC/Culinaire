package com.hugman.culinaire.objects.item;

import com.hugman.dawn.api.object.item.DynamicFood;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SandwichItem extends Item implements DynamicFood {
	public static final String SANDWICH_DATA = "SandwichData";
	public static final String HUNGER = "Hunger";
	public static final String SATURATION_MODIFIER = "SaturationModifier";
	public static final String HAS_GLINT = "HasGlint";
	public static final String INGREDIENT_LIST = "IngredientList";

	public SandwichItem(Settings settings) {
		super(settings);
	}

	@Override
	public int getHunger(ItemStack stack) {
		NbtCompound sandwichData = stack.getSubNbt(SANDWICH_DATA);
		if(sandwichData != null) {
			if(sandwichData.contains(HUNGER))
			return sandwichData.getInt(HUNGER);
		}
		else {
			FoodComponent foodComponent = stack.getItem().getFoodComponent();
			if(foodComponent != null) {
				return foodComponent.getHunger();
			}
		}
		return 0;
	}

	@Override
	public float getSaturationModifier(ItemStack stack) {
		NbtCompound sandwichData = stack.getSubNbt(SANDWICH_DATA);
		if(sandwichData != null) {
			if(sandwichData.contains(SATURATION_MODIFIER))
				return sandwichData.getFloat(SATURATION_MODIFIER);
		}
		else {
			FoodComponent foodComponent = stack.getItem().getFoodComponent();
			if(foodComponent != null) {
				return foodComponent.getSaturationModifier();
			}
		}
		return 0;
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		NbtCompound sandwichData = stack.getSubNbt(SANDWICH_DATA);
		if(sandwichData != null) {
			if(sandwichData.contains(HAS_GLINT)) {
				return sandwichData.getBoolean(HAS_GLINT);
			}
		}
		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		NbtCompound sandwichData = stack.getSubNbt(SANDWICH_DATA);
		if(sandwichData != null) {
			if(sandwichData.contains(INGREDIENT_LIST)) {
				tooltip.add(Text.Serializer.fromJson(sandwichData.getString(INGREDIENT_LIST)));
			}
		}
	}
}
