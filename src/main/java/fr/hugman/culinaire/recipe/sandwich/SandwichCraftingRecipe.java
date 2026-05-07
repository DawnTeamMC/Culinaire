package fr.hugman.culinaire.recipe.sandwich;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.item.sandwich.SandwichIngredients;
import fr.hugman.culinaire.recipe.sandwich.ingredient.SandwichCraftingIngredient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.*;

//TODO: check max complements
//TODO: check that base ingredients + complements < 3
public class SandwichCraftingRecipe extends NormalCraftingRecipe {
    public static final int BREAD_COUNT = 2;

    // Defaults
    public static final boolean DEFAULT_SHAPED = true;

    public static final MapCodec<SandwichCraftingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo),
            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(r -> r.bookInfo),
            Ingredient.CODEC.fieldOf("bread").forGetter(r -> r.bread),
            Ingredient.CODEC.listOf().fieldOf("base_ingredients").forGetter(r -> r.baseIngredients),
            SandwichCraftingIngredient.CODEC.listOf().fieldOf("complements").forGetter(r -> r.complements),
            Codec.INT.fieldOf("max_complements").forGetter(r -> r.maxComplements),
            Codec.BOOL.optionalFieldOf("shaped", DEFAULT_SHAPED).forGetter(r -> r.shaped),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.result)
    ).apply(i, SandwichCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SandwichCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, r -> r.commonInfo,
            CraftingRecipe.CraftingBookInfo.STREAM_CODEC, r -> r.bookInfo,
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.bread,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.baseIngredients,
            SandwichCraftingIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.complements,
            ByteBufCodecs.INT, r -> r.maxComplements,
            ByteBufCodecs.BOOL, r -> r.shaped,
            ItemStackTemplate.STREAM_CODEC, r -> r.result,
            SandwichCraftingRecipe::new
    );

    public static final RecipeSerializer<SandwichCraftingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final Ingredient bread;
    private final List<Ingredient> baseIngredients;
    private final List<SandwichCraftingIngredient> complements;
    private final int maxComplements;
    private final boolean shaped;
    private final ItemStackTemplate result;

    protected SandwichCraftingRecipe(
            CommonInfo commonInfo,
            CraftingBookInfo bookInfo,
            Ingredient bread,
            List<Ingredient> baseIngredients,
            List<SandwichCraftingIngredient> complements,
            int maxComplements,
            boolean shaped,
            ItemStackTemplate result
    ) {
        super(commonInfo, bookInfo);
        this.bread = bread;
        this.baseIngredients = baseIngredients;
        this.complements = complements;
        this.maxComplements = maxComplements;
        this.shaped = shaped;
        this.result = result;
    }

    protected SandwichCraftingRecipe(
            CommonInfo commonInfo,
            CraftingBookInfo bookInfo,
            Ingredient bread,
            List<Ingredient> baseIngredients,
            List<SandwichCraftingIngredient> complements,
            int maxComplements,
            ItemStackTemplate result
    ) {
        this(commonInfo, bookInfo, bread, baseIngredients, complements, maxComplements, DEFAULT_SHAPED, result);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        var minCount = 2 + this.baseIngredients.size();
        if(input.ingredientCount() < minCount) {
            return false;
        }

        int breadCount = 0;
        Map<Ingredient, Boolean> baseIngredients = new HashMap<>();
        int complementsCount = 0;

        main:
        for(int i = 0; i < input.size(); i++) {
            var stack = input.getItem(i);
            if(stack.isEmpty()) {
                continue;
            }
            if(this.bread.test(stack)) {
                breadCount++;
                if(breadCount > BREAD_COUNT) {
                    return false;
                }
                continue;
            }
            for(var baseIngredient : this.baseIngredients) {
                if(baseIngredient.test(stack)) {
                    if(baseIngredients.containsKey(baseIngredient)) {
                        return false;
                    }
                    baseIngredients.put(baseIngredient, true);
                    continue main;
                }
            }
            for(var complement : this.complements) {
                if(complement.ingredient().test(stack)) {
                    complementsCount++;
                    if(complementsCount > this.maxComplements) {
                        return false;
                    }
                    continue main;
                }
            }
            return false;
        }

        return breadCount == BREAD_COUNT && baseIngredients.size() == this.baseIngredients.size();
    }

    @Override
    @NonNull
    public ItemStack assemble(CraftingInput input) {
        var entries = new ArrayList<SandwichIngredients.Entry>();

        for(int i = 0; i < input.size(); i++) {
            var stack = input.getItem(i);
            for(var complement : this.complements) {
                if(complement.ingredient().test(stack)) {
                    entries.add(new SandwichIngredients.Entry(new ItemStackTemplate(stack.getItem(), stack.getComponentsPatch()), complement.properties()));
                }
            }
        }

        var result = this.result.create();

        result.set(CulinaireComponentTypes.SANDWICH_INGREDIENTS, new SandwichIngredients(entries));

        return result;
    }

    @Override
    public RecipeSerializer<? extends NormalCraftingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        // TODO: cache this
        if(this.shaped) {
            var list = new ArrayList<Optional<Ingredient>>();
            list.add(Optional.empty());
            list.add(Optional.of(this.bread));
            list.add(Optional.empty());

            // Middle row = 3 max
            // if 1 then center
            if(this.baseIngredients.size() == 1) {
                list.add(Optional.empty());
                list.add(Optional.of(this.baseIngredients.get(0)));
                list.add(Optional.empty());
            }
            else {
                for(var ingredient : this.baseIngredients) {
                    list.add(Optional.of(ingredient));
                }
                for(int i = this.baseIngredients.size(); i < 3; i++) {
                    list.add(Optional.empty());
                }
            }

            list.add(Optional.empty());
            list.add(Optional.of(this.bread));
            list.add(Optional.empty());

            return PlacementInfo.createFromOptionals(list);
        }
        else {
            var list = new ArrayList<Ingredient>();

            for (int i = 0; i < BREAD_COUNT; i++) {
                list.add(this.bread);
            }
            list.addAll(this.baseIngredients.stream().toList());

            return PlacementInfo.create(list);
        }
    }

    @Override
    public List<RecipeDisplay> display() {
        // 3x3 display
        // Top row, middle item is bread
        // Middle row is base ingredients
        // Bottom row is complements

        List<SlotDisplay> slotDisplays = new ArrayList<>();
        slotDisplays.add(SlotDisplay.Empty.INSTANCE);
        slotDisplays.add(this.bread.display());
        slotDisplays.add(SlotDisplay.Empty.INSTANCE);

        // Middle row = 3 max
        // if 1 then center
        if(this.baseIngredients.size() == 1) {
            slotDisplays.add(SlotDisplay.Empty.INSTANCE);
            slotDisplays.add(this.baseIngredients.get(0).display());
            slotDisplays.add(SlotDisplay.Empty.INSTANCE);
        }
        else {
            slotDisplays.addAll(this.baseIngredients.stream().map(Ingredient::display).toList());
            for(int i = this.baseIngredients.size(); i < 3; i++) {
                slotDisplays.add(SlotDisplay.Empty.INSTANCE);
            }
        }

        slotDisplays.add(SlotDisplay.Empty.INSTANCE);
        slotDisplays.add(this.bread.display());
        slotDisplays.add(SlotDisplay.Empty.INSTANCE);

        return List.of(
                new ShapedCraftingRecipeDisplay(3, 3, slotDisplays, new SlotDisplay.ItemStackSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE))
        );
    }
}
