package hugman.ce_foodstuffs;

import hugman.ce_foodstuffs.init.CEFBlocks;
import hugman.ce_foodstuffs.init.CEFItems;
import hugman.ce_foodstuffs.init.data.CEFBlockEntityTypes;
import hugman.ce_foodstuffs.init.data.CEFLootTables;
import hugman.ce_foodstuffs.init.data.CEFRecipeSerializers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CEFoodstuffs implements ModInitializer {
	public static final String MOD_ID = "ce_foodstuffs";
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		new CEFBlocks();
		new CEFBlockEntityTypes();
		new CEFItems();
		new CEFRecipeSerializers();
		CEFLootTables.addToVanillaTables();
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
}
