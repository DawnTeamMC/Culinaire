package fr.hugman.culinaire.client.screen;

import fr.hugman.culinaire.screen.CulinaireScreenHandlerTypes;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class CulinaireScreens {
    public static void register() {
        HandledScreens.register(CulinaireScreenHandlerTypes.KETTLE, KettleScreen::new);
    }
}
