package fr.hugman.culinaire;

import com.google.common.reflect.Reflection;
import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.block.CulinaireCauldronBehaviors;
import fr.hugman.culinaire.block.entity.CulinaireBlockEntityTypes;
import fr.hugman.culinaire.component.CulinaireComponentModifiers;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.entity.effect.CulinaireEffects;
import fr.hugman.culinaire.item.CulinaireItems;
import fr.hugman.culinaire.itemgroup.CulinaireItemGroupAdditions;
import fr.hugman.culinaire.itemgroup.CulinaireItemGroups;
import fr.hugman.culinaire.loot.CulinaireLootTables;
import fr.hugman.culinaire.recipe.CulinaireRecipeSerializers;
import fr.hugman.culinaire.registry.CulinaireCompostingChances;
import fr.hugman.culinaire.registry.CulinaireFlammables;
import fr.hugman.culinaire.registry.CulinaireRegistries;
import fr.hugman.culinaire.screen.CulinaireScreenHandlerTypes;
import fr.hugman.culinaire.sound.CulinaireSoundEvents;
import fr.hugman.culinaire.stat.CulinaireStats;
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
        Reflection.initialize(CulinaireBlockEntityTypes.class);

        CulinaireFlammables.register();

        Reflection.initialize(CulinaireComponentTypes.class);

        Reflection.initialize(CulinaireItems.class);

        Reflection.initialize(CulinaireItemGroups.class);
        CulinaireItemGroupAdditions.registerEvents();
        CulinaireCompostingChances.register();
        CulinaireCauldronBehaviors.register();

        Reflection.initialize(CulinaireRecipeSerializers.class);

        CulinaireLootTables.addToVanillaTables();

        CulinaireComponentModifiers.registerEvents();

        Reflection.initialize(CulinaireScreenHandlerTypes.class);

        Reflection.initialize(CulinaireEffects.class);

        Reflection.initialize(CulinaireStats.class);
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
