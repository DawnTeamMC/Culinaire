package fr.hugman.culinaire.recipe.sandwich.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.item.sandwich.SandwichIngredient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

public record SandwichCraftingIngredient(
        Ingredient ingredient,
        SandwichIngredient properties
) {
    public static final Codec<SandwichCraftingIngredient> CODEC = RecordCodecBuilder.create(i -> i.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(SandwichCraftingIngredient::ingredient),
            SandwichIngredient.MAP_CODEC.forGetter(SandwichCraftingIngredient::properties)
    ).apply(i, SandwichCraftingIngredient::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SandwichCraftingIngredient> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredient,
            SandwichIngredient.STREAM_CODEC, r -> r.properties,
            SandwichCraftingIngredient::new
    );
}
