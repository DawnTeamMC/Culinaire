package fr.hugman.culinaire.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import fr.hugman.culinaire.tag.CulinaireTeaTypeTags;
import fr.hugman.culinaire.tea.TeaType;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class TeaTypesComponent implements TooltipAppender {
    public static final TeaTypesComponent DEFAULT = new TeaTypesComponent(new Object2IntOpenHashMap<>(), true);
    private static final Codec<Integer> TEA_TYPE_LEVEL_CODEC = Codec.intRange(1, 255);
    private static final Codec<Object2IntOpenHashMap<RegistryEntry<TeaType>>> INLINE_CODEC = Codec.unboundedMap(
                    TeaType.ENTRY_CODEC, TEA_TYPE_LEVEL_CODEC
            )
            .xmap(Object2IntOpenHashMap::new, Function.identity());
    private static final Codec<TeaTypesComponent> BASE_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            INLINE_CODEC.fieldOf("levels").forGetter(component -> component.teaTypes),
                            Codec.BOOL.optionalFieldOf("show_in_tooltip", true).forGetter(component -> component.showInTooltip)
                    )
                    .apply(instance, TeaTypesComponent::new)
    );
    public static final Codec<TeaTypesComponent> CODEC = Codec.withAlternative(BASE_CODEC, INLINE_CODEC, map -> new TeaTypesComponent(map, true));
    public static final PacketCodec<RegistryByteBuf, TeaTypesComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.map(Object2IntOpenHashMap::new, TeaType.ENTRY_PACKET_CODEC, PacketCodecs.VAR_INT), component -> component.teaTypes,
            PacketCodecs.BOOLEAN, component -> component.showInTooltip,
            TeaTypesComponent::new
    );
    final Object2IntOpenHashMap<RegistryEntry<TeaType>> teaTypes;
    final boolean showInTooltip;

    public TeaTypesComponent(Object2IntOpenHashMap<RegistryEntry<TeaType>> teaTypes, boolean showInTooltip) {
        this.teaTypes = teaTypes;
        this.showInTooltip = showInTooltip;

        for (Entry<RegistryEntry<TeaType>> entry : teaTypes.object2IntEntrySet()) {
            int i = entry.getIntValue();
            if (i < 0 || i > 255) {
                throw new IllegalArgumentException("Tea type " + entry.getKey() + " has invalid level " + i);
            }
        }
    }

    public int getLevel(RegistryEntry<TeaType> teaType) {
        return this.teaTypes.getInt(teaType);
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        if (this.showInTooltip) {
            RegistryWrapper.WrapperLookup wrapperLookup = context.getRegistryLookup();
            RegistryEntryList<TeaType> registryEntryList = getTooltipOrderList(wrapperLookup, CulinaireRegistryKeys.TEA_TYPE, CulinaireTeaTypeTags.TOOLTIP_ORDER);

            for (RegistryEntry<TeaType> registryEntry : registryEntryList) {
                int i = this.teaTypes.getInt(registryEntry);
                if (i > 0) {
                    //TODO
                    //tooltip.accept(Enchantment.getName(registryEntry, i));
                }
            }

            for (Entry<RegistryEntry<TeaType>> entry : this.teaTypes.object2IntEntrySet()) {
                RegistryEntry<TeaType> registryEntry2 = entry.getKey();
                if (!registryEntryList.contains(registryEntry2)) {
                    //TODO
                    //tooltip.accept(Enchantment.getName((RegistryEntry<TeaType>)entry.getKey(), entry.getIntValue()));
                }
            }
        }
    }

    private static <T> RegistryEntryList<T> getTooltipOrderList(
            @Nullable RegistryWrapper.WrapperLookup registries, RegistryKey<Registry<T>> registryRef, TagKey<T> tooltipOrderTag
    ) {
        if (registries != null) {
            Optional<RegistryEntryList.Named<T>> optional = registries.getOrThrow(registryRef).getOptional(tooltipOrderTag);
            if (optional.isPresent()) {
                return optional.get();
            }
        }

        return RegistryEntryList.of();
    }

    public TeaTypesComponent withShowInTooltip(boolean showInTooltip) {
        return new TeaTypesComponent(this.teaTypes, showInTooltip);
    }

    public Set<RegistryEntry<TeaType>> getTeaTypes() {
        return Collections.unmodifiableSet(this.teaTypes.keySet());
    }

    public Set<Entry<RegistryEntry<TeaType>>> getTeaTypeEntries() {
        return Collections.unmodifiableSet(this.teaTypes.object2IntEntrySet());
    }

    public int getSize() {
        return this.teaTypes.size();
    }

    public boolean isEmpty() {
        return this.teaTypes.isEmpty();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return o instanceof TeaTypesComponent teaTypesComponent
                    && this.showInTooltip == teaTypesComponent.showInTooltip
                    && this.teaTypes.equals(teaTypesComponent.teaTypes);
        }
    }

    public int hashCode() {
        int i = this.teaTypes.hashCode();
        return 31 * i + (this.showInTooltip ? 1 : 0);
    }

    public String toString() {
        return "TeaTypes{teaTypes=" + this.teaTypes + ", showInTooltip=" + this.showInTooltip + "}";
    }

    public static class Builder {
        private final Object2IntOpenHashMap<RegistryEntry<TeaType>> teaTypes = new Object2IntOpenHashMap<>();
        private final boolean showInTooltip;

        public Builder(TeaTypesComponent teaTypesComponent) {
            this.teaTypes.putAll(teaTypesComponent.teaTypes);
            this.showInTooltip = teaTypesComponent.showInTooltip;
        }

        public void set(RegistryEntry<TeaType> teaType, int level) {
            if (level <= 0) {
                this.teaTypes.removeInt(teaType);
            } else {
                this.teaTypes.put(teaType, Math.min(level, 255));
            }
        }

        public void add(RegistryEntry<TeaType> teaType, int level) {
            if (level > 0) {
                this.teaTypes.merge(teaType, Math.min(level, 255), Integer::max);
            }
        }

        public void remove(Predicate<RegistryEntry<TeaType>> predicate) {
            this.teaTypes.keySet().removeIf(predicate);
        }

        public int getLevel(RegistryEntry<TeaType> teaType) {
            return this.teaTypes.getOrDefault(teaType, 0);
        }

        public Set<RegistryEntry<TeaType>> getTeaTypes() {
            return this.teaTypes.keySet();
        }

        public TeaTypesComponent build() {
            return new TeaTypesComponent(this.teaTypes, this.showInTooltip);
        }
    }
}
