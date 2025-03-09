package fr.hugman.culinaire.tea;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.Culinaire;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

import java.util.List;
import java.util.function.IntFunction;

public class TeaType {
    public static final Codec<TeaType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Strength.CODEC.fieldOf("strength").forGetter(TeaType::getStrength),
            Flavor.CODEC.fieldOf("flavor").forGetter(TeaType::getFlavor)
    ).apply(instance, TeaType::new));
    public static final PacketCodec<ByteBuf, TeaType> PACKET_CODEC = PacketCodec.tuple(
            Strength.PACKET_CODEC, TeaType::getStrength,
            Flavor.PACKET_CODEC, TeaType::getFlavor,
            TeaType::new
    );

    public static final Codec<List<TeaType>> LIST_CODEC = Codec.list(CODEC);
    public static final PacketCodec<ByteBuf, List<TeaType>> LIST_PACKET_CODEC = PACKET_CODEC.collect(PacketCodecs.toList());

    private final Strength strength;
    private final Flavor flavor;

    public TeaType(String strength, String flavor) {
        this(Strength.byName(strength), Flavor.byName(flavor));
    }

    public TeaType(Strength strength, Flavor flavor) {
        this.strength = strength;
        this.flavor = flavor;
    }

    public boolean isCorrect() {
        return this.getStrength() != null && this.getFlavor() != null;
    }

    public Strength getStrength() {
        return strength;
    }

    public Flavor getFlavor() {
        return flavor;
    }

    public TagKey<Item> getTagKey() {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "tea_ingredients/" + getFlavor().asString() + "/" + getStrength().asString()));
    }

    public Text getName() {
        return Text.translatable("tea_type." + Culinaire.MOD_ID + "." + getFlavor().asString() + "." + getStrength().asString());
    }

    public int getBrewTime() {
        return flavor.getBrewTime() * strength.getPotency();
    }

    @Override
    public String toString() {
        return "TeaType{" + "strength=" + strength + ", flavor=" + flavor + '}';
    }

    public enum Flavor implements StringIdentifiable {
        SWEET("sweet", 9523743, StatusEffects.SATURATION, true),
        UMAMI("umami", 10059295, StatusEffects.RESISTANCE, true),
        SALTY("salty", 10251038, StatusEffects.SPEED, true),
        SOUR("sour", 7238946, StatusEffects.POISON, false),
        BITTER("bitter", 5057061, StatusEffects.BLINDNESS, false),
        SHINING("shining", 16759902, StatusEffects.GLOWING, true),
        GLOOPY("gloopy", 9332621, (user, stack, world, teaType) -> Items.CHORUS_FRUIT.finishUsing(stack, world, user));

        private final String name;

        private final int color;
        private final int brewTime;
        private final TeaEffect effect;

        public static final Codec<Flavor> CODEC = StringIdentifiable.createBasicCodec(Flavor::values);

        private static final IntFunction<Flavor> BY_ID = ValueLists.createIdToValueFunction(Flavor::ordinal, values(), ValueLists.OutOfBoundsHandling.ZERO); // ew
        public static final PacketCodec<ByteBuf, Flavor> PACKET_CODEC = PacketCodecs.indexed(BY_ID, Flavor::ordinal);


        Flavor(String name, int color, TeaEffect effect) {
            this(name, color, effect, 200);
        }

        Flavor(String name, int color, TeaEffect effect, int brewTime) {
            this.name = name;
            this.brewTime = brewTime;
            this.color = color;
            this.effect = effect;
        }

        Flavor(String name, int color, RegistryEntry<StatusEffect> effect, boolean add) {
            this(name, color, (user, stack, world, teaType) -> {
                var v = effect.value();
                if (effect.value() != null && world instanceof ServerWorld serverWorld) {
                    if (add) {
                        if (v.isInstant()) {
                            v.applyInstantEffect(serverWorld, user, user, user, teaType.getStrength().getPotency(), 1.0D);
                        } else {
                            user.addStatusEffect(new StatusEffectInstance(effect, teaType.getStrength().getPotency() * 400));
                        }
                    } else {
                        user.removeStatusEffect(effect);
                    }
                }
            }, 200);
        }

        public static Flavor byName(String name) {
            for (Flavor flavor : Flavor.values()) {
                if (flavor.getName().equals(name)) {
                    return flavor;
                }
            }
            return null;
        }

        /**
         * @deprecated Use {@link #asString()} instead
         */
        public String getName() {
            return this.asString();
        }

        public TeaEffect getEffect() {
            return effect;
        }

        public int getColor() {
            return color;
        }

        public int getBrewTime() {
            return brewTime;
        }

        @Override
        public String asString() {
            return name;
        }
    }

    public enum Strength implements StringIdentifiable {
        WEAK("weak", 1),
        NORMAL("normal", 2),
        STRONG("strong", 3);

        public static final Codec<Strength> CODEC = StringIdentifiable.createBasicCodec(Strength::values);

        private static final IntFunction<Strength> BY_ID = ValueLists.createIdToValueFunction(Strength::ordinal, values(), ValueLists.OutOfBoundsHandling.ZERO); // ew
        public static final PacketCodec<ByteBuf, Strength> PACKET_CODEC = PacketCodecs.indexed(BY_ID, Strength::ordinal);

        private final String name;
        private final int potency;

        Strength(String name, int potency) {
            this.name = name;
            this.potency = potency;
        }

        public static Strength byName(String name) {
            for (Strength strength : Strength.values()) {
                if (strength.getName().equals(name)) {
                    return strength;
                }
            }
            return null;
        }

        public static Strength byPotency(int potency) {
            for (Strength strength : Strength.values()) {
                if (strength.getPotency() == potency) {
                    return strength;
                }
            }
            return null;
        }

        /**
         * @deprecated Use {@link #asString()} instead
         */
        @Deprecated
        public String getName() {
            return this.asString();
        }

        public int getPotency() {
            return potency;
        }

        @Override
        public String asString() {
            return name;
        }
    }
}
