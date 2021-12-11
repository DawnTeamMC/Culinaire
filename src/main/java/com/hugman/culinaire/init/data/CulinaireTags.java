package com.hugman.culinaire.init.data;

import com.hugman.culinaire.Culinaire;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class CulinaireTags {

	public static class Blocks {
		public static final Tag<Block> KETTLE_HOT_BLOCKS = register("kettle_hot_blocks");

		private static Tag<Block> register(String name) {
			return TagFactory.BLOCK.create(Culinaire.MOD_DATA.id(name));
		}

		private static Tag<Block> register(String namespace, String path) {
			return TagFactory.BLOCK.create(new Identifier(namespace, path));
		}
	}

	public static class Items {
		public static final Tag<Item> SANDWICH_BREAD = register("c", "sandwich/bread");
		public static final Tag<Item> SANDWICH_BLACKLIST = register("c", "sandwich/blacklist");

		private static Tag<Item> register(String name) {
			return TagFactory.ITEM.create(Culinaire.MOD_DATA.id(name));
		}

		private static Tag<Item> register(String namespace, String path) {
			return TagFactory.ITEM.create(new Identifier(namespace, path));
		}
	}
}
