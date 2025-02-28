package fr.hugman.culinaire.item;

import fr.hugman.culinaire.component.BurnableComponent;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public final class ItemSettings {
    public static Item.Settings max1() {
        return new Item.Settings().maxCount(1);
    }

    public static Item.Settings max16() {
        return new Item.Settings().maxCount(16);
    }

    public static Item.Settings drink(FoodComponent foodComponent) {
        return drink(foodComponent, ConsumableComponents.DRINK);
    }

    public static Item.Settings drink(FoodComponent foodComponent, ConsumableComponent consumableComponent) {
        return new Item.Settings()
                .food(foodComponent, consumableComponent)
                .useRemainder(Items.GLASS_BOTTLE)
                .recipeRemainder(Items.GLASS_BOTTLE)
                .maxCount(1);
    }

    public static Item.Settings foodOnStick(FoodComponent foodComponent, ConsumableComponent consumableComponent) {
        return foodOnStick().food(foodComponent, consumableComponent);
    }


    public static Item.Settings foodOnStick(FoodComponent foodComponent) {
        return foodOnStick().food(foodComponent);
    }

    private static Item.Settings foodOnStick() {
        return new Item.Settings().maxCount(1).useRemainder(Items.STICK);
    }

    public static Item.Settings burnableFoodOnStick(FoodComponent foodComponent, ConsumableComponent consumableComponent, int burningTime, Item burnsInto) {
        return foodOnStick(foodComponent, consumableComponent)
                .component(CulinaireComponentTypes.BURNABLE, new BurnableComponent(burningTime, burnsInto.getRegistryEntry()))
                .component(CulinaireComponentTypes.BURN, 0);
    }

    public static Item.Settings stew(FoodComponent foodComponent) {
        return new Item.Settings().maxCount(1).useRemainder(Items.BOWL).food(foodComponent);
    }
}
