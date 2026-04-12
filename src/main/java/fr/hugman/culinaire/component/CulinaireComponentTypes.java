package fr.hugman.culinaire.component;

import fr.hugman.culinaire.Culinaire;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;

public final class CulinaireComponentTypes {
    public static final DataComponentType<Integer> BURN = register("burn", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<BurnableComponent> BURNABLE = register("burnable", builder -> builder.persistent(BurnableComponent.CODEC).networkSynchronized(BurnableComponent.PACKET_CODEC));
    public static final DataComponentType<SandwichContentsComponent> SANDWICH_CONTENTS = register("sandwich_contents", builder -> builder.persistent(SandwichContentsComponent.CODEC).networkSynchronized(SandwichContentsComponent.PACKET_CODEC));
    public static final DataComponentType<TeaTypesComponent> TEA_TYPES = register("tea_types", builder -> builder.persistent(TeaTypesComponent.CODEC).networkSynchronized(TeaTypesComponent.PACKET_CODEC));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Culinaire.id(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}