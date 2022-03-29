package com.hugman.culinaire.init.data;

import com.hugman.culinaire.Culinaire;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class CulinaireTags {

	public static class Blocks {
		public static final TagKey<Block> KETTLE_HOT_BLOCKS = register("kettle_hot_blocks");

		private static TagKey<Block> register(String name) {
			return TagKey.of(Registry.BLOCK_KEY, Culinaire.MOD_DATA.id(name));
		}
	}

	public static class Items {
		private static TagKey<Item> register(String name) {
			return TagKey.of(Registry.ITEM_KEY, Culinaire.MOD_DATA.id(name));
		}
	}
}
