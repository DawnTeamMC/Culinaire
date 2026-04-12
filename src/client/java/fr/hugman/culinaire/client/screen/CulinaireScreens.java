package fr.hugman.culinaire.client.screen;

import fr.hugman.culinaire.screen.CulinaireScreenHandlerTypes;
import net.minecraft.client.gui.screens.MenuScreens;

public class CulinaireScreens {
    public static void register() {
        MenuScreens.register(CulinaireScreenHandlerTypes.KETTLE, KettleScreen::new);
    }
}
