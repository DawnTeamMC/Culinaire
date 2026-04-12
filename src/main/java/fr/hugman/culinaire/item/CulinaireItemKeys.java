package fr.hugman.culinaire.item;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class CulinaireItemKeys {
    private static ResourceKey<Item> of(String path) {
        return ResourceKey.create(Registries.ITEM, Culinaire.id(path));
    }
}
