package fr.hugman.culinaire.tea;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import java.util.Map;
import java.util.Optional;

public record TeaType(
        Component description,
        Map<Holder<Item>, Component> nameOverrides,
        Integer color,
        Integer brewTime,
        MobEffectInstance effect
) {
    public static final Codec<TeaType> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(
            instance -> instance.group(
                    ComponentSerialization.CODEC.fieldOf("description").forGetter(TeaType::description),
                    Codec.unboundedMap(Item.CODEC, ComponentSerialization.CODEC).optionalFieldOf("name_overrides", Map.of()).forGetter(TeaType::nameOverrides),
                    Codec.INT.fieldOf("color").forGetter(TeaType::color),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("brew_time").forGetter(TeaType::brewTime),
                    MobEffectInstance.CODEC.fieldOf("effect").forGetter(TeaType::effect)
            ).apply(instance, TeaType::new)
    ));

    public static final StreamCodec<RegistryFriendlyByteBuf, TeaType> PACKET_CODEC = StreamCodec.composite(
            ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC, TeaType::description,
            ByteBufCodecs.map(Object2ObjectOpenHashMap::new, ByteBufCodecs.holderRegistry(Registries.ITEM), ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC), TeaType::nameOverrides,
            ByteBufCodecs.INT, TeaType::color,
            ByteBufCodecs.INT, TeaType::brewTime,
            MobEffectInstance.STREAM_CODEC, TeaType::effect,
            TeaType::new
    );
    public static final Codec<Holder<TeaType>> ENTRY_CODEC = RegistryFileCodec.create(CulinaireRegistryKeys.TEA_TYPE, CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<TeaType>> ENTRY_PACKET_CODEC = ByteBufCodecs.holder(
            CulinaireRegistryKeys.TEA_TYPE, PACKET_CODEC
    );

    public static final Codec<HolderSet<TeaType>> ENTRY_LIST_CODEC = RegistryCodecs.homogeneousList(CulinaireRegistryKeys.TEA_TYPE, CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, HolderSet<TeaType>> ENTRY_LIST_PACKET_CODEC = ByteBufCodecs.holderSet(CulinaireRegistryKeys.TEA_TYPE);

    public Component getName() {
        MutableComponent mutableText = description.copy();
        ComponentUtils.mergeStyles(mutableText, Style.EMPTY.withColor(ChatFormatting.GRAY));
        return mutableText;
    }

    public Optional<Component> getItemNameOverride(Holder<Item> item) {
        return Optional.ofNullable(this.nameOverrides.get(item).copy());
    }
}
