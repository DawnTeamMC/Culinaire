package com.hugman.culinaire;

import com.hugman.culinaire.init.client.CulinaireColorsMaps;
import com.hugman.culinaire.init.client.CulinaireScreens;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CulinaireClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CulinaireColorsMaps.registerColors();
		CulinaireScreens.init();
	}
}
