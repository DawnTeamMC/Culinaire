package fr.hugman.culinaire.registry;

import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.registry.CompostableRegistry;

public class CulinaireCompostables {
    public static void register() {
        CompostableRegistry.INSTANCE.add(CulinaireItems.LETTUCE, 0.3f);
        CompostableRegistry.INSTANCE.add(CulinaireItems.LETTUCE_SEEDS, 0.5f);
        CompostableRegistry.INSTANCE.add(CulinaireItems.TOMATO, 0.3f);

        CompostableRegistry.INSTANCE.add(CulinaireItems.CHEESE, 0.5f);

        CompostableRegistry.INSTANCE.add(CulinaireItems.MARSHMALLOW, 0.3f);

        CompostableRegistry.INSTANCE.add(CulinaireItems.CHOUQUETTE, 0.3f);
        CompostableRegistry.INSTANCE.add(CulinaireItems.CROISSANT, 0.3f);

        CompostableRegistry.INSTANCE.add(CulinaireItems.APPLE_PIE, 1.0f);
        CompostableRegistry.INSTANCE.add(CulinaireItems.SWEET_BERRY_PIE, 1.0f);

        CompostableRegistry.INSTANCE.add(CulinaireItems.SANDWICH, 1.0f);
    }
}
