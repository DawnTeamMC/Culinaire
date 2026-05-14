package fr.hugman.culinaire.world.menu;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class CulinaireMenuTypes {
    public static final MenuType<KettleMenu> KETTLE = register("kettle", KettleMenu::new);
    public static final MenuType<SandwichMakingMenu> SANDWICH_MAKING = register("sandwich_making", SandwichMakingMenu::new);

    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuType.MenuSupplier<T> factory) {
        return Registry.register(BuiltInRegistries.MENU, Culinaire.id(name), new MenuType<>(factory, FeatureFlags.VANILLA_SET));
    }
}
