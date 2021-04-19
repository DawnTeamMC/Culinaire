package com.hugman.culinaire.init;

import com.hugman.culinaire.Culinaire;
import com.hugman.dawn.api.creator.Creator;
import com.hugman.dawn.api.creator.pack.Pack;

public abstract class CulinairePack extends Pack {
	protected static <V, B extends Creator.Builder<V>> V register(B creatorBuilder) {
		return add(creatorBuilder, Culinaire.MOD_DATA);
	}

	protected static <P extends Pack, B extends Pack.Builder> P register(B packBuilder) {
		return add(packBuilder, Culinaire.MOD_DATA);
	}
}
