package fr.hugman.culinaire.registry;

import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;

public class CulinaireCompostingChances {
    public static void register() {
        CompostingChanceRegistry.INSTANCE.add(CulinaireItems.LETTUCE, 0.3f);
        CompostingChanceRegistry.INSTANCE.add(CulinaireItems.LETTUCE_SEEDS, 0.5f);
        CompostingChanceRegistry.INSTANCE.add(CulinaireItems.TOMATO, 0.3f);

        CompostingChanceRegistry.INSTANCE.add(CulinaireItems.CHEESE, 0.5f);

        CompostingChanceRegistry.INSTANCE.add(CulinaireItems.MARSHMALLOW, 0.3f);

        CompostingChanceRegistry.INSTANCE.add(CulinaireItems.CHOUQUETTE, 0.3f);
        CompostingChanceRegistry.INSTANCE.add(CulinaireItems.CROISSANT, 0.3f);

        CompostingChanceRegistry.INSTANCE.add(CulinaireItems.APPLE_PIE, 1.0f);
        CompostingChanceRegistry.INSTANCE.add(CulinaireItems.SWEET_BERRY_PIE, 1.0f);

        CompostingChanceRegistry.INSTANCE.add(CulinaireItems.SANDWICH, 1.0f);
    }
}
