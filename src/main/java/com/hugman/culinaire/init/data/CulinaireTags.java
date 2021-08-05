package com.hugman.culinaire.init.data;

import com.hugman.culinaire.Culinaire;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class CulinaireTags {

	public static class Blocks {
		public static final Tag<Block> KETTLE_HOT_BLOCKS = register("kettle_hot_blocks");

		private static Tag<Block> register(String name) {
			return TagRegistry.block(Culinaire.MOD_DATA.id(name));
		}

		private static Tag<Block> register(String namespace, String path) {
			return TagRegistry.block(new Identifier(namespace, path));
		}
	}

	public static class Items {
		public static final Tag<Item> SANDWICH_BREAD = register("c", "sandwich/bread");
		public static final Tag<Item> SANDWICH_BLACKLIST = register("c", "sandwich/blacklist");

		private static Tag<Item> register(String name) {
			return TagRegistry.item(Culinaire.MOD_DATA.id(name));
		}

		private static Tag<Item> register(String namespace, String path) {
			return TagRegistry.item(new Identifier(namespace, path));
		}
	}
}
