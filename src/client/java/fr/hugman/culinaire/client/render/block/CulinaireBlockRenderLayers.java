package fr.hugman.culinaire.client.render.block;

import fr.hugman.culinaire.registry.content.VegetableContent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public final class CulinaireBlockRenderLayers {
    public static void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(VegetableContent.LETTUCE_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(VegetableContent.TOMATO_BLOCK, RenderLayer.getCutout());
    }
}
