package com.hugman.culinaire.init;

import com.hugman.culinaire.objects.block.AbstractLeveledCauldronBlock;
import com.hugman.culinaire.objects.block.MilkCauldronBlock;
import com.hugman.culinaire.objects.block.ThreeLeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;

import java.util.Map;

public interface CulinaireCauldronBehaviors extends CauldronBehavior {
	Map<Item, CauldronBehavior> MILK_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
	CauldronBehavior FILL_WITH_MILK = (state, world, pos, player, hand, stack) -> CauldronBehavior.fillCauldron(world, pos, player, hand, stack, CulinaireBlocks.MILK_CAULDRON.getDefaultState(), SoundEvents.ITEM_BUCKET_EMPTY);

	static void init() {
		EMPTY_CAULDRON_BEHAVIOR.put(Items.MILK_BUCKET, FILL_WITH_MILK);
		EMPTY_CAULDRON_BEHAVIOR.put(CulinaireItems.MILK_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if (!world.isClient) {
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
				player.incrementStat(Stats.USE_CAULDRON);
				world.setBlockState(pos, CulinaireBlocks.MILK_CAULDRON.getDefaultState().with(MilkCauldronBlock.COAGULATED, false).with(MilkCauldronBlock.LEVEL, 1));
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}

			return ActionResult.success(world.isClient);
		});

		MILK_CAULDRON_BEHAVIOR.put(CulinaireItems.MILK_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if (state.get(ThreeLeveledCauldronBlock.LEVEL) != 3) {
				if (!world.isClient) {
					player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
					player.incrementStat(Stats.USE_CAULDRON);
					AbstractLeveledCauldronBlock.incrementFluidLevel(state, world, pos);
					world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}

				return ActionResult.success(world.isClient);
			} else {
				return ActionResult.PASS;
			}
		});
		MILK_CAULDRON_BEHAVIOR.put(Items.BUCKET, (state, world, pos, player, hand, stack) -> CauldronBehavior.emptyCauldron(state, world, pos, player, hand, stack, new ItemStack(Items.MILK_BUCKET), (statex) -> true, SoundEvents.ITEM_BUCKET_FILL));
		MILK_CAULDRON_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if (!world.isClient) {
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(CulinaireItems.MILK_BOTTLE)));
				player.incrementStat(Stats.USE_CAULDRON);
				MilkCauldronBlock.decrementFluidLevel(state, world, pos);
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}

			return ActionResult.success(world.isClient);
		});
	}
}
