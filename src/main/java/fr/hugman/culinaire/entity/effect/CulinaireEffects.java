package fr.hugman.culinaire.entity.effect;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class CulinaireEffects {
    //TODO: translation keys + textures
    public static final Holder<MobEffect> STEPPING = of("stepping", new MobEffect(MobEffectCategory.BENEFICIAL, 0x3dcc95)
            .addAttributeModifier(Attributes.STEP_HEIGHT, Culinaire.id("effect.stepping"), 0.3F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );
    public static final Holder<MobEffect> AGILITY = of("agility", new MobEffect(MobEffectCategory.BENEFICIAL, 4866583) //TODO color
            .addAttributeModifier(Attributes.ATTACK_SPEED, Culinaire.id("effect.agility"), 0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );
    public static final Holder<MobEffect> KNOCKING = of("knocking", new MobEffect(MobEffectCategory.BENEFICIAL, 4866583) //TODO color
            .addAttributeModifier(Attributes.ATTACK_KNOCKBACK, Culinaire.id("effect.knocking"), 0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );
    public static final Holder<MobEffect> SPACIAL_HICCUP = of("spatial_hiccup", new TeleportRandomlyStatusEffect(MobEffectCategory.HARMFUL, 4866583)); //TODO color

    private static Holder<MobEffect> of(String id, MobEffect statusEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Culinaire.id(id), statusEffect);
    }
}
