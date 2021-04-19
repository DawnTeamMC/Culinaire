package com.hugman.culinaire.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
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
