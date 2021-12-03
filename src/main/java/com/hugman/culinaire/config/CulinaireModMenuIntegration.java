package com.hugman.culinaire.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class CulinaireModMenuIntegration implements ModMenuApi {
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return (parent) -> (Screen) AutoConfig.getConfigScreen(CulinaireConfig.class, parent).get();
	}
}
