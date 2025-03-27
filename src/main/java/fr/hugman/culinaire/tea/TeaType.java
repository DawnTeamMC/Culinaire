package fr.hugman.culinaire.tea;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.dynamic.Codecs;

import java.util.Map;
import java.util.Optional;

public record TeaType(
        Text description,
        Map<RegistryEntry<Item>, Text> nameOverrides,
        Integer color,
        Integer brewTime,
        StatusEffectInstance effect
) {
    public static final Codec<TeaType> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(
            instance -> instance.group(
                    TextCodecs.CODEC.fieldOf("description").forGetter(TeaType::description),
                    Codec.unboundedMap(Item.ENTRY_CODEC, TextCodecs.CODEC).optionalFieldOf("name_overrides", Map.of()).forGetter(TeaType::nameOverrides),
                    Codec.INT.fieldOf("color").forGetter(TeaType::color),
                    Codecs.NON_NEGATIVE_INT.fieldOf("brew_time").forGetter(TeaType::brewTime),
                    StatusEffectInstance.CODEC.fieldOf("effect").forGetter(TeaType::effect)
            ).apply(instance, TeaType::new)
    ));

    public static final PacketCodec<RegistryByteBuf, TeaType> PACKET_CODEC = PacketCodec.tuple(
            TextCodecs.PACKET_CODEC, TeaType::description,
            PacketCodecs.map(Object2ObjectOpenHashMap::new, PacketCodecs.registryEntry(RegistryKeys.ITEM), TextCodecs.PACKET_CODEC), TeaType::nameOverrides,
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

    public static Text getName(RegistryEntry<TeaType> teaType, int level) {
        MutableText mutableText = teaType.value().description.copy();
        Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(Formatting.GRAY));
        if (level != 1) {
            mutableText.append(ScreenTexts.SPACE).append(Text.translatable("enchantment.level." + level));
        }
        return mutableText;
    }

    public Text getName(int level) {
        MutableText mutableText = description.copy();
        Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(Formatting.GRAY));
        if (level != 1) {
            mutableText.append(ScreenTexts.SPACE).append(Text.translatable("enchantment.level." + level));
        }
        return mutableText;
    }

    public Optional<Text> getItemNameOverride(RegistryEntry<Item> item) {
        return Optional.ofNullable(this.nameOverrides.get(item).copy());
    }
}
