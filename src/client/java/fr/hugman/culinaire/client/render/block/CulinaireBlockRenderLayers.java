package fr.hugman.culinaire.client.render.block;

import fr.hugman.culinaire.block.CulinaireBlocks;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public final class CulinaireBlockRenderLayers {
    public static void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(CulinaireBlocks.LETTUCE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CulinaireBlocks.TOMATOES, RenderLayer.getCutout());
    }
}
