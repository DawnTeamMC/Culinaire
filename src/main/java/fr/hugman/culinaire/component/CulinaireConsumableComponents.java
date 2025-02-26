package fr.hugman.culinaire.component;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;

public class CulinaireConsumableComponents {
    public static final ConsumableComponent SNACK_FOOD = ConsumableComponents.food().consumeSeconds(0.8F).build();
    public static final ConsumableComponent BURNT_SNACK_FOOD = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 200, 0), 0.2F))
            .build();

    public static ConsumableComponent foodWithEffect(StatusEffectInstance effect, float probability) {
        return ConsumableComponents.food().consumeEffect(new ApplyEffectsConsumeEffect(effect, probability)).build();
    }
}
