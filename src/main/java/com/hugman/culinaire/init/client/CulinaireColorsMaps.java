package com.hugman.culinaire.init.client;

import com.hugman.culinaire.init.TeaBundle;
import com.hugman.culinaire.objects.tea.TeaType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

@Environment(EnvType.CLIENT)
public class CulinaireColorsMaps {
	public static void registerColors() {
		registerItemColors();
	}

	private static void registerItemColors() {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : TeaType.getColor(stack), TeaBundle.TEA_BOTTLE);
	}
}
