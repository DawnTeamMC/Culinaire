package hugman.ce_foodstuffs.init.client;

import hugman.ce_foodstuffs.init.CEFBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class CEFRenderLayers {
	public static void registerRenderLayers() {
		RenderLayer cutout = RenderLayer.getCutout();
		BlockRenderLayerMap.INSTANCE.putBlock(CEFBlocks.LETTUCE, cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(CEFBlocks.TOMATOES, cutout);
	}
}
