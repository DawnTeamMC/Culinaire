package fr.hugman.culinaire.recipe.sandwich;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.item.sandwich.SandwichIngredients;
import fr.hugman.culinaire.recipe.CulinaireRecipeTypes;
import fr.hugman.culinaire.recipe.display.SandwichRecipeDisplay;
import fr.hugman.culinaire.recipe.sandwich.ingredient.SandwichIngredientProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class SandwichRecipe implements Recipe<SandwichInput> {
    public static final MapCodec<SandwichRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo),
            BookInfo.MAP_CODEC.forGetter(r -> r.bookInfo),
            Ingredient.CODEC.fieldOf("bread").forGetter(r -> r.bread),
            Ingredient.CODEC.listOf().fieldOf("base_ingredients").forGetter(r -> r.mainIngredients),
            SandwichIngredientProvider.CODEC.listOf().fieldOf("complements").forGetter(r -> r.complements),
            Codec.INT.fieldOf("max_complements").forGetter(r -> r.maxComplements),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.result)
    ).apply(i, SandwichRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SandwichRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, r -> r.commonInfo,
            BookInfo.STREAM_CODEC, r -> r.bookInfo,
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.bread,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.mainIngredients,
            SandwichIngredientProvider.STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.complements,
            ByteBufCodecs.INT, r -> r.maxComplements,
            ItemStackTemplate.STREAM_CODEC, r -> r.result,
            SandwichRecipe::new
    );

    public static final RecipeSerializer<SandwichRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final CommonInfo commonInfo;
    private final BookInfo bookInfo;
    private final Ingredient bread;
    private final List<Ingredient> mainIngredients;
    private final List<SandwichIngredientProvider> complements;
    private final int maxComplements;
    private final ItemStackTemplate result;

    private PlacementInfo placementInfo;

    protected SandwichRecipe(
            CommonInfo commonInfo,
            BookInfo bookInfo,
            Ingredient bread,
            List<Ingredient> mainIngredients,
            List<SandwichIngredientProvider> complements,
            int maxComplements,
            ItemStackTemplate result
    ) {
        this.commonInfo = commonInfo;
        this.bookInfo = bookInfo;
        this.bread = bread;
        this.mainIngredients = mainIngredients;
        this.complements = complements;
        this.maxComplements = maxComplements;
        this.result = result;
    }

    public Ingredient getBread() {
        return bread;
    }

    @Override
    public boolean matches(SandwichInput input, Level level) {
        // Test bread
        if(!this.bread.test(input.getTopBread()) || !this.bread.test(input.getBottomBread())) {
            return false;
        }

        // Test main ingredients
        Map<Ingredient, Boolean> mainIngredients = new HashMap<>();
        int complementsCount = 0;
        main:
        for(var stack : input.getIngredients()) {
            if(stack.isEmpty()) {
                continue;
            }
            for(var baseIngredient : this.mainIngredients) {
                if(baseIngredient.test(stack)) {
                    if(mainIngredients.containsKey(baseIngredient)) {
                        return false;
                    }
                    mainIngredients.put(baseIngredient, true);
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

        return mainIngredients.size() == this.mainIngredients.size();
    }

    @Override
    @NonNull
    public ItemStack assemble(SandwichInput input) {
        var entries = new ArrayList<SandwichIngredients.Entry>();

        for(var stack : input.getIngredients()) {
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
    public boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    public String group() {
        return this.bookInfo.group();
    }

    @Override
    public RecipeSerializer<? extends SandwichRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<SandwichInput>> getType() {
        return CulinaireRecipeTypes.SANDWICH;
    }

    @Override
    public PlacementInfo placementInfo() {
        if(this.placementInfo == null) {
            this.placementInfo = this.createPlacementInfo();
        }
        return this.placementInfo;
    }

    protected PlacementInfo createPlacementInfo() {
        // TODO: cache this
        var list = new ArrayList<Optional<Ingredient>>();

        list.add(Optional.of(this.bread));
        int count = 1;
        for (Ingredient mainIngredient : this.mainIngredients) {
            list.add(Optional.of(mainIngredient));
            count++;
            if (count == 6) {
                break;
            }
        }

        while (count < 7) {
            list.add(Optional.empty());
            count++;
        }
        list.add(Optional.of(this.bread));

        return PlacementInfo.createFromOptionals(list);
    }

    @Override
    public List<RecipeDisplay> display() {
        List<SlotDisplay> slotDisplays = new ArrayList<>();
        slotDisplays.addAll(this.mainIngredients.stream().map(Ingredient::display).toList());

        //TODO: display complements too
        return List.of(new SandwichRecipeDisplay(
                this.bread.display(),
                slotDisplays,
                new SlotDisplay.ItemStackSlotDisplay(this.result),
                new SlotDisplay.ItemSlotDisplay(CulinaireBlocks.SANDWICH_MAKING_TABLE.asItem())
        ));
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return switch (this.bookInfo.category) {
            case BUILDING -> RecipeBookCategories.CRAFTING_BUILDING_BLOCKS;
            case EQUIPMENT -> RecipeBookCategories.CRAFTING_EQUIPMENT;
            case REDSTONE -> RecipeBookCategories.CRAFTING_REDSTONE;
            case MISC -> RecipeBookCategories.CRAFTING_MISC;
        };
    }

    public NonNullList<ItemStack> getRemainingItems(SandwichInput input) {
        return defaultCraftingReminder(input);
    }

    public static NonNullList<ItemStack> defaultCraftingReminder(SandwichInput input) {
        NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int slot = 0; slot < result.size(); slot++) {
            Item item = input.getItem(slot).getItem();
            ItemStackTemplate remainder = item.getCraftingRemainder();
            result.set(slot, remainder != null ? remainder.create() : ItemStack.EMPTY);
        }

        return result;
    }

    //TODO: different categories
    public record BookInfo(CraftingBookCategory category, String group) implements Recipe.BookInfo<CraftingBookCategory> {
        public static final MapCodec<BookInfo> MAP_CODEC = Recipe.BookInfo.mapCodec(
                CraftingBookCategory.CODEC, CraftingBookCategory.MISC, BookInfo::new
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, BookInfo> STREAM_CODEC = Recipe.BookInfo.streamCodec(
                CraftingBookCategory.STREAM_CODEC, BookInfo::new
        );
    }
}
