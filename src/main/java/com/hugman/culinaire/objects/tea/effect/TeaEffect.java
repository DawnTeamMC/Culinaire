package com.hugman.culinaire.objects.tea.effect;

import com.hugman.culinaire.init.CulinaireRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface TeaEffect {
	Codec<TeaEffect> TYPE_CODEC = CulinaireRegistries.TEA_FLAVOR_EFFECT.getCodec().dispatchStable(TeaEffect::getType, TeaEffectType::codec);

	void apply(LivingEntity user, ItemStack stack, World world);

	TeaEffectType<?> getType();
}