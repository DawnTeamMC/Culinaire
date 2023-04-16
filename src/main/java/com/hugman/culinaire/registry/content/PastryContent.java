package com.hugman.culinaire.registry.content;

import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.item.DawnItemSettings;
import fr.hugman.dawn.item.ItemGroupHelper;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

public class PastryContent {
    public static final FoodComponent CHOUQUETTE_FOOD = new FoodComponent.Builder().hunger(3).saturationModifier(0.1F).snack().build();

    public static final Item CHOUQUETTE = new Item(new DawnItemSettings().food(CHOUQUETTE_FOOD).compostingChance(0.3f));

    private static final FoodComponent APPLE_PIE_FOOD = new FoodComponent.Builder().hunger(8).saturationModifier(0.4F).build();
    private static final FoodComponent SWEET_BERRY_PIE_FOOD = new FoodComponent.Builder().hunger(7).saturationModifier(0.4F).build();

    public static final Item APPLE_PIE = new Item(new DawnItemSettings().food(APPLE_PIE_FOOD).compostingChance(1.0f));
    public static final Item SWEET_BERRY_PIE = new Item(new DawnItemSettings().food(SWEET_BERRY_PIE_FOOD).compostingChance(1.0f));

    public static void register(Registrar r) {
        r.add("chouquette", CHOUQUETTE);
        r.add("apple_pie", APPLE_PIE);
        r.add("sweet_berry_pie", SWEET_BERRY_PIE);

        ItemGroupHelper.append(ItemGroups.FOOD_AND_DRINK, entries -> entries.addAfter(Items.BREAD, CHOUQUETTE));
        ItemGroupHelper.append(ItemGroups.FOOD_AND_DRINK, entries -> entries.addBefore(Items.PUMPKIN_PIE, APPLE_PIE, SWEET_BERRY_PIE));
    }
}
