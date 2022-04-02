package com.hugman.culinaire.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.registry.Registry;

public record StatusEffectApplier(StatusEffect type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon) {
	public static final MapCodec<StatusEffectApplier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Registry.STATUS_EFFECT.getCodec().fieldOf("mob_effect").forGetter(StatusEffectApplier::type),
			Codec.INT.optionalFieldOf("duration", 0).forGetter(StatusEffectApplier::duration),
			Codec.INT.optionalFieldOf("amplifier", 0).forGetter(StatusEffectApplier::amplifier),
			Codec.BOOL.optionalFieldOf("ambient", false).forGetter(StatusEffectApplier::ambient),
			Codec.BOOL.optionalFieldOf("show_particles", true).forGetter(StatusEffectApplier::showParticles),
			Codec.BOOL.optionalFieldOf("show_icon", true).forGetter(StatusEffectApplier::showIcon)
	).apply(instance, StatusEffectApplier::new));

	public void apply(LivingEntity user) {
		if(this.type.isInstant()) {
			this.type.applyInstantEffect(user, user, user, this.amplifier, 1.0D);
		}
		else {
			user.addStatusEffect(new StatusEffectInstance(this.type, this.duration, this.amplifier, this.ambient, this.showParticles, this.showIcon));
		}
	}
}
