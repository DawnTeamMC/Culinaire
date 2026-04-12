package fr.hugman.culinaire.stat;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class CulinaireStats {
    public static final Identifier INTERACT_WITH_KETTLE = register("interact_with_kettle", StatFormatter.DEFAULT);

    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = Culinaire.id(id);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.get(identifier, formatter);
        return identifier;
    }
}
