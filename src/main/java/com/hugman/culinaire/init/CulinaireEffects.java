package com.hugman.culinaire.init;

import com.hugman.dawn.api.creator.EffectCreator;
import com.hugman.dawn.mod.object.effect.SimpleEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class CulinaireEffects extends CulinaireBundle {
	public static final StatusEffect FORESIGHT = add(new EffectCreator("foresight", new SimpleEffect(StatusEffectType.BENEFICIAL, 9966823)));
	public static final StatusEffect FULFILLMENT = add(new EffectCreator("fulfillment", new SimpleEffect(StatusEffectType.BENEFICIAL, 6048577)));
	public static final StatusEffect GUARD = add(new EffectCreator("guard", new SimpleEffect(StatusEffectType.BENEFICIAL, 16440596)));
	public static final StatusEffect POISON_RESISTANCE = add(new EffectCreator("poison_resistance", new SimpleEffect(StatusEffectType.BENEFICIAL, 7596722)));

	public static void init() {
	}
}