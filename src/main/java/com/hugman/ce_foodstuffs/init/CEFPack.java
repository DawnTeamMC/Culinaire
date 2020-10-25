package com.hugman.ce_foodstuffs.init;

import com.hugman.ce_foodstuffs.CEFoodstuffs;
import com.hugman.dawn.api.creator.Creator;
import com.hugman.dawn.api.creator.pack.Pack;

public abstract class CEFPack extends Pack {
	protected static <V, B extends Creator.Builder<V>> V register(B creatorBuilder) {
		return add(creatorBuilder, CEFoodstuffs.MOD_DATA);
	}

	protected static <P extends Pack, B extends Pack.Builder> P register(B packBuilder) {
		return add(packBuilder, CEFoodstuffs.MOD_DATA);
	}
}
