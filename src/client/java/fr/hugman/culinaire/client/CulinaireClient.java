package fr.hugman.culinaire.client;

import fr.hugman.culinaire.client.render.item.tint.CulinaireTintSourceTypes;
import fr.hugman.culinaire.client.screen.CulinaireScreens;
import net.fabricmc.api.ClientModInitializer;

public class CulinaireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CulinaireScreens.register();
        CulinaireTintSourceTypes.register();
    }
}
