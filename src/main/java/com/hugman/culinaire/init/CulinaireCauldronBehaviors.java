package com.hugman.culinaire.init;

import com.hugman.culinaire.objects.block.cauldron.AbstractLeveledCauldronBlock;
import com.hugman.culinaire.objects.block.cauldron.ThreeLeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.world.event.GameEvent;

import java.util.Map;

public interface CulinaireCauldronBehaviors extends CauldronBehavior {
	Map<Item, CauldronBehavior> MILK_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
	Map<Item, CauldronBehavior> CHEESE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
	Map<Item, CauldronBehavior> DARK_CHOCOLATE_BEHAVIOR = CauldronBehavior.createMap();
	Map<Item, CauldronBehavior> MILK_CHOCOLATE_BEHAVIOR = CauldronBehavior.createMap();
	Map<Item, CauldronBehavior> WHITE_CHOCOLATE_BEHAVIOR = CauldronBehavior.createMap();
	CauldronBehavior FILL_WITH_MILK = (state, world, pos, player, hand, stack) -> CauldronBehavior.fillCauldron(world, pos, player, hand, stack, CulinaireFoodBundle.MILK_CAULDRON.getDefaultState().with(ThreeLeveledCauldronBlock.LEVEL, 3), SoundEvents.ITEM_BUCKET_EMPTY);

	static void init() {
		EMPTY_CAULDRON_BEHAVIOR.put(Items.MILK_BUCKET, FILL_WITH_MILK);
		WATER_CAULDRON_BEHAVIOR.put(Items.MILK_BUCKET, FILL_WITH_MILK);
		LAVA_CAULDRON_BEHAVIOR.put(Items.MILK_BUCKET, FILL_WITH_MILK);
		POWDER_SNOW_CAULDRON_BEHAVIOR.put(Items.MILK_BUCKET, FILL_WITH_MILK);

		EMPTY_CAULDRON_BEHAVIOR.put(CulinaireFoodBundle.MILK_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if(!world.isClient) {
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
				player.incrementStat(Stats.USE_CAULDRON);
				player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
				world.setBlockState(pos, CulinaireFoodBundle.MILK_CAULDRON.getDefaultState().with(ThreeLeveledCauldronBlock.LEVEL, 1));
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
			}
			return ActionResult.success(world.isClient);
		});
		EMPTY_CAULDRON_BEHAVIOR.put(CulinaireFoodBundle.DARK_CHOCOLATE_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if(!world.isClient) {
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
				player.incrementStat(Stats.USE_CAULDRON);
				player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
				world.setBlockState(pos, CulinaireFoodBundle.DARK_CHOCOLATE_CAULDRON.getDefaultState().with(ThreeLeveledCauldronBlock.LEVEL, 1));
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
			}
			return ActionResult.success(world.isClient);
		});
		EMPTY_CAULDRON_BEHAVIOR.put(CulinaireFoodBundle.MILK_CHOCOLATE_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if(!world.isClient) {
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
				player.incrementStat(Stats.USE_CAULDRON);
				player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
				world.setBlockState(pos, CulinaireFoodBundle.MILK_CHOCOLATE_CAULDRON.getDefaultState().with(ThreeLeveledCauldronBlock.LEVEL, 1));
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
			}
			return ActionResult.success(world.isClient);
		});
		EMPTY_CAULDRON_BEHAVIOR.put(CulinaireFoodBundle.WHITE_CHOCOLATE_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if(!world.isClient) {
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
				player.incrementStat(Stats.USE_CAULDRON);
				player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
				world.setBlockState(pos, CulinaireFoodBundle.WHITE_CHOCOLATE_CAULDRON.getDefaultState().with(ThreeLeveledCauldronBlock.LEVEL, 1));
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
			}
			return ActionResult.success(world.isClient);
		});

		MILK_CAULDRON_BEHAVIOR.put(CulinaireFoodBundle.MILK_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if(state.get(ThreeLeveledCauldronBlock.LEVEL) != 3) {
				if(!world.isClient) {
					player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
					player.incrementStat(Stats.USE_CAULDRON);
					player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
					AbstractLeveledCauldronBlock.incrementFluidLevel(state, world, pos);
					world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
					world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
				}
				return ActionResult.success(world.isClient);
			}
			else {
				return ActionResult.PASS;
			}
		});
		MILK_CAULDRON_BEHAVIOR.put(Items.BUCKET, (state, world, pos, player, hand, stack) -> CauldronBehavior.emptyCauldron(state, world, pos, player, hand, stack, new ItemStack(Items.MILK_BUCKET), (statex) -> statex.get(ThreeLeveledCauldronBlock.LEVEL) == 3, SoundEvents.ITEM_BUCKET_FILL));
		MILK_CAULDRON_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if(!world.isClient) {
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(CulinaireFoodBundle.MILK_BOTTLE)));
				player.incrementStat(Stats.USE_CAULDRON);
				player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
				AbstractLeveledCauldronBlock.decrementFluidLevel(state, world, pos);
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
			}
			return ActionResult.success(world.isClient);
		});
		registerBucketBehaviors(MILK_CAULDRON_BEHAVIOR);

		DARK_CHOCOLATE_BEHAVIOR.put(CulinaireFoodBundle.DARK_CHOCOLATE_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if(state.get(ThreeLeveledCauldronBlock.LEVEL) != 3) {
				if(!world.isClient) {
					player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
					player.incrementStat(Stats.USE_CAULDRON);
					player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
					AbstractLeveledCauldronBlock.incrementFluidLevel(state, world, pos);
					world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
					world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
				}
				return ActionResult.success(world.isClient);
			}
			else {
				return ActionResult.PASS;
			}
		});
		DARK_CHOCOLATE_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if(!world.isClient) {
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(CulinaireFoodBundle.DARK_CHOCOLATE_BOTTLE)));
				player.incrementStat(Stats.USE_CAULDRON);
				player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
				AbstractLeveledCauldronBlock.decrementFluidLevel(state, world, pos);
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
			}
			return ActionResult.success(world.isClient);
		});
		registerBucketBehaviors(DARK_CHOCOLATE_BEHAVIOR);

		MILK_CHOCOLATE_BEHAVIOR.put(CulinaireFoodBundle.MILK_CHOCOLATE_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if(state.get(ThreeLeveledCauldronBlock.LEVEL) != 3) {
				if(!world.isClient) {
					player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
					player.incrementStat(Stats.USE_CAULDRON);
					player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
					AbstractLeveledCauldronBlock.incrementFluidLevel(state, world, pos);
					world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
					world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
				}
				return ActionResult.success(world.isClient);
			}
			else {
				return ActionResult.PASS;
			}
		});
		MILK_CHOCOLATE_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if(!world.isClient) {
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(CulinaireFoodBundle.MILK_CHOCOLATE_BOTTLE)));
				player.incrementStat(Stats.USE_CAULDRON);
				player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
				AbstractLeveledCauldronBlock.decrementFluidLevel(state, world, pos);
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
			}
			return ActionResult.success(world.isClient);
		});
		registerBucketBehaviors(MILK_CHOCOLATE_BEHAVIOR);

		WHITE_CHOCOLATE_BEHAVIOR.put(CulinaireFoodBundle.WHITE_CHOCOLATE_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if(state.get(ThreeLeveledCauldronBlock.LEVEL) != 3) {
				if(!world.isClient) {
					player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
					player.incrementStat(Stats.USE_CAULDRON);
					player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
					AbstractLeveledCauldronBlock.incrementFluidLevel(state, world, pos);
					world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
					world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
				}
				return ActionResult.success(world.isClient);
			}
			else {
				return ActionResult.PASS;
			}
		});
		WHITE_CHOCOLATE_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if(!world.isClient) {
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(CulinaireFoodBundle.WHITE_CHOCOLATE_BOTTLE)));
				player.incrementStat(Stats.USE_CAULDRON);
				player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
				AbstractLeveledCauldronBlock.decrementFluidLevel(state, world, pos);
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
			}
			return ActionResult.success(world.isClient);
		});
		registerBucketBehaviors(WHITE_CHOCOLATE_BEHAVIOR);
	}

	static void registerBucketBehaviors(Map<Item, CauldronBehavior> behaviorMap) {
		behaviorMap.put(Items.LAVA_BUCKET, FILL_WITH_LAVA);
		behaviorMap.put(Items.WATER_BUCKET, FILL_WITH_WATER);
		behaviorMap.put(Items.POWDER_SNOW_BUCKET, FILL_WITH_POWDER_SNOW);
		behaviorMap.put(Items.MILK_BUCKET, FILL_WITH_MILK);
	}
}
