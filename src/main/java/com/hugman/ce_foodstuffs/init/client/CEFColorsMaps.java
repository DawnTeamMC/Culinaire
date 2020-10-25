package com.hugman.ce_foodstuffs.init.client;

import com.hugman.ce_foodstuffs.init.CEFItems;
import com.hugman.ce_foodstuffs.objects.item.tea.TeaHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

@Environment(EnvType.CLIENT)
public class CEFColorsMaps {
	public static void registerColors() {
		registerItemColors();
	}

	private static void registerItemColors() {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : TeaHelper.getColor(stack), CEFItems.TEA_BOTTLE);
	}
}
