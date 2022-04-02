package com.hugman.culinaire.objects.tea;

import com.hugman.culinaire.init.CulinaireRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

import java.util.List;

public record TeaFlavor(int color, List<TeaPotency> potencies) {
	public static final Codec<TeaFlavor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("color").forGetter(flavor -> flavor.color),
			TeaPotency.CODEC.listOf().fieldOf("potencies").forGetter(flavor -> flavor.potencies)
	).apply(instance, TeaFlavor::new));

	/**
	 * Returns the registry ID of this tea flavor.
	 */
	public Identifier getId() {
		return CulinaireRegistries.TEA_FLAVOR.getId(this);
	}

	public TeaPotency getPotency(int potency) {
		for(TeaPotency teaPotency : potencies) {
			if(teaPotency.value() == potency) {
				return teaPotency;
			}
		}
		return null;
	}
}
