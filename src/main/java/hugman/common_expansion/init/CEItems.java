package hugman.common_expansion.init;

import hugman.common_expansion.CommonExpansion;
import hugman.common_expansion.init.data.CEFoods;
import hugman.common_expansion.objects.item.SandwichItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CEItems {
	public static final Item CHEESE = register("cheese", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFoods.CHEESE)));
	public static final Item LETTUCE = register("lettuce", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFoods.LETTUCE)));
	public static final Item TOMATO = register("tomato", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFoods.TOMATO)));
	public static final Item CHOCOLATE = register("chocolate", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFoods.CHOCOLATE)));
	public static final Item MARSHMALLOW = register("marshmallow", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFoods.MARSHMALLOW)));
	public static final Item SANDWICH = register("sandwich", new SandwichItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFoods.SANDWICH).maxCount(1)));

	private static Item register(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(CommonExpansion.MOD_ID, name), item);
	}
}
