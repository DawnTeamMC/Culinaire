package com.hugman.culinaire.registry.content;

import com.hugman.culinaire.item.SandwichItem;
import com.hugman.culinaire.recipe.serializer.SandwichRecipeSerializer;
import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.item.DawnItemSettings;
import fr.hugman.dawn.item.ItemGroupHelper;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class MealContent {
    private static final FoodComponent SALAD_FOOD = new FoodComponent.Builder().hunger(10).saturationModifier(0.4F).build();
    private static final FoodComponent MASHED_POTATOES_FOOD = new FoodComponent.Builder().hunger(9).saturationModifier(0.6F).build();

    public static final Item SALAD = new StewItem(new Item.Settings().maxCount(1).food(SALAD_FOOD));
    public static final Item MASHED_POTATOES = new StewItem(new Item.Settings().food(MASHED_POTATOES_FOOD).maxCount(1));

    private static final FoodComponent EMPTY_SANDWICH_FOOD = new FoodComponent.Builder().hunger(5).saturationModifier(0.7F).build();
    public static final SandwichRecipeSerializer SANDWICH_CRAFTING = new SandwichRecipeSerializer();
    public static final Item SANDWICH = new SandwichItem(new DawnItemSettings().food(EMPTY_SANDWICH_FOOD).maxCount(1).compostingChance(1.0f));

    public static void register(Registrar r) {
        r.add("salad", SALAD);
        r.add("mashed_potatoes", MASHED_POTATOES);

        Registry.register(Registries.RECIPE_SERIALIZER, r.id("crafting/sandwich"), SANDWICH_CRAFTING); //TODO: add a method for recipe serializers to Dawn API
        r.add("sandwich", SANDWICH);

        ItemGroupHelper.append(ItemGroups.FOOD_AND_DRINK, entries -> entries.addBefore(Items.MUSHROOM_STEW, SALAD, MASHED_POTATOES));
        //TODO: add sandwich to food and drink group
    }
}
