package hugman.common_expansion.init.data;

import net.minecraft.item.FoodComponent;

public class CEFoods
{
	public static final FoodComponent CHEESE = (new FoodComponent.Builder()).hunger(5).saturationModifier(0.4F).build();
	public static final FoodComponent LETTUCE = (new FoodComponent.Builder()).hunger(3).saturationModifier(0.3F).build();
	public static final FoodComponent TOMATO = (new FoodComponent.Builder()).hunger(2).saturationModifier(0.2F).build();
	public static final FoodComponent SANDWICH = (new FoodComponent.Builder()).hunger(4).saturationModifier(0.6F).build();
}
