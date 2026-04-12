package fr.hugman.culinaire.data.provider;

import fr.hugman.culinaire.entity.effect.CulinaireEffects;
import fr.hugman.culinaire.item.CulinaireItems;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import fr.hugman.culinaire.tea.TeaType;
import fr.hugman.culinaire.tea.TeaTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//TODO: a generic class for other devs
public class CulinaireTeaTypeProvider extends FabricDynamicRegistryProvider {
    public CulinaireTeaTypeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(CulinaireRegistryKeys.TEA_TYPE));
    }

    @Override
    public String getName() {
        return "Tea Types";
    }

    public static void register(BootstrapContext<TeaType> registerable) {
        of(registerable, TeaTypes.GREEN, 0x60bf79, new MobEffectInstance(MobEffects.LUCK, 20 * 15, 2));
        of(registerable, TeaTypes.WHITE, 0xfcf5d9, new MobEffectInstance(CulinaireEffects.STEPPING, 20 * 30, 2));
        of(registerable, TeaTypes.BLACK, 0x45190c, new MobEffectInstance(MobEffects.ABSORPTION, 20 * 15, 2));
        of(registerable, TeaTypes.OOLONG, 0xb88e1c, new MobEffectInstance(CulinaireEffects.AGILITY, 20 * 8, 2));
        of(registerable, TeaTypes.PU_ER, 0xa6500f, new MobEffectInstance(CulinaireEffects.KNOCKING, 20 * 8, 1));
        of(registerable, TeaTypes.ENDER, 0x71369e, new MobEffectInstance(CulinaireEffects.SPACIAL_HICCUP, 20 * 20, 2));
    }

    private static void of(BootstrapContext<TeaType> registry, ResourceKey<TeaType> key, int color, MobEffectInstance effectInstance) {
        var id = key.identifier();
        var teaBagEntry = CulinaireItems.TEA_BAG.builtInRegistryHolder();
        var teaBottleEntry = CulinaireItems.TEA_BOTTLE.builtInRegistryHolder();
        Map<Holder<Item>, Component> translations = Map.of(
                teaBagEntry, Component.translatable(teaBagEntry.value().getDescriptionId() + "." + id.getNamespace() + "." + id.getPath()),
                teaBottleEntry, Component.translatable(teaBottleEntry.value().getDescriptionId() + "." + id.getNamespace() + "." + id.getPath())
        );
        registry.register(key, new TeaType(
                Component.translatable("tea_type." + id.getNamespace() + "." + id.getPath()),
                translations,
                color,
                200,
                effectInstance
        ));
    }
}