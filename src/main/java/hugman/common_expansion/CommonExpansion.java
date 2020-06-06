package hugman.common_expansion;

import hugman.common_expansion.init.CEItems;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonExpansion implements ModInitializer
{
	public static final String MOD_ID = "common_expansion";
	public static final String MOD_PREFIX = MOD_ID + ":";
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize()
	{
		new CEItems();
	}
}
