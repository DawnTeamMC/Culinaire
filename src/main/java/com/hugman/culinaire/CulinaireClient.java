package com.hugman.culinaire;

import com.hugman.culinaire.client.CulinaireColorsMaps;
import com.hugman.culinaire.client.CulinaireScreens;
import com.hugman.culinaire.registry.content.VegetableContent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class CulinaireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CulinaireColorsMaps.registerColors();
        CulinaireScreens.init();
    }


    public static void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(VegetableContent.LETTUCE_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(VegetableContent.TOMATO_BLOCK, RenderLayer.getCutout());

    }
}
