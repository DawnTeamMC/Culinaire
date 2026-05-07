package fr.hugman.culinaire.item.sandwich;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.item.sandwich.value.SandwichIngredientValueModifier;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;

//TODO: add effects
public record SandwichIngredient(
        SandwichIngredientValueModifier nutritionModifier,
        SandwichIngredientValueModifier saturationModifier
) {
    public static final MapCodec<SandwichIngredient> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            SandwichIngredientValueModifier.CODEC.fieldOf("nutrition_modifier").forGetter(SandwichIngredient::nutritionModifier),
            SandwichIngredientValueModifier.CODEC.fieldOf("saturation_modifier").forGetter(SandwichIngredient::saturationModifier)
    ).apply(i, SandwichIngredient::new));

    public static final Codec<SandwichIngredient> CODEC = MAP_CODEC.codec();

    public static final StreamCodec<ByteBuf, SandwichIngredient> STREAM_CODEC = StreamCodec.composite(
            SandwichIngredientValueModifier.STREAM_CODEC, SandwichIngredient::nutritionModifier,
            SandwichIngredientValueModifier.STREAM_CODEC, SandwichIngredient::saturationModifier,
            SandwichIngredient::new
    );

    public static SandwichIngredient of(FoodProperties properties) {
        return new SandwichIngredient(
                new SandwichIngredientValueModifier(properties.nutrition(), AttributeModifier.Operation.ADD_VALUE),
                new SandwichIngredientValueModifier(properties.saturation(), AttributeModifier.Operation.ADD_VALUE)
        );
    }
}
