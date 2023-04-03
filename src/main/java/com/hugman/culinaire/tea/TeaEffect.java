package com.hugman.culinaire.tea;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface TeaEffect {
    void apply(LivingEntity user, ItemStack stack, World world, TeaType teaType);
}
