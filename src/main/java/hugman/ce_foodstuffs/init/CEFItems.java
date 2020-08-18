package hugman.ce_foodstuffs.init;

import hugman.ce_foodstuffs.CEFoodstuffs;
import hugman.ce_foodstuffs.init.data.CEFFoods;
import hugman.ce_foodstuffs.objects.item.MarshmallowOnAStickItem;
import hugman.ce_foodstuffs.objects.item.SandwichItem;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class CEFItems {
	public static final Item CHEESE = register("cheese", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.CHEESE)));
	public static final Item LETTUCE = register("lettuce", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.LETTUCE)));
	public static final Item LETTUCE_SEEDS = register("lettuce_seeds", new AliasedBlockItem(CEFBlocks.LETTUCE, new Item.Settings().group(ItemGroup.MATERIALS)));
	public static final Item TOMATO = register("tomato", new AliasedBlockItem(CEFBlocks.TOMATOES, new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.TOMATO)));
	public static final Item CHOCOLATE = register("chocolate", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.CHOCOLATE)));
	public static final Item MARSHMALLOW = register("marshmallow", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.UNCOOKED_MARSHMALLOW)));
	public static final Item APPLE_PIE = register("apple_pie", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.APPLE_PIE)));
	public static final Item SWEET_BERRY_PIE = register("sweet_berry_pie", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.SWEET_BERRY_PIE)));
	public static final Item SALAD = register("salad", new Item(new Item.Settings().group(ItemGroup.FOOD).maxCount(1).food(CEFFoods.SALAD)));
	public static final Item MASHED_POTATOES = register("mashed_potatoes", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.MASHED_POTATOES).maxCount(1)));
	public static final Item SANDWICH = register("sandwich", new SandwichItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.SANDWICH_BASE).maxCount(1)));
	public static final Item UNCOOKED_MARSHMALLOW_ON_A_STICK = register("uncooked_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.UNCOOKED_MARSHMALLOW).maxCount(1)));
	public static final Item TOASTY_MARSHMALLOW_ON_A_STICK = register("toasty_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.TOASTY_MARSHMALLOW).maxCount(1)));
	public static final Item GOLDEN_MARSHMALLOW_ON_A_STICK = register("golden_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.GOLDEN_MARSHMALLOW).maxCount(1)));
	public static final Item BURNT_MARSHMALLOW_ON_A_STICK = register("burnt_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.BURNT_MARSHMALLOW).maxCount(1)));

	private static Item register(String name, Item item) {
		return Registry.register(Registry.ITEM, CEFoodstuffs.id(name), item);
	}
}
