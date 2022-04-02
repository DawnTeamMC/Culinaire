package com.hugman.culinaire.objects.tea.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public record ClearEffectTeaEffect(StatusEffect statusEffect) implements TeaEffect {
	public static final Codec<ClearEffectTeaEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Registry.STATUS_EFFECT.getCodec().fieldOf("mob_effect").forGetter(ClearEffectTeaEffect::statusEffect)
	).apply(instance, ClearEffectTeaEffect::new));

	@Override
	public void apply(LivingEntity user, ItemStack stack, World world) {
		user.removeStatusEffect(this.statusEffect);
	}

	@Override
	public TeaEffectType<ClearEffectTeaEffect> getType() {
		return TeaEffectType.CLEAR_EFFECT;
	}
}