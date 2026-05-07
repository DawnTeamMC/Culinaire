package fr.hugman.culinaire.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;

public record BurnableComponent(
        int burningTime,
        Holder<Item> burnsInto
) {
    public static final Codec<BurnableComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("burning_time").forGetter(BurnableComponent::burningTime),
                            Item.CODEC.fieldOf("burns_into").forGetter(BurnableComponent::burnsInto)
                    )
                    .apply(instance, BurnableComponent::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, BurnableComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, BurnableComponent::burningTime,
            ByteBufCodecs.holderRegistry(Registries.ITEM), BurnableComponent::burnsInto,
            BurnableComponent::new
    );

}
