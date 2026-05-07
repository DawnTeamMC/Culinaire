package fr.hugman.culinaire.mixin;

import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.item.sandwich.SandwichIngredient;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodProperties.class)
public class FoodPropertiesMixin {
    @Unique
    private static ItemStack culinaire$lastStack;

    @Inject(method = "onConsume", at = @At("HEAD"))
    public void culinaire$captureStack(Level level, LivingEntity user, ItemStack stack, Consumable consumable, CallbackInfo ci) {
        culinaire$lastStack = stack;
    }

    @ModifyArg(method = "onConsume", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/food/FoodData;eat(Lnet/minecraft/world/food/FoodProperties;)V"
    ), index = 0)
    public FoodProperties culinaire$modifyFoodProperties(FoodProperties properties) {
        if (culinaire$lastStack != null) {
            var sandwichIngredients = culinaire$lastStack.get(CulinaireComponentTypes.SANDWICH_INGREDIENTS);
            if (sandwichIngredients != null) {
                //TODO: have a custom int flooring method in the config
                int nutrition = (int) sandwichIngredients.calculateValue(properties.nutrition(), SandwichIngredient::nutritionModifier);
                float saturation = sandwichIngredients.calculateValue(properties.saturation(), SandwichIngredient::saturationModifier);
                return new FoodProperties(nutrition, saturation, properties.canAlwaysEat());
            }
        }
        return properties;
    }
}
