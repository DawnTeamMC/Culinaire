package fr.hugman.culinaire.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record SandwichContentsComponent(
        List<Entry> entries
) implements TooltipProvider {
    public static final Codec<SandwichContentsComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.list(Entry.CODEC).fieldOf("entries").forGetter(SandwichContentsComponent::entries)
                    )
                    .apply(instance, SandwichContentsComponent::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SandwichContentsComponent> PACKET_CODEC = StreamCodec.composite(
            Entry.PACKET_CODEC.apply(ByteBufCodecs.list()), SandwichContentsComponent::entries,
            SandwichContentsComponent::new
    );

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type, DataComponentGetter components) {
        for (Entry entry : entries) {
            tooltip.accept(((MutableComponent) entry.stack().getHoverName()).withStyle(entry.boosted ? ChatFormatting.GREEN : ChatFormatting.GRAY));
        }
    }

    public record Entry(ItemStack stack, boolean boosted) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                ItemStack.CODEC.fieldOf("stack").forGetter(Entry::stack),
                                Codec.BOOL.fieldOf("boosted").forGetter(Entry::boosted)
                        )
                        .apply(instance, Entry::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, Entry> PACKET_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC, Entry::stack,
                ByteBufCodecs.BOOL, Entry::boosted,
                Entry::new
        );
    }

}
