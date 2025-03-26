package fr.hugman.culinaire.tea;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.dynamic.Codecs;

public record TeaType(
        Integer color,
        Integer brewTime,
        StatusEffectInstance effect
) {
    public static final Codec<TeaType> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("color").forGetter(tea -> tea.color),
                    Codecs.NON_NEGATIVE_INT.fieldOf("brew_time").forGetter(tea -> tea.brewTime),
                    StatusEffectInstance.CODEC.fieldOf("effect").forGetter(tea -> tea.effect)
            ).apply(instance, TeaType::new)
    ));

    public static final PacketCodec<RegistryByteBuf, TeaType> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, TeaType::color,
            PacketCodecs.INTEGER, TeaType::brewTime,
            StatusEffectInstance.PACKET_CODEC, TeaType::effect,
            TeaType::new
    );
    public static final Codec<RegistryEntry<TeaType>> ENTRY_CODEC = RegistryElementCodec.of(CulinaireRegistryKeys.TEA_TYPE, CODEC);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<TeaType>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(
            CulinaireRegistryKeys.TEA_TYPE, PACKET_CODEC
    );

    public static final Codec<RegistryEntryList<TeaType>> ENTRY_LIST_CODEC = RegistryCodecs.entryList(CulinaireRegistryKeys.TEA_TYPE, CODEC);
    public static final PacketCodec<RegistryByteBuf, RegistryEntryList<TeaType>> ENTRY_LIST_PACKET_CODEC = PacketCodecs.registryEntryList(CulinaireRegistryKeys.TEA_TYPE);

}
