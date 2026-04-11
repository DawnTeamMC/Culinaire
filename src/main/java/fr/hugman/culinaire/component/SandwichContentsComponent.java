package fr.hugman.culinaire.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.Consumer;

public record SandwichContentsComponent(
        List<Entry> entries
) implements TooltipAppender {
    public static final Codec<SandwichContentsComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.list(Entry.CODEC).fieldOf("entries").forGetter(SandwichContentsComponent::entries)
                    )
                    .apply(instance, SandwichContentsComponent::new)
    );
    public static final PacketCodec<RegistryByteBuf, SandwichContentsComponent> PACKET_CODEC = PacketCodec.tuple(
            Entry.PACKET_CODEC.collect(PacketCodecs.toList()), SandwichContentsComponent::entries,
            SandwichContentsComponent::new
    );

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type, ComponentsAccess components) {
        for (Entry entry : entries) {
            tooltip.accept(((MutableText) entry.stack().getName()).formatted(entry.boosted ? Formatting.GREEN : Formatting.GRAY));
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
        public static final PacketCodec<RegistryByteBuf, Entry> PACKET_CODEC = PacketCodec.tuple(
                ItemStack.PACKET_CODEC, Entry::stack,
                PacketCodecs.BOOLEAN, Entry::boosted,
                Entry::new
        );
    }

}
