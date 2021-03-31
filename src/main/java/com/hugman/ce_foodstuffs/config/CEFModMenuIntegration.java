package com.hugman.ce_foodstuffs.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class CEFModMenuIntegration implements ModMenuApi {
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return (parent) -> (Screen) AutoConfig.getConfigScreen(CEFConfig.class, parent).get();
	}
}
