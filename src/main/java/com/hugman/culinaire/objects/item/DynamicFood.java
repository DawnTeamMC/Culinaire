package com.hugman.culinaire.objects.item;

import net.minecraft.item.ItemStack;

public interface DynamicFood {
	int getHunger(ItemStack stack);
	float getSaturationModifier(ItemStack stack);
}
