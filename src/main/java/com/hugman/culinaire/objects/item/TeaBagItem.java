package com.hugman.culinaire.objects.item;

import com.hugman.culinaire.objects.tea.TeaFlavorManager;
import com.hugman.culinaire.objects.tea.TeaType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class TeaBagItem extends Item {
	public TeaBagItem(Settings settings) {
		super(settings);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		TeaType.appendTooltip(tooltip, TeaType.fromStack(stack));
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if(group == ItemGroup.SEARCH) {
			for(TeaType teaType : TeaType.getAll()) {
				ItemStack stack = new ItemStack(this);
				teaType.addToStack(stack);
				stacks.add(stack);
			}
		}
		else if(this.isIn(group)) {
			TeaFlavorManager.getAll().forEach(flavor -> {
				ItemStack stack = new ItemStack(this);
				TeaType.withMiddlePotency(flavor).addToStack(stack);
				stacks.add(stack);
			});
		}
	}
}