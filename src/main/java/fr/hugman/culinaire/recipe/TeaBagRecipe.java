package fr.hugman.culinaire.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.TeaTypesComponent;
import fr.hugman.culinaire.tea.TeaType;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class TeaBagRecipe extends CustomRecipe {
    private final Ingredient paper;
    private final Ingredient string;
    private final Map<Holder<TeaType>, Ingredient> teaTypeIngredients;
    private final ItemStack result;

    public TeaBagRecipe(CraftingBookCategory category, Ingredient paper, Ingredient string, Map<Holder<TeaType>, Ingredient> teaTypeIngredients, ItemStack result) {
        super(category);
        this.paper = paper;
        this.string = string;
        this.teaTypeIngredients = teaTypeIngredients;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput input, Level world) {
        boolean hasPaper = false;
        boolean hasString = false;
        boolean hasAnIngredient = false;
        for (int j = 0; j < input.size(); ++j) {
            ItemStack stack = input.getItem(j);
            if (!stack.isEmpty()) {
                if (this.paper.test(stack)) {
                    if (hasPaper) {
                        return false;
                    }
                    hasPaper = true;
                } else if (this.string.test(stack)) {
                    if (hasString) {
                        return false;
                    }
                    hasString = true;
                } else {
                    for (var ingredient : this.teaTypeIngredients.values()) {
                        if (ingredient.test(stack)) {
                            hasAnIngredient = true;
                        }
                    }
                }
            }
        }
        return hasPaper && hasString && hasAnIngredient;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack givenStack = this.result.copy();
        Object2IntOpenHashMap<Holder<TeaType>> teaTypeLevels = new Object2IntOpenHashMap<>();
        for (int j = 0; j < input.size(); ++j) {
            ItemStack stack = input.getItem(j);
            if (!stack.isEmpty()) {
                for (Map.Entry<Holder<TeaType>, Ingredient> entry : this.teaTypeIngredients.entrySet()) {
                    Holder<TeaType> teaTypeEntry = entry.getKey();
                    if (entry.getValue().test(stack)) {
                        teaTypeLevels.put(teaTypeEntry, teaTypeLevels.getOrDefault(teaTypeEntry, 0) + 1);
                    }
                }
            }
        }
        givenStack.set(CulinaireComponentTypes.TEA_TYPES, new TeaTypesComponent(teaTypeLevels, true));
        return givenStack;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return CulinaireRecipeSerializers.TEA_BAG;
    }

    public static class Serializer implements RecipeSerializer<TeaBagRecipe> {
        private static final MapCodec<TeaBagRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        CraftingBookCategory.CODEC.fieldOf("category").forGetter(CustomRecipe::category),
                        Ingredient.CODEC.fieldOf("paper").forGetter(recipe -> recipe.paper),
                        Ingredient.CODEC.fieldOf("string").forGetter(recipe -> recipe.string),
                        Codec.unboundedMap(TeaType.ENTRY_CODEC, Ingredient.CODEC).fieldOf("tea_types_ingredients").forGetter(recipe -> recipe.teaTypeIngredients),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                ).apply(instance, TeaBagRecipe::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, TeaBagRecipe> PACKET_CODEC = StreamCodec.composite(
                CraftingBookCategory.STREAM_CODEC, CustomRecipe::category,
                Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.paper,
                Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.string,
                ByteBufCodecs.map(Object2ObjectOpenHashMap::new, TeaType.ENTRY_PACKET_CODEC, Ingredient.CONTENTS_STREAM_CODEC), recipe -> recipe.teaTypeIngredients,
                ItemStack.STREAM_CODEC, recipe -> recipe.result,
                TeaBagRecipe::new
        );

        @Override
        public MapCodec<TeaBagRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TeaBagRecipe> streamCodec() {
            return PACKET_CODEC;
        }
    }
}
