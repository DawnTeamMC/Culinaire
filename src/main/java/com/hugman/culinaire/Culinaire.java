package com.hugman.culinaire;

import com.google.common.reflect.Reflection;
import com.hugman.culinaire.config.CulinaireConfig;
import com.hugman.culinaire.init.CulinaireCauldronBehaviors;
import com.hugman.culinaire.init.CulinaireFoodBundle;
import com.hugman.culinaire.init.CulinaireTeaBundle;
import com.hugman.culinaire.init.data.CulinaireLootTables;
import com.hugman.dawn.api.object.ModData;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Culinaire implements ModInitializer {
	public static final ModData MOD_DATA = new ModData("culinaire");
	public static final Logger LOGGER = LogManager.getLogger();
	public static final CulinaireConfig CONFIG = AutoConfig.register(CulinaireConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new)).getConfig();

	@Override
	public void onInitialize() {
		Reflection.initialize(CulinaireFoodBundle.class);
		Reflection.initialize(CulinaireTeaBundle.class);
		MOD_DATA.registerCreators();
		CulinaireCauldronBehaviors.init();
		CulinaireLootTables.addToVanillaTables();
	}
}
