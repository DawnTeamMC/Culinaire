package com.hugman.culinaire.init;

import com.hugman.dawn.api.util.CauldronInteractionBuilder;
import com.hugman.dawn.api.util.CauldronUtil;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;

import java.util.Map;

public class CulinaireCauldronBehaviors {
	public static Map<Item, CauldronBehavior> MILK = CauldronBehavior.createMap();
	public static Map<Item, CauldronBehavior> DARK_CHOCOLATE = CauldronBehavior.createMap();
	public static Map<Item, CauldronBehavior> MILK_CHOCOLATE = CauldronBehavior.createMap();
	public static Map<Item, CauldronBehavior> WHITE_CHOCOLATE = CauldronBehavior.createMap();

	public static void init() {
		// Milk Cauldron
		CauldronUtil.addBottleInteractions(MILK, FoodBundle.MILK_CAULDRON, FoodBundle.MILK_BOTTLE);
		CauldronUtil.addBucketInteractions(MILK, FoodBundle.MILK_CAULDRON, Items.MILK_BUCKET);
		MILK.put(Items.SUGAR, CauldronInteractionBuilder.create().addLevel(0).cauldron(FoodBundle.WHITE_CHOCOLATE_CAULDRON).build());

		// Dark Chocolate Cauldron
		CauldronUtil.addBottleInteractions(DARK_CHOCOLATE, FoodBundle.DARK_CHOCOLATE_CAULDRON, FoodBundle.DARK_CHOCOLATE_BOTTLE);
		DARK_CHOCOLATE.put(Items.MILK_BUCKET, CauldronInteractionBuilder.create().addLevel(3).cauldron(FoodBundle.MILK_CHOCOLATE_CAULDRON).sound(SoundEvents.ITEM_BUCKET_EMPTY).build());
		DARK_CHOCOLATE.put(FoodBundle.MILK_BOTTLE, CauldronInteractionBuilder.create().addLevel(1).cauldron(FoodBundle.MILK_CHOCOLATE_CAULDRON).sound(SoundEvents.ITEM_BOTTLE_EMPTY).build());

		// Milk Chocolate Cauldron
		CauldronUtil.addBottleInteractions(MILK_CHOCOLATE, FoodBundle.MILK_CHOCOLATE_CAULDRON, FoodBundle.MILK_CHOCOLATE_BOTTLE);

		// White Chocolate Cauldron
		CauldronUtil.addBottleInteractions(WHITE_CHOCOLATE, FoodBundle.WHITE_CHOCOLATE_CAULDRON, FoodBundle.WHITE_CHOCOLATE_BOTTLE);
		WHITE_CHOCOLATE.put(Items.COCOA_BEANS, CauldronInteractionBuilder.create().addLevel(0).cauldron(FoodBundle.MILK_CHOCOLATE_CAULDRON).build());
	}
}
