package com.hugman.culinaire.init;

import com.hugman.culinaire.init.data.CulinaireFoods;
import com.hugman.culinaire.objects.item.MarshmallowOnAStickItem;
import com.hugman.culinaire.objects.item.MilkBottleItem;
import com.hugman.culinaire.objects.item.SandwichItem;
import com.hugman.culinaire.objects.item.TeaBagItem;
import com.hugman.culinaire.objects.item.TeaBottleItem;
import com.hugman.culinaire.objects.recipe.SandwichMakingRecipe;
import com.hugman.culinaire.objects.recipe.TeaBagMakingRecipe;
import com.hugman.dawn.api.creator.ItemCreator;
import com.hugman.dawn.api.creator.RecipeSerializerCreator;
import com.hugman.dawn.api.creator.SoundCreator;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.MushroomStewItem;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.sound.SoundEvent;

public class CulinaireItems extends CulinairePack {
	public static final Item CHEESE = register(new ItemCreator.Builder("cheese", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.CHEESE))).compostingChance(0.5f));
	public static final Item LETTUCE = register(new ItemCreator.Builder("lettuce", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.LETTUCE))).compostingChance(0.3f));
	public static final Item LETTUCE_SEEDS = register(new ItemCreator.Builder("lettuce_seeds", new AliasedBlockItem(CulinaireBlocks.LETTUCE, new Item.Settings().group(ItemGroup.MATERIALS))).compostingChance(0.3f));
	public static final Item TOMATO = register(new ItemCreator.Builder("tomato", new AliasedBlockItem(CulinaireBlocks.TOMATOES, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.TOMATO))).compostingChance(0.3f));
	public static final Item CHOCOLATE = register(new ItemCreator.Builder("chocolate", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.CHOCOLATE))).compostingChance(0.3f));
	public static final Item MARSHMALLOW = register(new ItemCreator.Builder("marshmallow", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.MARSHMALLOW))).compostingChance(0.3f));
	public static final Item CHOUQUETTE = register(new ItemCreator.Builder("chouquette", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.CHOUQUETTE))).compostingChance(0.3f));
	public static final Item APPLE_PIE = register(new ItemCreator.Builder("apple_pie", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.APPLE_PIE))).compostingChance(1.0f));
	public static final Item SWEET_BERRY_PIE = register(new ItemCreator.Builder("sweet_berry_pie", new Item(new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.SWEET_BERRY_PIE))).compostingChance(1.0f));
	public static final Item SANDWICH = register(new ItemCreator.Builder("sandwich", new SandwichItem(new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.SANDWICH_BASE).maxCount(1))).compostingChance(1.0f));
	public static final SpecialRecipeSerializer<SandwichMakingRecipe> SANDWICH_MAKING = register(new RecipeSerializerCreator.Builder<>("sandwich_making", new SpecialRecipeSerializer<>(SandwichMakingRecipe::new)));
	public static final Item SALAD = register(new ItemCreator.Builder("salad", new MushroomStewItem(new Item.Settings().group(ItemGroup.FOOD).maxCount(1).food(CulinaireFoods.SALAD))));
	public static final Item MASHED_POTATOES = register(new ItemCreator.Builder("mashed_potatoes", new MushroomStewItem(new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.MASHED_POTATOES).maxCount(1))));
	public static final Item MARSHMALLOW_ON_A_STICK = register(new ItemCreator.Builder("marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK))));
	public static final Item TOASTY_MARSHMALLOW_ON_A_STICK = register(new ItemCreator.Builder("toasty_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.TOASTY_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK))));
	public static final Item GOLDEN_MARSHMALLOW_ON_A_STICK = register(new ItemCreator.Builder("golden_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.GOLDEN_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK))));
	public static final Item BURNT_MARSHMALLOW_ON_A_STICK = register(new ItemCreator.Builder("burnt_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.BURNT_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK))));
	public static final Item MILK_BOTTLE = register(new ItemCreator.Builder("milk_bottle", new MilkBottleItem(new Item.Settings().group(ItemGroup.FOOD).maxCount(1).recipeRemainder(Items.GLASS_BOTTLE))));
	public static final Item TEA_BAG = register(new ItemCreator.Builder("tea_bag", new TeaBagItem(new Item.Settings().group(ItemGroup.FOOD).maxCount(16))));
	public static final SpecialRecipeSerializer<TeaBagMakingRecipe> TEA_BAG_MAKING = register(new RecipeSerializerCreator.Builder<>("tea_bag_making", new SpecialRecipeSerializer<>(TeaBagMakingRecipe::new)));
	public static final Item TEA_BOTTLE = register(new ItemCreator.Builder("tea_bottle", new TeaBottleItem(new Item.Settings().group(ItemGroup.FOOD).maxCount(1).recipeRemainder(Items.GLASS_BOTTLE))));
	public static final SoundEvent TEA_BOTTLE_FILL_SOUND = register(new SoundCreator.Builder("item.tea_bottle.fill"));

	public static void init() {
	}
}