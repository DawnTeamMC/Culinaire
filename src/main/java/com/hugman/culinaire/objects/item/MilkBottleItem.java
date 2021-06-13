package com.hugman.culinaire.objects.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class MilkBottleItem extends Item {
	public MilkBottleItem(Settings settings) {
		super(settings);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
		if(user instanceof ServerPlayerEntity serverPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
			serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
		}
		if(!world.isClient) {
			user.clearStatusEffects();
		}
		if(playerEntity != null) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			if(!playerEntity.getAbilities().creativeMode) {
				stack.decrement(1);
			}
		}
		if(playerEntity == null || !playerEntity.getAbilities().creativeMode) {
			if(stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}
			if(playerEntity != null) {
				playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
			}
		}
		return stack;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 32;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
}
