package fr.hugman.culinaire.recipe.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

public record SandwichRecipeDisplay(
		SlotDisplay bread,
		List<SlotDisplay> ingredients,
		SlotDisplay result,
		SlotDisplay craftingStation
) implements RecipeDisplay {
	public static final MapCodec<SandwichRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(
		i -> i.group(
				SlotDisplay.CODEC.fieldOf("bread").forGetter(SandwichRecipeDisplay::bread),
				SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(SandwichRecipeDisplay::ingredients),
				SlotDisplay.CODEC.fieldOf("result").forGetter(SandwichRecipeDisplay::result),
				SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(SandwichRecipeDisplay::craftingStation)
			)
			.apply(i, SandwichRecipeDisplay::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, SandwichRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
		SlotDisplay.STREAM_CODEC, SandwichRecipeDisplay::bread,
		SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), SandwichRecipeDisplay::ingredients,
		SlotDisplay.STREAM_CODEC, SandwichRecipeDisplay::result,
		SlotDisplay.STREAM_CODEC, SandwichRecipeDisplay::craftingStation,
		SandwichRecipeDisplay::new
	);
	public static final Type<SandwichRecipeDisplay> TYPE = new Type<>(MAP_CODEC, STREAM_CODEC);

	@Override
	public Type<SandwichRecipeDisplay> type() {
		return TYPE;
	}

	@Override
	public boolean isEnabled(final FeatureFlagSet enabledFeatures) {
		return this.ingredients.stream().allMatch(e -> e.isEnabled(enabledFeatures)) && RecipeDisplay.super.isEnabled(enabledFeatures);
	}
}
