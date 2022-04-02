package com.hugman.culinaire.objects.tea.effect;

import com.hugman.culinaire.util.StatusEffectApplier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public record GrantEffectTeaEffect(StatusEffectApplier statusEffectApplier) implements TeaEffect {
	public static final Codec<GrantEffectTeaEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			StatusEffectApplier.CODEC.forGetter(biome -> biome.statusEffectApplier)
	).apply(instance, GrantEffectTeaEffect::new));

	@Override
	public void apply(LivingEntity user, ItemStack stack, World world) {
		this.statusEffectApplier.apply(user);
	}

	@Override
	public TeaEffectType<GrantEffectTeaEffect> getType() {
		return TeaEffectType.GRANT_EFFECT;
	}
}