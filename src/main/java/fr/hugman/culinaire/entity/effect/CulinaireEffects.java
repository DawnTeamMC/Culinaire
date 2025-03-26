package fr.hugman.culinaire.entity.effect;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class CulinaireEffects {
    public static final RegistryEntry<StatusEffect> HIGH_STEPS = of(
            "high_steps",
            new StatusEffect(StatusEffectCategory.BENEFICIAL, 0x3dcc95)
                    .addAttributeModifier(EntityAttributes.STEP_HEIGHT, Culinaire.id("effect.high_steps"), 0.5F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    ); //TODO: translation key + texture

    private static RegistryEntry<StatusEffect> of(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Culinaire.id(id), statusEffect);
    }
}
