package fr.hugman.culinaire.client;

import fr.hugman.culinaire.client.render.block.CulinaireBlockRenderLayers;
import fr.hugman.culinaire.client.screen.CulinaireScreens;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CulinaireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CulinaireBlockRenderLayers.register();
        CulinaireScreens.register();
    }
}
