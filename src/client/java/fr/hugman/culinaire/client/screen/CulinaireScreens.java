package fr.hugman.culinaire.client.screen;

import fr.hugman.culinaire.world.menu.CulinaireMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;

public class CulinaireScreens {
    public static void register() {
        MenuScreens.register(CulinaireMenuTypes.KETTLE, KettleScreen::new);
        MenuScreens.register(CulinaireMenuTypes.SANDWICH_MAKING, SandwichMakingScreen::new);
    }
}
