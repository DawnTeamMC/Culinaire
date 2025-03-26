package fr.hugman.culinaire.data.provider;

import fr.hugman.culinaire.entity.effect.CulinaireEffects;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import fr.hugman.culinaire.tea.TeaType;
import fr.hugman.culinaire.tea.TeaTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

//TODO: a generic class for other devs
public class CulinaireTeaTypeProvider extends FabricDynamicRegistryProvider {
    public CulinaireTeaTypeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.addAll(registries.getOrThrow(CulinaireRegistryKeys.TEA_TYPE));
    }

    @Override
    public String getName() {
        return "Tea Types";
    }

    public static void register(Registerable<TeaType> registerable) {
        of(registerable, TeaTypes.GREEN, 0x60bf79, new StatusEffectInstance(StatusEffects.ABSORPTION, 20 * 30, 2));
        of(registerable, TeaTypes.WHITE, 0xfcf5d9, new StatusEffectInstance(CulinaireEffects.HIGH_STEPS, 20 * 30, 3));
        of(registerable, TeaTypes.BLACK, 0x45190c, new StatusEffectInstance(CulinaireEffects.HIGH_STEPS, 20 * 30, 3)); //TODO
        of(registerable, TeaTypes.OOLONG, 0xb88e1c, new StatusEffectInstance(CulinaireEffects.HIGH_STEPS, 20 * 30, 3)); //TODO
        of(registerable, TeaTypes.PU_ERH, 0xa6500f, new StatusEffectInstance(CulinaireEffects.HIGH_STEPS, 20 * 30, 3)); //TODO
        of(registerable, TeaTypes.ENDER, 0x71369e, new StatusEffectInstance(CulinaireEffects.HIGH_STEPS, 20 * 30, 3)); //TODO
    }

    private static void of(Registerable<TeaType> registry, RegistryKey<TeaType> key, int color, StatusEffectInstance effectInstance) {
        registry.register(key, new TeaType(color, 200, effectInstance));
    }
}