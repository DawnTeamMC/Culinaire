package com.hugman.culinaire.client;

import com.hugman.culinaire.registry.content.TeaContent;
import com.hugman.culinaire.screen.KettleScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class CulinaireScreens {
    public static void init() {
        ScreenRegistry.register(TeaContent.KETTLE_SCREEN_HANDLER, KettleScreen::new);
    }
}
