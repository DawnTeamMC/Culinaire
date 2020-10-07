package hugman.ce_foodstuffs;

import com.hugman.dawn.api.creator.ModData;
import hugman.ce_foodstuffs.init.CEFBlocks;
import hugman.ce_foodstuffs.init.CEFItems;
import hugman.ce_foodstuffs.init.CEFSounds;
import hugman.ce_foodstuffs.init.data.CEFLootTables;
import hugman.ce_foodstuffs.init.data.CEFRecipeSerializers;
import hugman.ce_foodstuffs.init.data.CEFScreenHandlers;
import hugman.ce_foodstuffs.init.data.CEFStats;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CEFoodstuffs implements ModInitializer {
	public static final ModData MOD_DATA = new ModData("ce_foodstuffs");
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		new CEFBlocks();
		new CEFItems();
		new CEFSounds();
		new CEFStats();
		new CEFScreenHandlers();
		new CEFRecipeSerializers();
		CEFLootTables.addToVanillaTables();
	}
}
