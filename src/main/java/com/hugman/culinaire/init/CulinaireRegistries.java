package com.hugman.culinaire.init;

import com.google.common.reflect.Reflection;
import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.objects.tea.TeaFlavor;
import com.hugman.culinaire.objects.tea.effect.TeaEffectType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.util.registry.SimpleRegistry;

public class CulinaireRegistries {
	public static final SimpleRegistry<TeaEffectType> TEA_EFFECT_TYPE = FabricRegistryBuilder.createSimple(TeaEffectType.class, Culinaire.MOD_DATA.id("tea_effect_type")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final SimpleRegistry<TeaFlavor> TEA_FLAVOR = FabricRegistryBuilder.createSimple(TeaFlavor.class, Culinaire.MOD_DATA.id("tea_flavor")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static void register() {
		Reflection.initialize(TeaEffectType.class);
		Reflection.initialize(TeaFlavor.class);
	}
}
