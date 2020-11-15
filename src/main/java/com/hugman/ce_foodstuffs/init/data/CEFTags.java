package com.hugman.ce_foodstuffs.init.data;

import com.hugman.ce_foodstuffs.CEFoodstuffs;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class CEFTags {
	public static class Items {
		public static final Tag<Item> SANDWICH_BREAD = register("c", "sandwich/bread");
		public static final Tag<Item> SANDWICH_BLACKLIST = register("c", "sandwich/blacklist");

		private static Tag<Item> register(String name) {
			return TagRegistry.item(CEFoodstuffs.MOD_DATA.id(name));
		}

		private static Tag<Item> register(String namespace, String path) {
			return TagRegistry.item(new Identifier(namespace, path));
		}
	}
}
