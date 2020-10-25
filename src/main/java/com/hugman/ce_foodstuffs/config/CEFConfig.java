package com.hugman.ce_foodstuffs.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.PartitioningSerializer;

@Config(name = "ce_foodstuffs")
@Config.Gui.Background("minecraft:textures/block/yellow_concrete.png")
public class CEFConfig extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("features")
	@ConfigEntry.Gui.TransitiveObject
	public FeaturesCategory features = new FeaturesCategory();

	@Config(name = "features")
	public static class FeaturesCategory implements ConfigData {
		public boolean canDrinkMilkBucket = false;
	}
}
