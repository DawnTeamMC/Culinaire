package fr.hugman.culinaire.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.codec.SuperPacketCodec;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.SandwichContentsComponent;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class SandwichRecipe extends CustomRecipe {
    private static final MapCodec<SandwichRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    CraftingBookCategory.CODEC.fieldOf("category").forGetter(CustomRecipe::category),
                    Ingredient.CODEC.fieldOf("bread").forGetter(recipe -> recipe.bread),
                    Ingredient.CODEC.fieldOf("ingredient_blacklist").forGetter(recipe -> recipe.ingredientBlacklist),
                    Codec.FLOAT.fieldOf("hunger_modifier_base").forGetter(recipe -> recipe.nutritionModifierBase),
                    Codec.FLOAT.fieldOf("hunger_modifier_boosted").forGetter(recipe -> recipe.nutritionModifierBoosted),
                    Codec.FLOAT.fieldOf("saturation_modifier_base").forGetter(recipe -> recipe.saturationModifierBase),
                    Codec.FLOAT.fieldOf("saturation_modifier_boosted").forGetter(recipe -> recipe.saturationModifierBoosted),
                    Codec.unboundedMap(Ingredient.CODEC, Ingredient.CODEC).fieldOf("ingredient_associations").forGetter(recipe -> recipe.ingredientAssociations),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
            ).apply(i, SandwichRecipe::new)
    );
    private static final StreamCodec<RegistryFriendlyByteBuf, SandwichRecipe> STREAM_CODEC = SuperPacketCodec.tuple(
            CraftingBookCategory.STREAM_CODEC, CustomRecipe::category,
            Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.bread,
            Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.ingredientBlacklist,
            ByteBufCodecs.FLOAT, recipe -> recipe.nutritionModifierBase,
            ByteBufCodecs.FLOAT, recipe -> recipe.nutritionModifierBoosted,
            ByteBufCodecs.FLOAT, recipe -> recipe.saturationModifierBase,
            ByteBufCodecs.FLOAT, recipe -> recipe.saturationModifierBoosted,
            ByteBufCodecs.map(Object2ObjectOpenHashMap::new, Ingredient.CONTENTS_STREAM_CODEC, Ingredient.CONTENTS_STREAM_CODEC), recipe -> recipe.ingredientAssociations,
            ItemStackTemplate.STREAM_CODEC, recipe -> recipe.result,
            SandwichRecipe::new
    );
    public static final RecipeSerializer<SandwichRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public final Ingredient bread;
    public final Ingredient ingredientBlacklist;
    public final float nutritionModifierBase;
    public final float nutritionModifierBoosted;
    public final float saturationModifierBase;
    public final float saturationModifierBoosted;
    public final Map<Ingredient, Ingredient> ingredientAssociations;
    public final ItemStackTemplate result;

    public SandwichRecipe(
            CraftingBookCategory category,
            Ingredient bread,
            Ingredient ingredientBlacklist,
            float nutritionModifierBase,
            float nutritionModifierBoosted,
            float saturationModifierBase,
            float saturationModifierBoosted,
            Map<Ingredient, Ingredient> ingredientAssociations,
            ItemStackTemplate result
    ) {
        //TODO: category is unused
        this.bread = bread;
        this.ingredientBlacklist = ingredientBlacklist;
        this.nutritionModifierBase = nutritionModifierBase;
        this.nutritionModifierBoosted = nutritionModifierBoosted;
        this.saturationModifierBase = saturationModifierBase;
        this.saturationModifierBoosted = saturationModifierBoosted;
        this.ingredientAssociations = ingredientAssociations;
        this.result = result;
    }

    @Override
    public RecipeSerializer<? extends SandwichRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public boolean matches(CraftingInput input, Level world) {
        boolean hasBread = false;
        boolean hasOnlyIngredients = false;
        int[] emptySlots = new int[]{0, 2, 6, 8};
        for (int emptySlot : emptySlots) {
            ItemStack itemStack = input.getItem(emptySlot);
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        ItemStack topMiddleStack = input.getItem(1);
        ItemStack bottomMiddleStack = input.getItem(7);
        if (!topMiddleStack.isEmpty() && !bottomMiddleStack.isEmpty()) {
            if (bread.test(topMiddleStack) && bread.test(bottomMiddleStack)) {
                hasBread = true;
            }
        }
        for (int i = 3; i < 6; ++i) {
            ItemStack itemStack = input.getItem(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.has(DataComponents.FOOD) && !ingredientBlacklist.test(itemStack)) {
                    hasOnlyIngredients = true;
                } else {
                    return false;
                }
            }
        }
        return hasBread && hasOnlyIngredients;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        // Get food items
        ItemStack[] slots = new ItemStack[3];
        int j = 0;
        for (int i = 3; i <= 5; i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                slots[j] = stack;
                j++;
            }
        }
        ItemStack[] food = new ItemStack[j];
        System.arraycopy(slots, 0, food, 0, food.length);

        ItemStack givenStack = this.result.create();
        boolean[] associations = getAssociations(food);

        // Calculations
        float nutrition = 0.0f;
        float saturationModifier = 0.0f;
        boolean hasGlint = false;
        List<SandwichContentsComponent.Entry> content = new ArrayList<>();

        for (int i = 0; i < food.length; i++) {
            if (food[i].has(DataComponents.FOOD)) {
                var foodComponent = food[i].get(DataComponents.FOOD);
                nutrition += foodComponent.nutrition() * (associations[i] ? nutritionModifierBoosted : nutritionModifierBase);
                saturationModifier += foodComponent.saturation() * (associations[i] ? saturationModifierBoosted : saturationModifierBase);
            }

            content.add(new SandwichContentsComponent.Entry(food[i].copy(), associations[i]));
            if (food[i].getItem().isFoil(food[i]) || Boolean.TRUE.equals(food[i].get(DataComponents.ENCHANTMENT_GLINT_OVERRIDE)))
                hasGlint = true;

            //TODO: collect effects
        }

        givenStack.set(CulinaireComponentTypes.SANDWICH_CONTENTS, new SandwichContentsComponent(content));
        givenStack.set(DataComponents.FOOD, new FoodProperties(Mth.floor(nutrition), saturationModifier, false));
        givenStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, hasGlint);

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
}
