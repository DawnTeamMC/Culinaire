package hugman.ce_foodstuffs.init.client;

import hugman.ce_foodstuffs.init.data.CEFScreenHandlers;
import hugman.ce_foodstuffs.objects.screen.KettleScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class CEFScreens {
	public static void init() {
		ScreenRegistry.register(CEFScreenHandlers.KETTLE_SCREEN_HANDLER, KettleScreen::new);
	}
}
