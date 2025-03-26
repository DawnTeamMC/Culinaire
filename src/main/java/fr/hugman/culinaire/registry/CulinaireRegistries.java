package fr.hugman.culinaire.registry;

import fr.hugman.culinaire.tea.TeaType;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

public class CulinaireRegistries {
    public static void register() {
        DynamicRegistries.registerSynced(CulinaireRegistryKeys.TEA_TYPE, TeaType.CODEC);
    }
}
