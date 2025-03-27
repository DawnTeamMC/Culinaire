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
    //TODO: translation keys + textures
    public static final RegistryEntry<StatusEffect> STEPPING = of("stepping", new StatusEffect(StatusEffectCategory.BENEFICIAL, 0x3dcc95)
            .addAttributeModifier(EntityAttributes.STEP_HEIGHT, Culinaire.id("effect.stepping"), 0.3F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );
    public static final RegistryEntry<StatusEffect> AGILITY = of("agility", new StatusEffect(StatusEffectCategory.BENEFICIAL, 4866583) //TODO color
            .addAttributeModifier(EntityAttributes.ATTACK_SPEED, Culinaire.id("effect.agility"), 0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );
    public static final RegistryEntry<StatusEffect> KNOCKING = of("knocking", new StatusEffect(StatusEffectCategory.BENEFICIAL, 4866583) //TODO color
            .addAttributeModifier(EntityAttributes.ATTACK_KNOCKBACK, Culinaire.id("effect.knocking"), 0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );
    public static final RegistryEntry<StatusEffect> SPACIAL_HICCUP = of("spatial_hiccup", new TeleportRandomlyStatusEffect(StatusEffectCategory.HARMFUL, 4866583)); //TODO color

    private static RegistryEntry<StatusEffect> of(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Culinaire.id(id), statusEffect);
    }
}
