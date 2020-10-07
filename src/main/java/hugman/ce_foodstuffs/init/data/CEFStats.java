package hugman.ce_foodstuffs.init.data;

import com.hugman.dawn.api.creator.StatCreator;
import hugman.ce_foodstuffs.init.CEFPack;
import net.minecraft.util.Identifier;

public class CEFStats extends CEFPack {
	public static final Identifier INTERACT_WITH_KETTLE = register(new StatCreator.Builder("interact_with_kettle"));
}
