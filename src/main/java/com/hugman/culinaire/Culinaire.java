package com.hugman.culinaire;

import com.hugman.culinaire.block.CulinaireCauldronBehaviors;
import com.hugman.culinaire.config.CulinaireConfig;
import com.hugman.culinaire.loot.CulinaireLootTables;
import com.hugman.culinaire.registry.content.*;
import fr.hugman.dawn.Registrar;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Culinaire implements ModInitializer {
    public static final Registrar REGISTRAR = new Registrar("culinaire");
    public static final Logger LOGGER = LogManager.getLogger();
    public static final CulinaireConfig CONFIG = AutoConfig.register(CulinaireConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new)).getConfig();

    @Override
    public void onInitialize() {
        FruitContent.register(REGISTRAR);
        VegetableContent.register(REGISTRAR);
        DairyContent.register(REGISTRAR);

        CandyContent.register(REGISTRAR);
        PastryContent.register(REGISTRAR);

        MealContent.register(REGISTRAR);
        TeaContent.register(REGISTRAR);

        CulinaireCauldronBehaviors.init();
        CulinaireLootTables.addToVanillaTables();
    }

    public static Identifier id(String path) {
        return REGISTRAR.id(path);
    }
}
