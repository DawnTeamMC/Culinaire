package hugman.common_expansion.init.data;

import hugman.common_expansion.CommonExpansion;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class CETags {
	public static class Items {
		public static final Tag<Item> SANDWICH_BREAD = register("sandwich_bread");

		private static Tag<Item> register(String name) {
			return TagRegistry.item(new Identifier(CommonExpansion.MOD_ID, name));
		}
	}
}
