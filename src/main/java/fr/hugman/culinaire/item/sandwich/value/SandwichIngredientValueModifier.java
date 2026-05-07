package fr.hugman.culinaire.item.sandwich.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record SandwichIngredientValueModifier(float amount, AttributeModifier.Operation operation) {
    public static final MapCodec<SandwichIngredientValueModifier> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.FLOAT.fieldOf("amount").forGetter(SandwichIngredientValueModifier::amount),
            AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(SandwichIngredientValueModifier::operation)
    ).apply(i, SandwichIngredientValueModifier::new));
    public static final Codec<SandwichIngredientValueModifier> CODEC = MAP_CODEC.codec();

    public static final StreamCodec<ByteBuf, SandwichIngredientValueModifier> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SandwichIngredientValueModifier::amount,
            AttributeModifier.Operation.STREAM_CODEC, SandwichIngredientValueModifier::operation,
            SandwichIngredientValueModifier::new
    );
}
