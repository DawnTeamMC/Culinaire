package fr.hugman.culinaire.component;

import net.minecraft.component.type.FoodComponent;

public class CulinaireFoodComponents {
    public static final FoodComponent LETTUCE = simple(2, 0.2F);
    public static final FoodComponent TOMATO = simple(3, 0.5F);

    public static final FoodComponent MILK_BOTTLE = alwaysEdible(0, 0.2F);
    public static final FoodComponent CHEESE = simple(5, 0.5F);

    public static final FoodComponent CHOCOLATE_BOTTLE = alwaysEdible(2, 0.2F);
    public static final FoodComponent CHOCOLATE_BAR = simple(3, 0.2F);
    public static final FoodComponent CHOCOLATE_PIE = simple(6, 0.3F);

    public static final FoodComponent MARSHMALLOW = simple(1, 0.2f);
    public static final FoodComponent TOASTY_MARSHMALLOW = simple(3, 0.4F);
    public static final FoodComponent GOLDEN_MARSHMALLOW = simple(5, 0.3F);
    public static final FoodComponent BURNT_MARSHMALLOW = simple(1, 0F);

    public static final FoodComponent CHOUQUETTE = simple(3, 0.1F);
    public static final FoodComponent CROISSANT = simple(4, 0.3F);
    public static final FoodComponent APPLE_PIE = simple(8, 0.4F);
    public static final FoodComponent SWEET_BERRY_PIE = simple(7, 0.4F);

    public static final FoodComponent SALAD = simple(10, 0.4F);
    public static final FoodComponent MASHED_POTATOES = simple(9, 0.6F);

    public static FoodComponent simple(int nutrition, float saturation) {
        return new FoodComponent.Builder().nutrition(nutrition).saturationModifier(saturation).build();
    }

    public static FoodComponent alwaysEdible(int nutrition, float saturation) {
        return new FoodComponent.Builder().nutrition(nutrition).saturationModifier(saturation).build();
    }
}
