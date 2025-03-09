package fr.hugman.culinaire.screen;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class CulinaireScreenHandlerTypes {
    public static final ScreenHandlerType<KettleScreenHandler> KETTLE = register("kettle_screen", KettleScreenHandler::new);

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, Culinaire.id(name), new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }
}
