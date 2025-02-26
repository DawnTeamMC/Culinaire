package fr.hugman.culinaire.component;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public class CulinaireComponentTypes {
    public static final ComponentType<Integer> BURN = register("burn", builder -> builder.codec(Codecs.NON_NEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<BurnableComponent> BURNABLE = register("burnable", builder -> builder.codec(BurnableComponent.CODEC).packetCodec(BurnableComponent.PACKET_CODEC));

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Culinaire.id(id), builderOperator.apply(ComponentType.builder()).build());
    }

}
