package com.hugman.culinaire.item;

import com.hugman.culinaire.tea.TeaHelper;
import com.hugman.culinaire.tea.TeaType;
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
        TeaHelper.appendTeaTooltip(tooltip, TeaHelper.getTeaTypesByCompound(stack.getNbt()));
    }
}