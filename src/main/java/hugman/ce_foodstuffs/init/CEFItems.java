package hugman.ce_foodstuffs.init;

import com.hugman.dawn.api.creator.ItemCreator;
import hugman.ce_foodstuffs.init.data.CEFFoods;
import hugman.ce_foodstuffs.objects.item.*;
import net.minecraft.item.*;

public class CEFItems extends CEFPack {
	public static final Item CHEESE = register(new ItemCreator.Builder("cheese", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.CHEESE))));
	public static final Item LETTUCE = register(new ItemCreator.Builder("lettuce", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.LETTUCE))));
	public static final Item LETTUCE_SEEDS = register(new ItemCreator.Builder("lettuce_seeds", new AliasedBlockItem(CEFBlocks.LETTUCE, new Item.Settings().group(ItemGroup.MATERIALS))));
	public static final Item TOMATO = register(new ItemCreator.Builder("tomato", new AliasedBlockItem(CEFBlocks.TOMATOES, new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.TOMATO))));
	public static final Item CHOCOLATE = register(new ItemCreator.Builder("chocolate", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.CHOCOLATE))));
	public static final Item MARSHMALLOW = register(new ItemCreator.Builder("marshmallow", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.UNCOOKED_MARSHMALLOW))));
	public static final Item APPLE_PIE = register(new ItemCreator.Builder("apple_pie", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.APPLE_PIE))));
	public static final Item SWEET_BERRY_PIE = register(new ItemCreator.Builder("sweet_berry_pie", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.SWEET_BERRY_PIE))));
	public static final Item SALAD = register(new ItemCreator.Builder("salad", new MushroomStewItem(new Item.Settings().group(ItemGroup.FOOD).maxCount(1).food(CEFFoods.SALAD))));
	public static final Item MASHED_POTATOES = register(new ItemCreator.Builder("mashed_potatoes", new MushroomStewItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.MASHED_POTATOES).maxCount(1))));
	public static final Item SANDWICH = register(new ItemCreator.Builder("sandwich", new SandwichItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.SANDWICH_BASE).maxCount(1))));
	public static final Item UNCOOKED_MARSHMALLOW_ON_A_STICK = register(new ItemCreator.Builder("uncooked_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.UNCOOKED_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK))));
	public static final Item TOASTY_MARSHMALLOW_ON_A_STICK = register(new ItemCreator.Builder("toasty_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.TOASTY_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK))));
	public static final Item GOLDEN_MARSHMALLOW_ON_A_STICK = register(new ItemCreator.Builder("golden_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.GOLDEN_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK))));
	public static final Item BURNT_MARSHMALLOW_ON_A_STICK = register(new ItemCreator.Builder("burnt_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.FOOD).food(CEFFoods.BURNT_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK))));
	public static final Item MILK_BOTTLE = register(new ItemCreator.Builder("milk_bottle", new MilkBottleItem(new Item.Settings().group(ItemGroup.FOOD).maxCount(1).recipeRemainder(Items.GLASS_BOTTLE))));
	public static final Item TEA_BAG = register(new ItemCreator.Builder("tea_bag", new TeaBagItem(new Item.Settings().group(ItemGroup.FOOD).maxCount(16).recipeRemainder(Items.PAPER))));
	public static final Item TEA_BOTTLE = register(new ItemCreator.Builder("tea_bottle", new TeaBottleItem(new Item.Settings().group(ItemGroup.FOOD).maxCount(1).recipeRemainder(Items.GLASS_BOTTLE))));
}