package fr.hugman.culinaire.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.TeaTypesComponent;
import fr.hugman.culinaire.tea.TeaType;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

import java.util.Map;

public class TeaBagRecipe extends SpecialCraftingRecipe {
    private final Ingredient paper;
    private final Ingredient string;
    private final Map<RegistryEntry<TeaType>, Ingredient> teaTypeIngredients;
    private final ItemStack result;

    public TeaBagRecipe(CraftingRecipeCategory category, Ingredient paper, Ingredient string, Map<RegistryEntry<TeaType>, Ingredient> teaTypeIngredients, ItemStack result) {
        super(category);
        this.paper = paper;
        this.string = string;
        this.teaTypeIngredients = teaTypeIngredients;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        boolean hasPaper = false;
        boolean hasString = false;
        boolean hasAnIngredient = false;
        for (int j = 0; j < input.size(); ++j) {
            ItemStack stack = input.getStackInSlot(j);
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
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack givenStack = this.result.copy();
        Object2IntOpenHashMap<RegistryEntry<TeaType>> teaTypeLevels = new Object2IntOpenHashMap<>();
        for (int j = 0; j < input.size(); ++j) {
            ItemStack stack = input.getStackInSlot(j);
            if (!stack.isEmpty()) {
                for (Map.Entry<RegistryEntry<TeaType>, Ingredient> entry : this.teaTypeIngredients.entrySet()) {
                    RegistryEntry<TeaType> teaTypeEntry = entry.getKey();
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
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return CulinaireRecipeSerializers.TEA_BAG;
    }

    public static class Serializer implements RecipeSerializer<TeaBagRecipe> {
        private static final MapCodec<TeaBagRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        CraftingRecipeCategory.CODEC.fieldOf("category").forGetter(SpecialCraftingRecipe::getCategory),
                        Ingredient.CODEC.fieldOf("paper").forGetter(recipe -> recipe.paper),
                        Ingredient.CODEC.fieldOf("string").forGetter(recipe -> recipe.string),
                        Codec.unboundedMap(TeaType.ENTRY_CODEC, Ingredient.CODEC).fieldOf("tea_types_ingredients").forGetter(recipe -> recipe.teaTypeIngredients),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                ).apply(instance, TeaBagRecipe::new)
        );
        private static final PacketCodec<RegistryByteBuf, TeaBagRecipe> PACKET_CODEC = PacketCodec.tuple(
                CraftingRecipeCategory.PACKET_CODEC, SpecialCraftingRecipe::getCategory,
                Ingredient.PACKET_CODEC, recipe -> recipe.paper,
                Ingredient.PACKET_CODEC, recipe -> recipe.string,
                PacketCodecs.map(Object2ObjectOpenHashMap::new, TeaType.ENTRY_PACKET_CODEC, Ingredient.PACKET_CODEC), recipe -> recipe.teaTypeIngredients,
                ItemStack.PACKET_CODEC, recipe -> recipe.result,
                TeaBagRecipe::new
        );

        @Override
        public MapCodec<TeaBagRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, TeaBagRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
