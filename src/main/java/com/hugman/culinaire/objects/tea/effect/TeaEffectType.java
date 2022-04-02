package com.hugman.culinaire.objects.tea.effect;

import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.init.CulinaireRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;

public record TeaEffectType<E extends TeaEffect>(Codec<E> codec) {
	public static final TeaEffectType<ConsumeItemTeaEffect> CONSUME_ITEM = register("consume_item", ConsumeItemTeaEffect.CODEC);
	public static final TeaEffectType<GrantEffectTeaEffect> GRANT_EFFECT = register("grant_effect", GrantEffectTeaEffect.CODEC);
	public static final TeaEffectType<ClearEffectTeaEffect> CLEAR_EFFECT = register("clear_effect", ClearEffectTeaEffect.CODEC);

	private static <E extends TeaEffect> TeaEffectType<E> register(String string, Codec<E> codec) {
		Culinaire.LOGGER.info("Registering TeaEffectType: " + Culinaire.MOD_DATA.id(string));
		return Registry.register(CulinaireRegistries.TEA_FLAVOR_EFFECT, Culinaire.MOD_DATA.id(string), new TeaEffectType<>(codec));
	}
}
