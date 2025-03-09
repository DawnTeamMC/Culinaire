package fr.hugman.culinaire.client.screen;

import fr.hugman.culinaire.screen.CulinaireScreenHandlerTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class CulinaireScreens {
    public static void init() {
        HandledScreens.register(CulinaireScreenHandlerTypes.KETTLE, KettleScreen::new);
    }
}
