package com.hugman.culinaire.objects.tea;

import com.hugman.culinaire.objects.tea.effect.TeaEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

import java.util.List;

public record TeaPotency(int value, String name, String description, List<TeaEffect> effects, TagKey<Item> ingredients, int brewTime) {
	public static final Codec<TeaPotency> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.optionalFieldOf("value", 2).forGetter(TeaPotency::value),
			Codec.STRING.fieldOf("name").forGetter(TeaPotency::name),
			Codec.STRING.fieldOf("description").forGetter(TeaPotency::description),
			TeaEffect.TYPE_CODEC.listOf().fieldOf("effects").forGetter(TeaPotency::effects),
			TagKey.stringCodec(Registry.ITEM_KEY).fieldOf("ingredients").forGetter(TeaPotency::ingredients),
			Codec.INT.optionalFieldOf("brew_time", 200).forGetter(flavor -> flavor.brewTime)
	).apply(instance, TeaPotency::new));
}