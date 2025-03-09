package fr.hugman.culinaire.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.codec.SuperPacketCodec;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.SandwichContentsComponent;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SandwichRecipe extends SpecialCraftingRecipe {
    public final Ingredient bread;
    public final Ingredient ingredientBlacklist;
    public final float nutritionModifierBase;
    public final float nutritionModifierBoosted;
    public final float saturationModifierBase;
    public final float saturationModifierBoosted;
    public final Map<Ingredient, Ingredient> ingredientAssociations;
    public final ItemStack resultItem;

    public SandwichRecipe(CraftingRecipeCategory category, Ingredient bread, Ingredient ingredientBlacklist, float nutritionModifierBase, float nutritionModifierBoosted, float saturationModifierBase, float saturationModifierBoosted, Map<Ingredient, Ingredient> ingredientAssociations, ItemStack resultItem) {
        super(category);
        this.bread = bread;
        this.ingredientBlacklist = ingredientBlacklist;
        this.nutritionModifierBase = nutritionModifierBase;
        this.nutritionModifierBoosted = nutritionModifierBoosted;
        this.saturationModifierBase = saturationModifierBase;
        this.saturationModifierBoosted = saturationModifierBoosted;
        this.ingredientAssociations = ingredientAssociations;
        this.resultItem = resultItem;
    }

    @Override
    public RecipeSerializer<? extends SandwichRecipe> getSerializer() {
        return CulinaireRecipeSerializers.SANDWICH_CRAFTING;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        boolean hasBread = false;
        boolean hasOnlyIngredients = false;
        int[] emptySlots = new int[]{0, 2, 6, 8};
        for (int emptySlot : emptySlots) {
            ItemStack itemStack = input.getStackInSlot(emptySlot);
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        ItemStack topMiddleStack = input.getStackInSlot(1);
        ItemStack bottomMiddleStack = input.getStackInSlot(7);
        if (!topMiddleStack.isEmpty() && !bottomMiddleStack.isEmpty()) {
            if (bread.test(topMiddleStack) && bread.test(bottomMiddleStack)) {
                hasBread = true;
            }
        }
        for (int i = 3; i < 6; ++i) {
            ItemStack itemStack = input.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.contains(DataComponentTypes.FOOD) && !ingredientBlacklist.test(itemStack)) {
                    hasOnlyIngredients = true;
                } else {
                    return false;
                }
            }
        }
        return hasBread && hasOnlyIngredients;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        // Get food items
        ItemStack[] slots = new ItemStack[3];
        int j = 0;
        for (int i = 3; i <= 5; i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty()) {
                slots[j] = stack;
                j++;
            }
        }
        ItemStack[] food = new ItemStack[j];
        System.arraycopy(slots, 0, food, 0, food.length);

        ItemStack givenStack = resultItem.copy();
        boolean[] associations = getAssociations(food);

        // Calculations
        float nutrition = 0.0f;
        float saturationModifier = 0.0f;
        boolean hasGlint = false;
        List<SandwichContentsComponent.Entry> content = new ArrayList<>();

        for (int i = 0; i < food.length; i++) {
            if (food[i].contains(DataComponentTypes.FOOD)) {
                var foodComponent = food[i].get(DataComponentTypes.FOOD);
                nutrition += foodComponent.nutrition() * (associations[i] ? nutritionModifierBoosted : nutritionModifierBase);
                saturationModifier += foodComponent.saturation() * (associations[i] ? saturationModifierBoosted : saturationModifierBase);
            }

            content.add(new SandwichContentsComponent.Entry(food[i].copy(), associations[i]));
            if (food[i].getItem().hasGlint(food[i]) || Boolean.TRUE.equals(food[i].get(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE)))
                hasGlint = true;

            //TODO: collect effects
        }

        givenStack.set(CulinaireComponentTypes.SANDWICH_CONTENTS, new SandwichContentsComponent(content));
        givenStack.set(DataComponentTypes.FOOD, new FoodComponent(MathHelper.floor(nutrition), saturationModifier, false));
        givenStack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, hasGlint);

        return givenStack;
    }

    private boolean areAssociated(ItemStack item1, ItemStack item2) {
        boolean b = false;
        for (Ingredient ingredient : ingredientAssociations.keySet()) {
            if (ingredient.test(item1)) b = ingredientAssociations.get(ingredient).test(item2);
            if (ingredient.test(item2)) b |= ingredientAssociations.get(ingredient).test(item1);
        }
        return b;
    }

    private boolean[] getAssociations(ItemStack... items) {
        if (items.length == 1) {
            return new boolean[]{false};
        } else if (items.length == 2) {
            boolean b = areAssociated(items[0], items[1]);
            return new boolean[]{b, b};
        } else {
            boolean[] tab = new boolean[items.length];
            for (int i = 0; i < items.length; i++) {
                for (int j = i; j < items.length; j++) {
                    if (i != j) {
                        if (areAssociated(items[i], items[j])) {
                            tab[i] = true;
                            tab[j] = true;
                        }
                    }
                }
            }
            return tab;
        }
    }

    public static class Serializer implements RecipeSerializer<SandwichRecipe> {
        private static final MapCodec<SandwichRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        CraftingRecipeCategory.CODEC.fieldOf("category").forGetter(SpecialCraftingRecipe::getCategory),
                        Ingredient.CODEC.fieldOf("bread").forGetter(recipe -> recipe.bread),
                        Ingredient.CODEC.fieldOf("ingredient_blacklist").forGetter(recipe -> recipe.ingredientBlacklist),
                        Codec.FLOAT.fieldOf("hunger_modifier_base").forGetter(recipe -> recipe.nutritionModifierBase),
                        Codec.FLOAT.fieldOf("hunger_modifier_boosted").forGetter(recipe -> recipe.nutritionModifierBoosted),
                        Codec.FLOAT.fieldOf("saturation_modifier_base").forGetter(recipe -> recipe.saturationModifierBase),
                        Codec.FLOAT.fieldOf("saturation_modifier_boosted").forGetter(recipe -> recipe.saturationModifierBoosted),
                        Codec.unboundedMap(Ingredient.CODEC, Ingredient.CODEC).fieldOf("ingredient_associations").forGetter(recipe -> recipe.ingredientAssociations),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.resultItem)
                ).apply(instance, SandwichRecipe::new)
        );
        private static final PacketCodec<RegistryByteBuf, SandwichRecipe> PACKET_CODEC = SuperPacketCodec.tuple(
                CraftingRecipeCategory.PACKET_CODEC, SpecialCraftingRecipe::getCategory,
                Ingredient.PACKET_CODEC, recipe -> recipe.bread,
                Ingredient.PACKET_CODEC, recipe -> recipe.ingredientBlacklist,
                PacketCodecs.FLOAT, recipe -> recipe.nutritionModifierBase,
                PacketCodecs.FLOAT, recipe -> recipe.nutritionModifierBoosted,
                PacketCodecs.FLOAT, recipe -> recipe.saturationModifierBase,
                PacketCodecs.FLOAT, recipe -> recipe.saturationModifierBoosted,
                PacketCodecs.map(Object2ObjectOpenHashMap::new, Ingredient.PACKET_CODEC, Ingredient.PACKET_CODEC), recipe -> recipe.ingredientAssociations,
                ItemStack.PACKET_CODEC, recipe -> recipe.resultItem,
                SandwichRecipe::new
        );

        @Override
        public MapCodec<SandwichRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, SandwichRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
