package com.hugman.culinaire.block;

import com.hugman.culinaire.registry.content.CandyContent;
import com.hugman.culinaire.registry.content.DairyContent;
import fr.hugman.dawn.block.CauldronInteractionBuilder;
import fr.hugman.dawn.block.CauldronUtil;
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
        CauldronUtil.addBottleInteractions(MILK, DairyContent.MILK_CAULDRON, DairyContent.MILK_BOTTLE);
        CauldronUtil.addBucketInteractions(MILK, DairyContent.MILK_CAULDRON, Items.MILK_BUCKET);
        MILK.put(Items.SUGAR, CauldronInteractionBuilder.create().addLevel(0).cauldron(CandyContent.WHITE_CHOCOLATE_CAULDRON).build());

        // Dark Chocolate Cauldron
        CauldronUtil.addBottleInteractions(DARK_CHOCOLATE, CandyContent.DARK_CHOCOLATE_CAULDRON, CandyContent.DARK_CHOCOLATE_BOTTLE);
        DARK_CHOCOLATE.put(Items.MILK_BUCKET, CauldronInteractionBuilder.create().addLevel(3).cauldron(CandyContent.MILK_CHOCOLATE_CAULDRON).sound(SoundEvents.ITEM_BUCKET_EMPTY).build());
        DARK_CHOCOLATE.put(DairyContent.MILK_BOTTLE, CauldronInteractionBuilder.create().addLevel(1).cauldron(CandyContent.MILK_CHOCOLATE_CAULDRON).sound(SoundEvents.ITEM_BOTTLE_EMPTY).build());

        // Milk Chocolate Cauldron
        CauldronUtil.addBottleInteractions(MILK_CHOCOLATE, CandyContent.MILK_CHOCOLATE_CAULDRON, CandyContent.MILK_CHOCOLATE_BOTTLE);

        // White Chocolate Cauldron
        CauldronUtil.addBottleInteractions(WHITE_CHOCOLATE, CandyContent.WHITE_CHOCOLATE_CAULDRON, CandyContent.WHITE_CHOCOLATE_BOTTLE);
        WHITE_CHOCOLATE.put(Items.COCOA_BEANS, CauldronInteractionBuilder.create().addLevel(0).cauldron(CandyContent.MILK_CHOCOLATE_CAULDRON).build());
    }
}
