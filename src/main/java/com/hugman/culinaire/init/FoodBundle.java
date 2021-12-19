package com.hugman.culinaire.init;

import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.objects.block.CheeseCauldronBlock;
import com.hugman.culinaire.objects.block.CheeseWheelBlock;
import com.hugman.culinaire.objects.block.LettuceBlock;
import com.hugman.culinaire.objects.block.MilkCauldronBlock;
import com.hugman.culinaire.objects.block.TomatoesBlock;
import com.hugman.culinaire.objects.item.ChocolateBottleItem;
import com.hugman.culinaire.objects.item.MarshmallowOnAStickItem;
import com.hugman.culinaire.objects.item.MilkBottleItem;
import com.hugman.culinaire.objects.item.SandwichItem;
import com.hugman.culinaire.objects.recipe.SandwichRecipe;
import com.hugman.culinaire.objects.recipe.serializer.SandwichRecipeSerializer;
import com.hugman.dawn.api.creator.BlockCreator;
import com.hugman.dawn.api.creator.ItemCreator;
import com.hugman.dawn.api.creator.RecipeSerializerCreator;
import com.hugman.dawn.api.object.block.ThreeLeveledCauldronBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.StewItem;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.sound.BlockSoundGroup;

public class FoodBundle extends CulinaireBundle {
	public static final Item MILK_BOTTLE = add(new ItemCreator.Builder("milk_bottle", MilkBottleItem::new, new Item.Settings().group(ItemGroup.FOOD).maxCount(Culinaire.CONFIG.features.milkBottlesMaxCount).recipeRemainder(Items.GLASS_BOTTLE)).build());
	public static final Block MILK_CAULDRON = add(new BlockCreator.Builder("milk_cauldron", s -> new MilkCauldronBlock(s, CulinaireCauldronBehaviors.MILK), FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().ticksRandomly().strength(2.0F).nonOpaque()).noItem().build());
	public static final Block CHEESE_CAULDRON = add(new BlockCreator.Builder("cheese_cauldron", CheeseCauldronBlock::new, FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0F).nonOpaque()).noItem().build());
	public static final Item CHEESE = add(new ItemCreator.Builder("cheese", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.CHEESE)).compostingChance(0.5f).build());
	public static final Block CHEESE_WHEEL = add(new BlockCreator.Builder("cheese_wheel", CheeseWheelBlock::new, FabricBlockSettings.of(Material.CAKE).strength(0.5F).sounds(BlockSoundGroup.WOOL)).itemGroup(ItemGroup.FOOD).build());

	public static final Item DARK_CHOCOLATE_BOTTLE = add(new ItemCreator.Builder("dark_chocolate_bottle", ChocolateBottleItem::new, new Item.Settings().group(ItemGroup.FOOD).recipeRemainder(Items.GLASS_BOTTLE)).build());
	public static final Item MILK_CHOCOLATE_BOTTLE = add(new ItemCreator.Builder("milk_chocolate_bottle", ChocolateBottleItem::new, new Item.Settings().group(ItemGroup.FOOD).recipeRemainder(Items.GLASS_BOTTLE)).build());
	public static final Item WHITE_CHOCOLATE_BOTTLE = add(new ItemCreator.Builder("white_chocolate_bottle", ChocolateBottleItem::new, new Item.Settings().group(ItemGroup.FOOD).recipeRemainder(Items.GLASS_BOTTLE)).build());

	public static final Item DARK_CHOCOLATE_BAR = add(new ItemCreator.Builder("dark_chocolate_bar", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.CHOCOLATE_BAR)).build());
	public static final Item MILK_CHOCOLATE_BAR = add(new ItemCreator.Builder("milk_chocolate_bar", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.CHOCOLATE_BAR)).build());
	public static final Item WHITE_CHOCOLATE_BAR = add(new ItemCreator.Builder("white_chocolate_bar", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.CHOCOLATE_BAR)).build());

	public static final Item DARK_CHOCOLATE_PIE = add(new ItemCreator.Builder("dark_chocolate_pie", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.CHOCOLATE_PIE)).build());
	public static final Item MILK_CHOCOLATE_PIE = add(new ItemCreator.Builder("milk_chocolate_pie", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.CHOCOLATE_PIE)).build());
	public static final Item WHITE_CHOCOLATE_PIE = add(new ItemCreator.Builder("white_chocolate_pie", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.CHOCOLATE_PIE)).build());

	public static final Block DARK_CHOCOLATE_CAULDRON = add(new BlockCreator.Builder("dark_chocolate_cauldron", s -> new ThreeLeveledCauldronBlock(s, CulinaireCauldronBehaviors.DARK_CHOCOLATE), FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0F).nonOpaque()).noItem().build());
	public static final Block MILK_CHOCOLATE_CAULDRON = add(new BlockCreator.Builder("milk_chocolate_cauldron", s -> new ThreeLeveledCauldronBlock(s, CulinaireCauldronBehaviors.MILK_CHOCOLATE), FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0F).nonOpaque()).noItem().build());
	public static final Block WHITE_CHOCOLATE_CAULDRON = add(new BlockCreator.Builder("white_chocolate_cauldron", s -> new ThreeLeveledCauldronBlock(s, CulinaireCauldronBehaviors.WHITE_CHOCOLATE), FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0F).nonOpaque()).noItem().build());

	public static final Block LETTUCE_BLOCK = add(new BlockCreator.Builder("lettuce", LettuceBlock::new, FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)).render(BlockCreator.Render.CUTOUT).noItem().build());
	public static final Item LETTUCE_SEEDS = add(new ItemCreator.Builder("lettuce_seeds", s -> new AliasedBlockItem(LETTUCE_BLOCK, s), new Item.Settings().group(ItemGroup.MATERIALS)).compostingChance(0.3f).build());
	public static final Item LETTUCE = add(new ItemCreator.Builder("lettuce", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.LETTUCE)).compostingChance(0.3f).build());
	public static final Block TOMATO_BLOCK = add(new BlockCreator.Builder("tomatoes", TomatoesBlock::new, FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)).render(BlockCreator.Render.CUTOUT).noItem().build());
	public static final Item TOMATO = add(new ItemCreator.Builder("tomato", s -> new AliasedBlockItem(TOMATO_BLOCK, s), new Item.Settings().group(ItemGroup.FOOD).food(Components.TOMATO)).compostingChance(0.3f).build());

	public static final Item CHOUQUETTE = add(new ItemCreator.Builder("chouquette", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.CHOUQUETTE)).compostingChance(0.3f).build());

	public static final Item MARSHMALLOW = add(new ItemCreator.Builder("marshmallow", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.MARSHMALLOW)).compostingChance(0.3f).build());
	public static final Item MARSHMALLOW_ON_A_STICK = add(new ItemCreator.Builder("marshmallow_on_a_stick", MarshmallowOnAStickItem::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK)).build());
	public static final Item TOASTY_MARSHMALLOW_ON_A_STICK = add(new ItemCreator.Builder("toasty_marshmallow_on_a_stick", MarshmallowOnAStickItem::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.TOASTY_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK)).build());
	public static final Item GOLDEN_MARSHMALLOW_ON_A_STICK = add(new ItemCreator.Builder("golden_marshmallow_on_a_stick", MarshmallowOnAStickItem::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.GOLDEN_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK)).build());
	public static final Item BURNT_MARSHMALLOW_ON_A_STICK = add(new ItemCreator.Builder("burnt_marshmallow_on_a_stick", MarshmallowOnAStickItem::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.BURNT_MARSHMALLOW).maxCount(1).recipeRemainder(Items.STICK)).build());

	public static final RecipeSerializer<SandwichRecipe> SANDWICH_CRAFTING = add(new RecipeSerializerCreator<>("crafting/sandwich", new SandwichRecipeSerializer()));
	public static final Item SANDWICH = add(new ItemCreator.Builder("sandwich", SandwichItem::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.EMPTY_SANDWICH).maxCount(1)).compostingChance(1.0f).build());

	public static final Item APPLE_PIE = add(new ItemCreator.Builder("apple_pie", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.APPLE_PIE)).compostingChance(1.0f).build());
	public static final Item SWEET_BERRY_PIE = add(new ItemCreator.Builder("sweet_berry_pie", Item::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.SWEET_BERRY_PIE)).compostingChance(1.0f).build());
	public static final Item SALAD = add(new ItemCreator.Builder("salad", StewItem::new, new Item.Settings().group(ItemGroup.FOOD).maxCount(1).food(Components.SALAD)).build());
	public static final Item MASHED_POTATOES = add(new ItemCreator.Builder("mashed_potatoes", StewItem::new, new Item.Settings().group(ItemGroup.FOOD).food(Components.MASHED_POTATOES).maxCount(1)).build());

	public static class Components {
		public static final FoodComponent CHEESE = new FoodComponent.Builder().hunger(5).saturationModifier(0.5f).build();

		public static final FoodComponent CHOCOLATE_PIE = new FoodComponent.Builder().hunger(6).saturationModifier(0.3F).build();
		public static final FoodComponent CHOCOLATE_BAR = new FoodComponent.Builder().hunger(3).saturationModifier(0.2F).build();

		public static final FoodComponent LETTUCE = new FoodComponent.Builder().hunger(2).saturationModifier(0.2F).build();
		public static final FoodComponent TOMATO = new FoodComponent.Builder().hunger(3).saturationModifier(0.5F).build();

		public static final FoodComponent CHOUQUETTE = new FoodComponent.Builder().hunger(3).saturationModifier(0.1F).snack().build();
		
		public static final FoodComponent EMPTY_SANDWICH = new FoodComponent.Builder().hunger(5).saturationModifier(0.7F).build();

		public static final FoodComponent MARSHMALLOW = new FoodComponent.Builder().hunger(1).saturationModifier(0.2F).snack().build();
		public static final FoodComponent TOASTY_MARSHMALLOW = new FoodComponent.Builder().hunger(3).saturationModifier(0.4F).snack().build();
		public static final FoodComponent GOLDEN_MARSHMALLOW = new FoodComponent.Builder().hunger(5).saturationModifier(0.3F).snack().build();
		public static final FoodComponent BURNT_MARSHMALLOW = new FoodComponent.Builder().hunger(1).saturationModifier(0F).snack().statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 200), 0.2f).build();

		public static final FoodComponent APPLE_PIE = new FoodComponent.Builder().hunger(8).saturationModifier(0.4F).build();
		public static final FoodComponent SWEET_BERRY_PIE = new FoodComponent.Builder().hunger(7).saturationModifier(0.4F).build();
		public static final FoodComponent SALAD = new FoodComponent.Builder().hunger(10).saturationModifier(0.4F).build();
		public static final FoodComponent MASHED_POTATOES = new FoodComponent.Builder().hunger(9).saturationModifier(0.6F).build();
	}
}