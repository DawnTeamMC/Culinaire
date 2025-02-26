package fr.hugman.culinaire.item;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class CulinaireItemKeys {
    private static RegistryKey<Item> of(String path) {
        return RegistryKey.of(RegistryKeys.ITEM, Culinaire.id(path));
    }
}
