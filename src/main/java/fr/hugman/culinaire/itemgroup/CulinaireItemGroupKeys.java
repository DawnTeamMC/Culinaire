package fr.hugman.culinaire.itemgroup;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class CulinaireItemGroupKeys {
    public static final ResourceKey<CreativeModeTab> CULINAIRE = of("culinaire");

    private static ResourceKey<CreativeModeTab> of(String path) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, Culinaire.id(path));
    }
}
