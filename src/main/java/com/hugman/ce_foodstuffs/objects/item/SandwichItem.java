package com.hugman.ce_foodstuffs.objects.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SandwichItem extends Item {

	public SandwichItem(Settings settings) {
		super(settings);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		CompoundTag sandwichData = stack.getSubTag("SandwichData");
		if(sandwichData != null && user instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) user;
			player.getHungerManager().add(sandwichData.getInt("Hunger"), sandwichData.getFloat("SaturationModifier"));
		}
		return super.finishUsing(stack, world, user);
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		CompoundTag sandwichData = stack.getSubTag("SandwichData");
		if(sandwichData != null) {
			ListTag ingredientList = sandwichData.getList("Ingredients", 10);
			if(!ingredientList.isEmpty()) {
				for(int i = 0; i < ingredientList.size(); ++i) {
					CompoundTag ingredientData = ingredientList.getCompound(i);
					Item item = Registry.ITEM.get(new Identifier(ingredientData.getString("Item")));
					if(item.hasGlint(new ItemStack(item))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		MutableText text = new LiteralText("").formatted(Formatting.GRAY);
		CompoundTag sandwichData = stack.getSubTag("SandwichData");
		if(sandwichData != null) {
			ListTag ingredientList = sandwichData.getList("Ingredients", 10);
			if(!ingredientList.isEmpty()) {
				for(int i = 0; i < ingredientList.size(); ++i) {
					CompoundTag ingredientData = ingredientList.getCompound(i);
					Item item = Registry.ITEM.get(new Identifier(ingredientData.getString("Item")));
					if(i > 0) {
						text.append(", ");
					}
					text.append(((MutableText) (item.getName())).formatted(ingredientData.getBoolean("Complementary") ? Formatting.GREEN : Formatting.GRAY));
				}
				tooltip.add(text);
			}
		}
	}
}
