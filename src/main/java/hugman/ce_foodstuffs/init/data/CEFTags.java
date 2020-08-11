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

		private static Tag<Item> register(String name) {
			return TagRegistry.item(new Identifier(CEFoodstuffs.MOD_ID, name));
		}
	}
}
