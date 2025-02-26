package fr.hugman.culinaire.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.Item;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKey;

public record BurnableComponent(
        int maxTime,
        RegistryKey<Item> burnsInto
) {
    private static final Codec<BurnableComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("max_time").forGetter(DyedColorComponent::rgb),
                            Codec.BOOL.optionalFieldOf("burns_into", Boolean.valueOf(true)).forGetter(DyedColorComponent::showInTooltip)
                    )
                    .apply(instance, DyedColorComponent::new)
    );
    public static final PacketCodec<ByteBuf, BurnableComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, DyedColorComponent::rgb,
            PacketCodecs.BOOLEAN, DyedColorComponent::showInTooltip,
            BurnableComponent::new
    );

}
