package hugman.ce_foodstuffs.init.data;

import com.hugman.dawn.api.creator.ScreenHandlerCreator;
import hugman.ce_foodstuffs.init.CEFPack;
import hugman.ce_foodstuffs.objects.screen.handler.KettleScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class CEFScreenHandlers extends CEFPack {
	public static final ScreenHandlerType<KettleScreenHandler> KETTLE_SCREEN_HANDLER = register(new ScreenHandlerCreator.Builder("kettle", KettleScreenHandler::new));
}
