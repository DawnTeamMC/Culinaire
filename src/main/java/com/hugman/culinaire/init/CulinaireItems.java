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

public class CulinaireItems extends CulinaireBundle {
	public static final Item CHEESE = add(new ItemCreator.Builder("cheese", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.CHEESE)).compostingChance(0.5f).build());
	public static final Item LETTUCE = add(new ItemCreator.Builder("lettuce", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.LETTUCE)).compostingChance(0.3f).build());
	public static final Item LETTUCE_SEEDS = add(new ItemCreator.Builder("lettuce_seeds", s -> new AliasedBlockItem(CulinaireBlocks.LETTUCE, s), new Item.Settings().group(ItemGroup.MATERIALS)).compostingChance(0.3f).build());
	public static final Item TOMATO = add(new ItemCreator.Builder("tomato", s -> new AliasedBlockItem(CulinaireBlocks.TOMATOES, s),  new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.TOMATO)).compostingChance(0.3f).build());
	public static final Item CHOCOLATE = add(new ItemCreator.Builder("chocolate", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.CHOCOLATE)).compostingChance(0.3f).build());
	public static final Item MARSHMALLOW = add(new ItemCreator.Builder("marshmallow", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.MARSHMALLOW)).compostingChance(0.3f).build());
	public static final Item CHOUQUETTE = add(new ItemCreator.Builder("chouquette",Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.CHOUQUETTE)).compostingChance(0.3f).build());
	public static final Item APPLE_PIE = add(new ItemCreator.Builder("apple_pie", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.APPLE_PIE)).compostingChance(1.0f).build());
	public static final Item SWEET_BERRY_PIE = add(new ItemCreator.Builder("sweet_berry_pie",Item::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.SWEET_BERRY_PIE)).compostingChance(1.0f).build());
	public static final Item SANDWICH = add(new ItemCreator.Builder("sandwich", SandwichItem::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.SANDWICH_BASE).maxCount(1)).compostingChance(1.0f).build());
	public static final SpecialRecipeSerializer<SandwichMakingRecipe> SANDWICH_MAKING = add(new RecipeSerializerCreator<>("sandwich_making", new SpecialRecipeSerializer<>(SandwichMakingRecipe::new)));
	public static final Item SALAD = add(new ItemCreator.Builder("salad", MushroomStewItem::new, new Item.Settings().group(ItemGroup.FOOD).maxCount(1).food(CulinaireFoods.SALAD)).build());
	public static final Item MASHED_POTATOES = add(new ItemCreator.Builder("mashed_potatoes", MushroomStewItem::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.MASHED_POTATOES).maxCount(1)).build());
	public static final Item MARSHMALLOW_ON_A_STICK = add(new ItemCreator.Builder("marshmallow_on_a_stick", MarshmallowOnAStickItem::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK)).build());
	public static final Item TOASTY_MARSHMALLOW_ON_A_STICK = add(new ItemCreator.Builder("toasty_marshmallow_on_a_stick", MarshmallowOnAStickItem::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.TOASTY_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK)).build());
	public static final Item GOLDEN_MARSHMALLOW_ON_A_STICK = add(new ItemCreator.Builder("golden_marshmallow_on_a_stick", MarshmallowOnAStickItem::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.GOLDEN_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK)).build());
	public static final Item BURNT_MARSHMALLOW_ON_A_STICK = add(new ItemCreator.Builder("burnt_marshmallow_on_a_stick", MarshmallowOnAStickItem::new, new Item.Settings().group(ItemGroup.FOOD).food(CulinaireFoods.BURNT_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK)).build());
	public static final Item MILK_BOTTLE = add(new ItemCreator.Builder("milk_bottle", MilkBottleItem::new, new Item.Settings().group(ItemGroup.FOOD).maxCount(1).recipeRemainder(Items.GLASS_BOTTLE)).build());
	public static final Item TEA_BAG = add(new ItemCreator.Builder("tea_bag", TeaBagItem::new, new Item.Settings().group(ItemGroup.FOOD).maxCount(16)).build());
	public static final SpecialRecipeSerializer<TeaBagMakingRecipe> TEA_BAG_MAKING = add(new RecipeSerializerCreator<>("tea_bag_making", new SpecialRecipeSerializer<>(TeaBagMakingRecipe::new)));
	public static final Item TEA_BOTTLE = add(new ItemCreator.Builder("tea_bottle", TeaBottleItem::new, new Item.Settings().group(ItemGroup.FOOD).maxCount(1).recipeRemainder(Items.GLASS_BOTTLE)).build());
	public static final SoundEvent TEA_BOTTLE_FILL_SOUND = creator(new SoundCreator("item.tea_bottle.fill")).getSound();

	public static void init() {
	}
}