package com.hugman.ce_foodstuffs.init;

import com.hugman.dawn.api.creator.EffectCreator;
import com.hugman.dawn.mod.object.effect.SimpleEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class CEFEffects extends CEFPack {
	public static final StatusEffect FORESIGHT = register(new EffectCreator.Builder("foresight", new SimpleEffect(StatusEffectType.BENEFICIAL, 9966823)));
	public static final StatusEffect FULFILLMENT = register(new EffectCreator.Builder("fulfillment", new SimpleEffect(StatusEffectType.BENEFICIAL, 6048577)));
	public static final StatusEffect GUARD = register(new EffectCreator.Builder("guard", new SimpleEffect(StatusEffectType.BENEFICIAL, 16440596)));
	public static final StatusEffect POISON_RESISTANCE = register(new EffectCreator.Builder("poison_resistance", new SimpleEffect(StatusEffectType.BENEFICIAL, 7596722)));

	public static void init() {
	}
}