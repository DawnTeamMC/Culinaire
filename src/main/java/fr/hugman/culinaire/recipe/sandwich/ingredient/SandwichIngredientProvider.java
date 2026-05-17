package fr.hugman.culinaire.recipe.sandwich.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.item.sandwich.SandwichIngredient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

public record SandwichIngredientProvider(
        Ingredient ingredient,
        SandwichIngredient properties
) {
    public static final Codec<SandwichIngredientProvider> CODEC = RecordCodecBuilder.create(i -> i.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(SandwichIngredientProvider::ingredient),
            SandwichIngredient.MAP_CODEC.forGetter(SandwichIngredientProvider::properties)
    ).apply(i, SandwichIngredientProvider::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SandwichIngredientProvider> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredient,
            SandwichIngredient.STREAM_CODEC, r -> r.properties,
            SandwichIngredientProvider::new
    );
}
