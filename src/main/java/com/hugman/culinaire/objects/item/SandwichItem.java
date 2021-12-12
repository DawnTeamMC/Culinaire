package com.hugman.culinaire.objects.item;

import com.hugman.culinaire.util.FoodUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.lwjgl.system.macosx.EnumerationMutationHandlerI;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

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
