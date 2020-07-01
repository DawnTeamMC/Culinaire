package hugman.common_expansion.init.data;

import net.minecraft.item.FoodComponent;

public class CEFoods {
	public static final FoodComponent CHEESE = build(5, 0.4f);
	public static final FoodComponent LETTUCE = build(3, 0.3F);
	public static final FoodComponent TOMATO = build(2, 0.2F);
	public static final FoodComponent SANDWICH = build(4, 0.6F);
	public static final FoodComponent MARSHMALLOW = build(1, 0.2F);

	private static FoodComponent build(int hunger, float saturationModifier) {
		return (new FoodComponent.Builder().hunger(hunger).saturationModifier(saturationModifier).build());
	}
}
