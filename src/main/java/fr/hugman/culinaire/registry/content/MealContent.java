package fr.hugman.culinaire.registry.content;

import fr.hugman.culinaire.item.SandwichItem;
import fr.hugman.culinaire.recipe.serializer.SandwichRecipeSerializer;
import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.item.DawnItemSettings;
import fr.hugman.dawn.item.ItemGroupHelper;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class MealContent {
    private static final FoodComponent EMPTY_SANDWICH_FOOD = new FoodComponent.Builder().hunger(5).saturationModifier(0.7F).build();
    public static final SandwichRecipeSerializer SANDWICH_CRAFTING = new SandwichRecipeSerializer();
    public static final Item SANDWICH = new SandwichItem(new DawnItemSettings().food(EMPTY_SANDWICH_FOOD).maxCount(1).compostingChance(1.0f));

    public static void register(Registrar r) {
        Registry.register(Registries.RECIPE_SERIALIZER, r.id("crafting/sandwich"), SANDWICH_CRAFTING);
        r.add("sandwich", SANDWICH);

    }
}
