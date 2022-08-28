package com.hugman.culinaire.objects.tea.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public record ConsumeItemTeaEffect(Item item) implements TeaEffect {
	public static final Codec<ConsumeItemTeaEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Registry.ITEM.getCodec().fieldOf("ingredients").forGetter(ConsumeItemTeaEffect::item)
	).apply(instance, ConsumeItemTeaEffect::new));

	@Override
	public void apply(LivingEntity user, ItemStack stack, World world) {
		this.item.finishUsing(stack, world, user);
	}

	@Override
	public TeaEffectType<ConsumeItemTeaEffect> getType() {
		return TeaEffectType.CONSUME_ITEM;
	}
}