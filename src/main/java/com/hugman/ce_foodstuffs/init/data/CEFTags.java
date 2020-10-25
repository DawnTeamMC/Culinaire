package com.hugman.ce_foodstuffs.init.data;

import com.hugman.ce_foodstuffs.CEFoodstuffs;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public class CEFTags {
	public static class Items {
		public static final Tag<Item> SANDWICH_BREAD = register("sandwich/bread");
		public static final Tag<Item> SANDWICH_BLACKLIST = register("sandwich/blacklist");

		private static Tag<Item> register(String name) {
			return TagRegistry.item(CEFoodstuffs.MOD_DATA.id(name));
		}
	}
}
