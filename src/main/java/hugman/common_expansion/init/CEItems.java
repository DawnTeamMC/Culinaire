package hugman.common_expansion.init;

import hugman.common_expansion.CommonExpansion;
import hugman.common_expansion.init.data.CEFoods;
import hugman.common_expansion.objects.item.MarshmallowOnAStickItem;
import hugman.common_expansion.objects.item.SandwichItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CEItems {
	public static final Item CHEESE = register("cheese", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFoods.CHEESE)));
	public static final Item LETTUCE = register("lettuce", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFoods.LETTUCE)));
	public static final Item TOMATO = register("tomato", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFoods.TOMATO)));
	public static final Item CHOCOLATE = register("chocolate", new Item(new Item.Settings().group(ItemGroup.FOOD)));
	public static final Item MARSHMALLOW = register("marshmallow", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFoods.MARSHMALLOW)));
	public static final Item APPLE_PIE = register("apple_pie", new Item(new Item.Settings().group(ItemGroup.FOOD)));
	public static final Item SWEET_BERRY_PIE = register("sweet_berry_pie", new Item(new Item.Settings().group(ItemGroup.FOOD)));
	public static final Item SALAD = register("salad", new Item(new Item.Settings().group(ItemGroup.FOOD).maxCount(1)));
	public static final Item MASHED_POTATOES = register("mashed_potatoes", new Item(new Item.Settings().group(ItemGroup.FOOD).maxCount(1)));
	public static final Item SANDWICH = register("sandwich", new SandwichItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFoods.SANDWICH).maxCount(1)));
	public static final Item MARSHMALLOW_ON_A_STICK = register("marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFoods.MARSHMALLOW).maxCount(1)));

	private static Item register(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(CommonExpansion.MOD_ID, name), item);
	}
}
