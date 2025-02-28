package fr.hugman.culinaire.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

public record BurnableComponent(
        int burningTime,
        RegistryEntry<Item> burnsInto
) {
    public static final Codec<BurnableComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("burning_time").forGetter(BurnableComponent::burningTime),
                            Item.ENTRY_CODEC.fieldOf("burns_into").forGetter(BurnableComponent::burnsInto)
                    )
                    .apply(instance, BurnableComponent::new)
    );
    public static final PacketCodec<RegistryByteBuf, BurnableComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, BurnableComponent::burningTime,
            PacketCodecs.registryEntry(RegistryKeys.ITEM), BurnableComponent::burnsInto,
            BurnableComponent::new
    );

}
