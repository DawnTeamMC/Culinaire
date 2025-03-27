package fr.hugman.culinaire.component;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import fr.hugman.culinaire.tag.CulinaireTeaTypeTags;
import fr.hugman.culinaire.tea.TeaType;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.Consumable;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public final class TeaTypesComponent implements TooltipAppender, Consumable {
    public static final TeaTypesComponent DEFAULT = new TeaTypesComponent(new Object2IntOpenHashMap<>(), true);

    private static final Text NONE_TEXT = Text.translatable("effect.none").formatted(Formatting.GRAY);
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

    public Optional<Text> getName(RegistryEntry<Item> item) {
        return getAbundantType().flatMap(entry -> entry.value().getItemNameOverride(item));
    }

    public Optional<RegistryEntry<TeaType>> getAbundantType() {
        return hasAbundantType() ? Optional.of(this.teaTypes.object2IntEntrySet().iterator().next().getKey()) : Optional.empty();
    }

    public boolean hasAbundantType() {
        return this.teaTypes.object2IntEntrySet().size() == 1;
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

    public int getBrewTime() {
        return teaTypes.object2IntEntrySet().stream().mapToInt(teaType -> teaType.getKey().value().brewTime()).sum();
    }

    public int getColor(int defaultColor) {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;

        for (var teaType : getTeaTypeEntries()) {
            int m = teaType.getKey().value().color();
            int n = teaType.getIntValue() + 1;
            i += n * ColorHelper.getRed(m);
            j += n * ColorHelper.getGreen(m);
            k += n * ColorHelper.getBlue(m);
            l += n;
        }

        return l == 0 ? defaultColor : ColorHelper.getArgb(i / l, j / l, k / l);
    }

    public Set<Entry<RegistryEntry<TeaType>>> getTeaTypeEntries() {
        return Collections.unmodifiableSet(this.teaTypes.object2IntEntrySet());
    }

    public Iterable<StatusEffectInstance> getEffects() {
        return teaTypes.object2IntEntrySet().stream()
                .map(entry -> {
                    var effect = entry.getKey().value().effect();
                    return new StatusEffectInstance(
                            effect.getEffectType(),
                            effect.mapDuration(i -> i * entry.getIntValue()),
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.shouldShowParticles()
                    );
                })::iterator;
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        if (this.showInTooltip) {
            if(!hasAbundantType()) {
                RegistryWrapper.WrapperLookup wrapperLookup = context.getRegistryLookup();
                RegistryEntryList<TeaType> teaTypeEntries = getTooltipOrderList(wrapperLookup, CulinaireRegistryKeys.TEA_TYPE, CulinaireTeaTypeTags.TOOLTIP_ORDER);

                for (RegistryEntry<TeaType> registryEntry : teaTypeEntries) {
                    int i = this.teaTypes.getInt(registryEntry);
                    if (i > 0) {
                        tooltip.accept(registryEntry.value().getName());
                    }
                }

                for (Entry<RegistryEntry<TeaType>> entry : this.teaTypes.object2IntEntrySet()) {
                    RegistryEntry<TeaType> registryEntry2 = entry.getKey();
                    if (!teaTypeEntries.contains(registryEntry2)) {
                        tooltip.accept(entry.getKey().value().getName());
                    }
                }
            }

            var list = Lists.<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>>newArrayList();
            boolean bl = true;

            for (StatusEffectInstance statusEffectInstance : this.getEffects()) {
                bl = false;
                MutableText mutableText = Text.translatable(statusEffectInstance.getTranslationKey());
                RegistryEntry<StatusEffect> registryEntry = statusEffectInstance.getEffectType();
                registryEntry.value().forEachAttributeModifier(statusEffectInstance.getAmplifier(), (attribute, modifier) -> list.add(new Pair<>(attribute, modifier)));
                if (statusEffectInstance.getAmplifier() > 0) {
                    mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
                }

                if (!statusEffectInstance.isDurationBelow(20)) {
                    mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.getDurationText(statusEffectInstance, 1.0f, context.getUpdateTickRate()));
                }

                tooltip.accept(mutableText.formatted(registryEntry.value().getCategory().getFormatting()));
            }

            if (bl) {
                tooltip.accept(NONE_TEXT);
            }

            if (!list.isEmpty()) {
                tooltip.accept(ScreenTexts.EMPTY);
                tooltip.accept(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));

                for (Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier> pair : list) {
                    EntityAttributeModifier entityAttributeModifier = pair.getSecond();
                    double d = entityAttributeModifier.value();
                    double e;
                    if (entityAttributeModifier.operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            && entityAttributeModifier.operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                        e = entityAttributeModifier.value();
                    } else {
                        e = entityAttributeModifier.value() * 100.0;
                    }

                    if (d > 0.0) {
                        tooltip.accept(
                                Text.translatable(
                                                "attribute.modifier.plus." + entityAttributeModifier.operation().getId(),
                                                AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                                Text.translatable(pair.getFirst().value().getTranslationKey())
                                        )
                                        .formatted(Formatting.BLUE)
                        );
                    } else if (d < 0.0) {
                        e *= -1.0;
                        tooltip.accept(
                                Text.translatable(
                                                "attribute.modifier.take." + entityAttributeModifier.operation().getId(),
                                                AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                                Text.translatable(pair.getFirst().value().getTranslationKey())
                                        )
                                        .formatted(Formatting.RED)
                        );
                    }
                }
            }
        }
    }

    @Override
    public void onConsume(World world, LivingEntity user, ItemStack stack, ConsumableComponent consumable) {
        if (user.getWorld() instanceof ServerWorld serverWorld) {
            PlayerEntity playerEntity2 = user instanceof PlayerEntity playerEntity ? playerEntity : null;
            this.getEffects().forEach(effect -> {
                if (effect.getEffectType().value().isInstant()) {
                    effect.getEffectType().value().applyInstantEffect(serverWorld, playerEntity2, playerEntity2, user, effect.getAmplifier(), 1.0);
                } else {
                    user.addStatusEffect(effect);
                }
            });
        }
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
        return "TeaTypes{teaTypeEntries=" + this.teaTypes + ", showInTooltip=" + this.showInTooltip + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Object2IntOpenHashMap<RegistryEntry<TeaType>> teaTypes = new Object2IntOpenHashMap<>();

        public Builder() {
        }

        public Builder add(RegistryEntry<TeaType> teaType, int level) {
            if (level > 0) {
                this.teaTypes.merge(teaType, Math.min(level, 255), Integer::max);
            }
            return this;
        }

        public TeaTypesComponent build() {
            return new TeaTypesComponent(this.teaTypes, true);
        }
    }
}
