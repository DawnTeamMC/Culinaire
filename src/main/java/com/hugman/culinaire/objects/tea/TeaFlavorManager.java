package com.hugman.culinaire.objects.tea;

import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TeaFlavorManager {
	public static final TeaFlavorManager INSTANCE = new TeaFlavorManager();

	private final Map<Identifier, TeaFlavor> entries = new HashMap<>();
	private boolean locked;

	public static void lock() {
		INSTANCE.locked = true;
	}

	public static void unlock() {
		INSTANCE.locked = false;
	}

	public static void reset() {
		if(INSTANCE.locked) {
			throw new IllegalStateException("Tea flavor manager is locked (trying to reset)");
		}
		INSTANCE.entries.clear();
	}

	public static void register(Identifier id, TeaFlavor flavor) {
		if(INSTANCE.locked) {
			throw new IllegalStateException("Tea flavor manager is locked (trying to add " + id + ")");
		}
		INSTANCE.entries.put(id, flavor);
	}

	public static TeaFlavor get(Identifier id) {
		return INSTANCE.entries.get(id);
	}

	public static Collection<TeaFlavor> getAll() {
		return INSTANCE.entries.values();
	}

	public static Identifier getId(TeaFlavor flavor) {
		for(Map.Entry<Identifier, TeaFlavor> entry : INSTANCE.entries.entrySet()) {
			if(entry.getValue() == flavor) {
				return entry.getKey();
			}
		}
		return null;
	}
}
