package com.hugman.culinaire.init;

import com.hugman.culinaire.init.data.CulinaireFoods;
import com.hugman.culinaire.objects.block.CheeseCauldronBlock;
import com.hugman.culinaire.objects.block.CheeseWheelBlock;
import com.hugman.culinaire.objects.block.LettuceBlock;
import com.hugman.culinaire.objects.block.MilkCauldronBlock;
import com.hugman.culinaire.objects.block.TomatoesBlock;
import com.hugman.culinaire.objects.item.MarshmallowOnAStickItem;
import com.hugman.culinaire.objects.item.MilkBottleItem;
import com.hugman.culinaire.objects.item.SandwichItem;
import com.hugman.culinaire.objects.recipe.SandwichMakingRecipe;
import com.hugman.dawn.api.creator.BlockCreator;
import com.hugman.dawn.api.creator.ItemCreator;
import com.hugman.dawn.api.creator.RecipeSerializerCreator;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.MushroomStewItem;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.sound.BlockSoundGroup;

public class CulinaireFoodBundle extends CulinaireBundle {
	public static final Item MILK_BOTTLE = add(new ItemCreator.Builder("milk_bottle", MilkBottleItem::new, new Item.Settings().group(ItemGroup.FOOD).maxCount(1).recipeRemainder(Items.GLASS_BOTTLE)).build());
	public static final Block MILK_CAULDRON = add(new BlockCreator.Builder("milk_cauldron", s -> new MilkCauldronBlock(s, CulinaireCauldronBehaviors.MILK_CAULDRON_BEHAVIOR), FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().ticksRandomly().strength(2.0F).nonOpaque()).noItem().build());
	public static final Block CHEESE_CAULDRON = add(new BlockCreator.Builder("cheese_cauldron", s -> new CheeseCauldronBlock(s, CulinaireCauldronBehaviors.CHEESE_CAULDRON_BEHAVIOR), FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0F).nonOpaque()).noItem().build());
	public static final Item CHEESE = add(new ItemCreator.Builder("cheese", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.CHEESE)).compostingChance(0.5f).build());
	public static final Block CHEESE_WHEEL = add(new BlockCreator.Builder("cheese_wheel", CheeseWheelBlock::new, FabricBlockSettings.of(Material.CAKE).strength(0.5F).sounds(BlockSoundGroup.WOOL)).itemGroup(ItemGroup.FOOD).build());

	public static final Block LETTUCE_BLOCK = add(new BlockCreator.Builder("lettuce", LettuceBlock::new, FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)).render(BlockCreator.Render.CUTOUT).noItem().build());
	public static final Item LETTUCE_SEEDS = add(new ItemCreator.Builder("lettuce_seeds", s -> new AliasedBlockItem(LETTUCE_BLOCK, s), new Item.Settings().group(ItemGroup.MATERIALS)).compostingChance(0.3f).build());
	public static final Item LETTUCE = add(new ItemCreator.Builder("lettuce", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.LETTUCE)).compostingChance(0.3f).build());
	public static final Block TOMATO_BLOCK = add(new BlockCreator.Builder("tomatoes", TomatoesBlock::new, FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)).render(BlockCreator.Render.CUTOUT).noItem().build());
	public static final Item TOMATO = add(new ItemCreator.Builder("tomato", s -> new AliasedBlockItem(TOMATO_BLOCK, s), new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.TOMATO)).compostingChance(0.3f).build());
	public static final Item CHOCOLATE = add(new ItemCreator.Builder("chocolate", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.CHOCOLATE)).compostingChance(0.3f).build());

	public static final Item CHOUQUETTE = add(new ItemCreator.Builder("chouquette", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.CHOUQUETTE)).compostingChance(0.3f).build());

	public static final Item MARSHMALLOW = add(new ItemCreator.Builder("marshmallow", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.MARSHMALLOW)).compostingChance(0.3f).build());
	public static final Item MARSHMALLOW_ON_A_STICK = add(new ItemCreator.Builder("marshmallow_on_a_stick", MarshmallowOnAStickItem::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK)).build());
	public static final Item TOASTY_MARSHMALLOW_ON_A_STICK = add(new ItemCreator.Builder("toasty_marshmallow_on_a_stick", MarshmallowOnAStickItem::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.TOASTY_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK)).build());
	public static final Item GOLDEN_MARSHMALLOW_ON_A_STICK = add(new ItemCreator.Builder("golden_marshmallow_on_a_stick", MarshmallowOnAStickItem::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.GOLDEN_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK)).build());
	public static final Item BURNT_MARSHMALLOW_ON_A_STICK = add(new ItemCreator.Builder("burnt_marshmallow_on_a_stick", MarshmallowOnAStickItem::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.BURNT_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK)).build());

	public static final Item SANDWICH = add(new ItemCreator.Builder("sandwich", SandwichItem::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.SANDWICH_BASE).maxCount(1)).compostingChance(1.0f).build());
	public static final SpecialRecipeSerializer<SandwichMakingRecipe> SANDWICH_MAKING = add(new RecipeSerializerCreator<>("sandwich_making", new SpecialRecipeSerializer<>(SandwichMakingRecipe::new)));

	public static final Item APPLE_PIE = add(new ItemCreator.Builder("apple_pie", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.APPLE_PIE)).compostingChance(1.0f).build());
	public static final Item SWEET_BERRY_PIE = add(new ItemCreator.Builder("sweet_berry_pie", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.SWEET_BERRY_PIE)).compostingChance(1.0f).build());
	public static final Item SALAD = add(new ItemCreator.Builder("salad", MushroomStewItem::new, new Item.Settings().group(ItemGroup.FOOD).maxCount(1).food(CulinaireFoods.SALAD)).build());
	public static final Item MASHED_POTATOES = add(new ItemCreator.Builder("mashed_potatoes", MushroomStewItem::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.MASHED_POTATOES).maxCount(1)).build());


	public static void init() {
	}
}