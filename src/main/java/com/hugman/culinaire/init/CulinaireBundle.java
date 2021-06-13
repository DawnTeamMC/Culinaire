package com.hugman.culinaire.init;

import com.hugman.culinaire.Culinaire;
import com.hugman.dawn.api.creator.Creator;
import com.hugman.dawn.api.creator.SimpleCreator;
import com.hugman.dawn.api.creator.bundle.Bundle;

public abstract class CulinaireBundle extends Bundle {
	protected static <O, V extends SimpleCreator<O>> O add(V creator) {
		Culinaire.MOD_DATA.addCreator(creator);
		return creator.getValue();
	}

	protected static <V extends Creator> V creator(V creator) {
		Culinaire.MOD_DATA.addCreator(creator);
		return creator;
	}

	protected static <V extends Bundle> V bundle(V bundle) {
		Culinaire.MOD_DATA.addBundle(bundle);
		return bundle;
	}
}
