package fr.hugman.culinaire.component;

import net.minecraft.world.food.FoodProperties;

public final class CulinaireFoodComponents {
    public static final FoodProperties LETTUCE = simple(2, 0.2F);
    public static final FoodProperties TOMATO = simple(3, 0.5F);

    public static final FoodProperties MILK_BOTTLE = alwaysEdible(0, 0.2F);
    public static final FoodProperties CHEESE = simple(5, 0.5F);

    public static final FoodProperties CHOCOLATE_BOTTLE = alwaysEdible(2, 0.2F);
    public static final FoodProperties CHOCOLATE_BAR = simple(3, 0.2F);
    public static final FoodProperties CHOCOLATE_PIE = simple(6, 0.3F);

    public static final FoodProperties MARSHMALLOW = simple(1, 0.2f);
    public static final FoodProperties TOASTY_MARSHMALLOW = simple(3, 0.4F);
    public static final FoodProperties GOLDEN_MARSHMALLOW = simple(5, 0.3F);
    public static final FoodProperties BURNT_MARSHMALLOW = simple(1, 0F);

    public static final FoodProperties CHOUQUETTE = simple(3, 0.1F);
    public static final FoodProperties CROISSANT = simple(4, 0.3F);
    public static final FoodProperties APPLE_PIE = simple(8, 0.4F);
    public static final FoodProperties SWEET_BERRY_PIE = simple(7, 0.4F);

    public static final FoodProperties SALAD = simple(10, 0.4F);
    public static final FoodProperties MASHED_POTATOES = simple(9, 0.6F);

    public static final FoodProperties SANDWICH = simple(5, 0.7F);

    public static FoodProperties simple(int nutrition, float saturation) {
        return new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build();
    }

    public static FoodProperties alwaysEdible(int nutrition, float saturation) {
        return new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build();
    }
}
