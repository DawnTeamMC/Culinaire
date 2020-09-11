package hugman.ce_foodstuffs.init.data;

import hugman.ce_foodstuffs.CEFoodstuffs;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class CEFTags {
	public static class Items {
		public static final Tag<Item> SANDWICH_BREAD = register("sandwich/bread");
		public static final Tag<Item> SANDWICH_BLACKLIST = register("sandwich/blacklist");
		public static final Tag<Item> TEA_POTENCY_1 = register("tea_ingredients/potency/1");
		public static final Tag<Item> TEA_POTENCY_2 = register("tea_ingredients/potency/2");
		public static final Tag<Item> TEA_POTENCY_3 = register("tea_ingredients/potency/3");
		public static final Tag<Item> TEA_POTENCY_1_AND_1 = register("tea_ingredients/potency/1_and_1");
		public static final Tag<Item> PAPER = register("c", "paper");
		public static final Tag<Item> STRING = register("c", "string");

		private static Tag<Item> register(String name) {
			return TagRegistry.item(new Identifier(CEFoodstuffs.MOD_ID, name));
		}

		private static Tag<Item> register(String id, String name) {
			return TagRegistry.item(new Identifier(id, name));
		}
	}
}
