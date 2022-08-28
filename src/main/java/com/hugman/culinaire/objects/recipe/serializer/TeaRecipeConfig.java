package com.hugman.culinaire.objects.recipe.serializer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryCodecs;
import net.minecraft.util.registry.RegistryEntryList;

public record TeaRecipeConfig(RegistryEntryList<Item> ingredients, ItemStack result) {
	public static final Codec<TeaRecipeConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			RegistryCodecs.entryList(Registry.ITEM_KEY).fieldOf("ingredients").forGetter(TeaRecipeConfig::ingredients),
			ItemStack.CODEC.fieldOf("result").forGetter(TeaRecipeConfig::result)
	).apply(instance, TeaRecipeConfig::new));

	public static TeaRecipeConfig fromBuffer(PacketByteBuf buf) {
		ItemStack resultStack = buf.readItemStack();
	}


	public void toBuffer(PacketByteBuf buf) {
		DataResult<NbtElement> dataResult = CODEC.encodeStart(NbtOps.INSTANCE, this);
		buf.writeNbt(dataResult.result().get());
		buf.writeItemStack(this.result());
	}
}
