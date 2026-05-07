package fr.hugman.culinaire.item.sandwich;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.item.sandwich.value.SandwichIngredientValueModifier;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

//TODO: codec should unsort the lists
public record SandwichIngredients(List<Entry> entries) implements TooltipProvider {
    public static final Codec<SandwichIngredients> CODEC = Codec.list(Entry.CODEC).xmap(SandwichIngredients::new, SandwichIngredients::entries);
    public static final StreamCodec<RegistryFriendlyByteBuf, SandwichIngredients> STREAM_CODEC = StreamCodec.composite(
            Entry.STREAM_CODEC.apply(ByteBufCodecs.list()), SandwichIngredients::entries,
            SandwichIngredients::new
    );

    public float calculateValue(float base, Function<SandwichIngredient, SandwichIngredientValueModifier> mapper) {
        for (SandwichIngredientValueModifier modifier : this.getModifiers(mapper, AttributeModifier.Operation.ADD_VALUE)) {
            base += modifier.amount();
        }

        float result = base;

        for (SandwichIngredientValueModifier modifier : this.getModifiers(mapper, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)) {
            result += base * modifier.amount();
        }

        for (SandwichIngredientValueModifier modifier : this.getModifiers(mapper, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)) {
            result *= 1.0f + modifier.amount();
        }

        return result;
    }

    private List<SandwichIngredientValueModifier> getModifiers(Function<SandwichIngredient, SandwichIngredientValueModifier> mapper, AttributeModifier.Operation operation) {
        return this.entries.stream()
                .map(Entry::ingredient)
                .map(mapper)
                .filter(modifier -> modifier.operation() == operation)
                .toList();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        //TODO: this is obivoulsy not final
        for (Entry entry : this.entries) {
            consumer.accept(Component.literal("- With: ").append(entry.stack.create().getHoverName()));
        }
    }

    //TODO: support higher counts
    public record Entry(ItemStackTemplate stack, SandwichIngredient ingredient) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group(
                ItemStackTemplate.CODEC.fieldOf("stack").forGetter(Entry::stack),
                SandwichIngredient.CODEC.fieldOf("ingredient").forGetter(Entry::ingredient)
        ).apply(i, Entry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
                ItemStackTemplate.STREAM_CODEC, Entry::stack,
                SandwichIngredient.STREAM_CODEC, Entry::ingredient,
                Entry::new
        );
    }
}
