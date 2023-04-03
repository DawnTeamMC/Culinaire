package com.hugman.culinaire.client;

import com.hugman.culinaire.registry.content.TeaContent;
import com.hugman.culinaire.tea.TeaHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

@Environment(EnvType.CLIENT)
public class CulinaireColorsMaps {
    public static void registerColors() {
        registerItemColors();
    }

    private static void registerItemColors() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : TeaHelper.getColor(stack), TeaContent.TEA_BOTTLE);
    }
}
