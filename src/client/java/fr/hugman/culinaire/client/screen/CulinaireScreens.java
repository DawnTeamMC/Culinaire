package fr.hugman.culinaire.client.screen;

import fr.hugman.culinaire.registry.content.TeaContent;
import fr.hugman.culinaire.screen.KettleScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CulinaireScreens {
    public static void init() {
        ScreenRegistry.register(TeaContent.KETTLE_SCREEN_HANDLER, KettleScreen::new);
    }
}
