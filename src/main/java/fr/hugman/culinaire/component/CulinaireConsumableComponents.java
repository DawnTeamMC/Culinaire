package fr.hugman.culinaire.component;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public final class CulinaireConsumableComponents {
    public static final Consumable SNACK_FOOD = Consumables.defaultFood().consumeSeconds(0.8F).build();
    public static final Consumable BURNT_SNACK_FOOD = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 200, 0), 0.2F))
            .build();

    public static final Consumable TEA = Consumables.defaultDrink().consumeSeconds(2.0F).build();

    public static Consumable foodWithEffect(MobEffectInstance effect, float probability) {
        return Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(effect, probability)).build();
    }
}
