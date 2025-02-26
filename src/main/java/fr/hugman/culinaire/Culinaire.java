package fr.hugman.culinaire;

import com.google.common.reflect.Reflection;
import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.block.CulinaireCauldronBehaviors;
import fr.hugman.culinaire.entity.CulinaireEntityTypes;
import fr.hugman.culinaire.item.CulinaireItems;
import fr.hugman.culinaire.itemgroup.CulinaireItemGroupAdditions;
import fr.hugman.culinaire.itemgroup.CulinaireItemGroups;
import fr.hugman.culinaire.loot.CulinaireLootTables;
import com.hugman.culinaire.registry.content.*;
import fr.hugman.culinaire.registry.CulinaireCompostingChances;
import fr.hugman.culinaire.registry.CulinaireFlammables;
import fr.hugman.culinaire.registry.CulinaireRegistries;
import fr.hugman.dawn.Registrar;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Culinaire implements ModInitializer {
    public static final String MOD_ID = "culinaire";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        CulinaireRegistries.register();
        
        Reflection.initialize(CulinaireSoundEvents.class);

        Reflection.initialize(CulinaireBlocks.class);

        CulinaireFlammables.register();

        Reflection.initialize(CulinaireItems.class);

        Reflection.initialize(CulinaireItemGroups.class);
        CulinaireItemGroupAdditions.appendItemGroups();
        CulinaireCompostingChances.register();
        CulinaireCauldronBehaviors.register();

        CulinaireLootTables.addToVanillaTables();
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
