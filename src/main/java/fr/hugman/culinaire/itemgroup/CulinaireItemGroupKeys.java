package fr.hugman.culinaire.itemgroup;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class CulinaireItemGroupKeys {
    public static final RegistryKey<ItemGroup> CULINAIRE = of("culinaire");

    private static RegistryKey<ItemGroup> of(String path) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, Culinaire.id(path));
    }
}
