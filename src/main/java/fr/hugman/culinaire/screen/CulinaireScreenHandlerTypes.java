package fr.hugman.culinaire.screen;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class CulinaireScreenHandlerTypes {
    public static final MenuType<KettleScreenHandler> KETTLE = register("kettle_screen", KettleScreenHandler::new);

    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuType.MenuSupplier<T> factory) {
        return Registry.register(BuiltInRegistries.MENU, Culinaire.id(name), new MenuType<>(factory, FeatureFlags.VANILLA_SET));
    }
}
