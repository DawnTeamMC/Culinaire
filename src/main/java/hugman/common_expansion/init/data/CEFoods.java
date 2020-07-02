package hugman.common_expansion.init.data;

import net.minecraft.item.FoodComponent;

public class CEFoods {
	public static final FoodComponent CHEESE = build(5, 0.4f);
	public static final FoodComponent LETTUCE = build(2, 0.2F);
	public static final FoodComponent TOMATO = build(3, 0.2F);
	public static final FoodComponent SANDWICH_BASE = build(5, 0.625F);
	public static final FoodComponent UNCOOKED_MARSHMALLOW = build(2, 0.1F, true);
	public static final FoodComponent TOASTY_MARSHMALLOW = build(3, 0.1F, true);
	public static final FoodComponent GOLDEN_MARSHMALLOW = build(4, 0.15F, true);
	public static final FoodComponent BURNT_MARSHMALLOW = build(1, 0.1F, true);
	public static final FoodComponent CHOCOLATE = build(2, 0.1F);
	public static final FoodComponent APPLE_PIE = build(9, 0.15F);
	public static final FoodComponent SWEET_BERRY_PIE = build(7, 0.15F);
	public static final FoodComponent SALAD_BASE = build(2, 0.75F);
	public static final FoodComponent MASHED_POTATOES = build(7, 0.6F);

	private static FoodComponent build(int hunger, float saturationModifier) {
		return build(hunger, saturationModifier, false);
	}

	private static FoodComponent build(int hunger, float saturationModifier, boolean isSnack) {
		FoodComponent.Builder builder = new FoodComponent.Builder();
		builder.hunger(hunger);
		builder.saturationModifier(saturationModifier);
		if(isSnack) {
			builder.snack();
		}
		return builder.build();
	}
}
