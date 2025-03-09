package fr.hugman.culinaire.registry;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

public class CulinaireFlammables {
    public static void register() {
        var fire = FlammableBlockRegistry.getDefaultInstance();
    }
}
