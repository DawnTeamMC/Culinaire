package com.hugman.ce_foodstuffs.init.client;

import com.hugman.ce_foodstuffs.init.CEFBlocks;
import com.hugman.ce_foodstuffs.objects.screen.KettleScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class CEFScreens {
	public static void init() {
		ScreenRegistry.register(CEFBlocks.KETTLE_SCREEN_HANDLER, KettleScreen::new);
	}
}
