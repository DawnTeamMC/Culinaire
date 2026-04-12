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
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ConsumableListener;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;

public final class TeaTypesComponent implements TooltipProvider, ConsumableListener {
    public static final TeaTypesComponent DEFAULT = new TeaTypesComponent(new Object2IntOpenHashMap<>(), true);

    private static final Component NONE_TEXT = Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);
    private static final Codec<Integer> TEA_TYPE_LEVEL_CODEC = Codec.intRange(1, 255);
    private static final Codec<Object2IntOpenHashMap<Holder<TeaType>>> INLINE_CODEC = Codec.unboundedMap(
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
    public static final StreamCodec<RegistryFriendlyByteBuf, TeaTypesComponent> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(Object2IntOpenHashMap::new, TeaType.ENTRY_PACKET_CODEC, ByteBufCodecs.VAR_INT), component -> component.teaTypes,
            ByteBufCodecs.BOOL, component -> component.showInTooltip,
            TeaTypesComponent::new
    );
    final Object2IntOpenHashMap<Holder<TeaType>> teaTypes;
    final boolean showInTooltip;

    public TeaTypesComponent(Object2IntOpenHashMap<Holder<TeaType>> teaTypes, boolean showInTooltip) {
        this.teaTypes = teaTypes;
        this.showInTooltip = showInTooltip;

        for (Entry<Holder<TeaType>> entry : teaTypes.object2IntEntrySet()) {
            int i = entry.getIntValue();
            if (i < 0 || i > 255) {
                throw new IllegalArgumentException("Tea type " + entry.getKey() + " has invalid level " + i);
            }
        }
    }

    public Optional<Component> getName(Holder<Item> item) {
        return getAbundantType().flatMap(entry -> entry.value().getItemNameOverride(item));
    }

    public Optional<Holder<TeaType>> getAbundantType() {
        return hasAbundantType() ? Optional.of(this.teaTypes.object2IntEntrySet().iterator().next().getKey()) : Optional.empty();
    }

    public boolean hasAbundantType() {
        return this.teaTypes.object2IntEntrySet().size() == 1;
    }

    private static <T> HolderSet<T> getTooltipOrderList(
            @Nullable HolderLookup.Provider registries, ResourceKey<Registry<T>> registryRef, TagKey<T> tooltipOrderTag
    ) {
        if (registries != null) {
            Optional<HolderSet.Named<T>> optional = registries.lookupOrThrow(registryRef).get(tooltipOrderTag);
            if (optional.isPresent()) {
                return optional.get();
            }
        }

        return HolderSet.direct();
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
            i += n * ARGB.red(m);
            j += n * ARGB.green(m);
            k += n * ARGB.blue(m);
            l += n;
        }

        return l == 0 ? defaultColor : ARGB.color(i / l, j / l, k / l);
    }

    public Set<Entry<Holder<TeaType>>> getTeaTypeEntries() {
        return Collections.unmodifiableSet(this.teaTypes.object2IntEntrySet());
    }

    public Iterable<MobEffectInstance> getEffects() {
        return teaTypes.object2IntEntrySet().stream()
                .map(entry -> {
                    var effect = entry.getKey().value().effect();
                    return new MobEffectInstance(
                            effect.getEffect(),
                            effect.mapDuration(i -> i * entry.getIntValue()),
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible()
                    );
                })::iterator;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type, DataComponentGetter components) {
        if (this.showInTooltip) {
            if(!hasAbundantType()) {
                HolderLookup.Provider wrapperLookup = context.registries();
                HolderSet<TeaType> teaTypeEntries = getTooltipOrderList(wrapperLookup, CulinaireRegistryKeys.TEA_TYPE, CulinaireTeaTypeTags.TOOLTIP_ORDER);

                for (Holder<TeaType> registryEntry : teaTypeEntries) {
                    int i = this.teaTypes.getInt(registryEntry);
                    if (i > 0) {
                        tooltip.accept(registryEntry.value().getName());
                    }
                }

                for (Entry<Holder<TeaType>> entry : this.teaTypes.object2IntEntrySet()) {
                    Holder<TeaType> registryEntry2 = entry.getKey();
                    if (!teaTypeEntries.contains(registryEntry2)) {
                        tooltip.accept(entry.getKey().value().getName());
                    }
                }
            }

            var list = Lists.<Pair<Holder<Attribute>, AttributeModifier>>newArrayList();
            boolean bl = true;

            for (MobEffectInstance statusEffectInstance : this.getEffects()) {
                bl = false;
                MutableComponent mutableText = Component.translatable(statusEffectInstance.getDescriptionId());
                Holder<MobEffect> registryEntry = statusEffectInstance.getEffect();
                registryEntry.value().createModifiers(statusEffectInstance.getAmplifier(), (attribute, modifier) -> list.add(new Pair<>(attribute, modifier)));
                if (statusEffectInstance.getAmplifier() > 0) {
                    mutableText = Component.translatable("potion.withAmplifier", mutableText, Component.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
                }

                if (!statusEffectInstance.endsWithin(20)) {
                    mutableText = Component.translatable("potion.withDuration", mutableText, MobEffectUtil.formatDuration(statusEffectInstance, 1.0f, context.tickRate()));
                }

                tooltip.accept(mutableText.withStyle(registryEntry.value().getCategory().getTooltipFormatting()));
            }

            if (bl) {
                tooltip.accept(NONE_TEXT);
            }

            if (!list.isEmpty()) {
                tooltip.accept(CommonComponents.EMPTY);
                tooltip.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));

                for (Pair<Holder<Attribute>, AttributeModifier> pair : list) {
                    AttributeModifier entityAttributeModifier = pair.getSecond();
                    double d = entityAttributeModifier.amount();
                    double e;
                    if (entityAttributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            && entityAttributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                        e = entityAttributeModifier.amount();
                    } else {
                        e = entityAttributeModifier.amount() * 100.0;
                    }

                    if (d > 0.0) {
                        tooltip.accept(
                                Component.translatable(
                                                "attribute.modifier.plus." + entityAttributeModifier.operation().id(),
                                                ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e),
                                                Component.translatable(pair.getFirst().value().getDescriptionId())
                                        )
                                        .withStyle(ChatFormatting.BLUE)
                        );
                    } else if (d < 0.0) {
                        e *= -1.0;
                        tooltip.accept(
                                Component.translatable(
                                                "attribute.modifier.take." + entityAttributeModifier.operation().id(),
                                                ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e),
                                                Component.translatable(pair.getFirst().value().getDescriptionId())
                                        )
                                        .withStyle(ChatFormatting.RED)
                        );
                    }
                }
            }
        }
    }

    @Override
    public void onConsume(Level world, LivingEntity user, ItemStack stack, Consumable consumable) {
        if (user.level() instanceof ServerLevel serverWorld) {
            Player playerEntity2 = user instanceof Player playerEntity ? playerEntity : null;
            this.getEffects().forEach(effect -> {
                if (effect.getEffect().value().isInstantenous()) {
                    effect.getEffect().value().applyInstantenousEffect(serverWorld, playerEntity2, playerEntity2, user, effect.getAmplifier(), 1.0);
                } else {
                    user.addEffect(effect);
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
        private final Object2IntOpenHashMap<Holder<TeaType>> teaTypes = new Object2IntOpenHashMap<>();

        public Builder() {
        }

        public Builder add(Holder<TeaType> teaType, int level) {
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
