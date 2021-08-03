package com.hugman.culinaire.config;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = "culinaire")
@Config.Gui.Background("minecraft:textures/block/yellow_concrete.png")
public class CulinaireConfig extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("features")
	@ConfigEntry.Gui.TransitiveObject
	public FeaturesCategory features = new FeaturesCategory();

	@Config(name = "features")
	public static class FeaturesCategory implements ConfigData {
		public boolean canDrinkMilkBucket = false;
		public int milkBottlesMaxCount = 1;
	}
}
