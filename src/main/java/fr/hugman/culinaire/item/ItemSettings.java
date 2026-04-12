package fr.hugman.culinaire.item;

import fr.hugman.culinaire.component.BurnableComponent;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;

public final class ItemSettings {
    public static Item.Properties max1() {
        return new Item.Properties().stacksTo(1);
    }

    public static Item.Properties max16() {
        return new Item.Properties().stacksTo(16);
    }

    public static Item.Properties drink(FoodProperties foodComponent) {
        return drink(foodComponent, Consumables.DEFAULT_DRINK);
    }

    public static Item.Properties drink(FoodProperties foodComponent, Consumable consumableComponent) {
        return new Item.Properties()
                .food(foodComponent, consumableComponent)
                .usingConvertsTo(Items.GLASS_BOTTLE)
                .craftRemainder(Items.GLASS_BOTTLE)
                .stacksTo(1);
    }

    public static Item.Properties foodOnStick(FoodProperties foodComponent, Consumable consumableComponent) {
        return foodOnStick().food(foodComponent, consumableComponent);
    }


    public static Item.Properties foodOnStick(FoodProperties foodComponent) {
        return foodOnStick().food(foodComponent);
    }

    private static Item.Properties foodOnStick() {
        return new Item.Properties().stacksTo(1).usingConvertsTo(Items.STICK);
    }

    public static Item.Properties burnableFoodOnStick(FoodProperties foodComponent, Consumable consumableComponent, int burningTime, Item burnsInto) {
        return foodOnStick(foodComponent, consumableComponent)
                .component(CulinaireComponentTypes.BURNABLE, new BurnableComponent(burningTime, burnsInto.builtInRegistryHolder()))
                .component(CulinaireComponentTypes.BURN, 0);
    }

    public static Item.Properties stew(FoodProperties foodComponent) {
        return new Item.Properties().stacksTo(1).usingConvertsTo(Items.BOWL).food(foodComponent);
    }
}
