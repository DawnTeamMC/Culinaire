package com.hugman.culinaire.registry.content;

import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.block.CheeseCauldronBlock;
import com.hugman.culinaire.block.CheeseWheelBlock;
import com.hugman.culinaire.block.CulinaireCauldronBehaviors;
import com.hugman.culinaire.block.MilkCauldronBlock;
import com.hugman.culinaire.item.MilkBottleItem;
import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import fr.hugman.dawn.item.DawnItemSettings;
import fr.hugman.dawn.item.ItemGroupHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

public class DairyContent {
	public static final Item MILK_BOTTLE = new MilkBottleItem(new Item.Settings().maxCount(Culinaire.CONFIG.features.milkBottlesMaxCount).recipeRemainder(Items.GLASS_BOTTLE));
	public static final Block MILK_CAULDRON = new MilkCauldronBlock(CulinaireCauldronBehaviors.MILK, DawnBlockSettings.copy(Blocks.CAULDRON).ticksRandomly());

	private static final FoodComponent CHEESE_FOOD = new FoodComponent.Builder().hunger(5).saturationModifier(0.5f).build();

	public static final Item CHEESE = new Item(new DawnItemSettings().food(CHEESE_FOOD).compostingChance(0.5f));
	public static final Block CHEESE_WHEEL = new CheeseWheelBlock(DawnBlockSettings.copy(Blocks.CAKE).item());
	public static final Block CHEESE_CAULDRON = new CheeseCauldronBlock(DawnBlockSettings.copy(Blocks.CAULDRON));

	public static void register(Registrar r) {
		r.add("milk_bottle", MILK_BOTTLE);
		r.add("milk_cauldron", MILK_CAULDRON);

		r.add("cheese", CHEESE);
		r.add("cheese_wheel", CHEESE_WHEEL);
		r.add("cheese_cauldron", CHEESE_CAULDRON);

		ItemGroupHelper.append(ItemGroups.FOOD_AND_DRINK, entries -> entries.addAfter(Items.MILK_BUCKET, MILK_BOTTLE, CHEESE, CHEESE_WHEEL));
	}
}
