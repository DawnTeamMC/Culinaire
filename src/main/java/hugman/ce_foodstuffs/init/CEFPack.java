package hugman.ce_foodstuffs.init;

import com.hugman.dawn.api.creator.pack.Pack;
import com.hugman.dawn.api.creator.pack.PackBuilder;
import com.hugman.dawn.api.util.CreatorBuilder;
import hugman.ce_foodstuffs.CEFoodstuffs;

public abstract class CEFPack extends Pack {
	protected static <V, B extends CreatorBuilder> V register(B creatorBuilder) {
		return add(creatorBuilder, CEFoodstuffs.MOD_DATA);
	}

	protected static <P extends Pack, B extends PackBuilder> P register(B packBuilder) {
		return add(packBuilder, CEFoodstuffs.MOD_DATA);
	}
}
