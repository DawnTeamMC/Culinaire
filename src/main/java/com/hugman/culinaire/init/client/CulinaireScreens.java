package com.hugman.culinaire.init.client;

import com.hugman.culinaire.init.TeaBundle;
import com.hugman.culinaire.objects.screen.KettleScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class CulinaireScreens {
	public static void init() {
		ScreenRegistry.register(TeaBundle.KETTLE_SCREEN_HANDLER.getType(), KettleScreen::new);
	}
}
