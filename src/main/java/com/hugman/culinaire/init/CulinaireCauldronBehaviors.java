package com.hugman.culinaire.init;

import com.hugman.culinaire.objects.block.cauldron.AbstractLeveledCauldronBlock;
import com.hugman.culinaire.objects.block.cauldron.ThreeLeveledCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Map;

public interface CulinaireCauldronBehaviors extends CauldronBehavior {
	Map<Item, CauldronBehavior> MILK_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
	Map<Item, CauldronBehavior> CHEESE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
	Map<Item, CauldronBehavior> DARK_CHOCOLATE_BEHAVIOR = CauldronBehavior.createMap();
	Map<Item, CauldronBehavior> MILK_CHOCOLATE_BEHAVIOR = CauldronBehavior.createMap();
	Map<Item, CauldronBehavior> WHITE_CHOCOLATE_BEHAVIOR = CauldronBehavior.createMap();

	static void init() {
		registerBucketBehaviors(MILK_CAULDRON_BEHAVIOR, CulinaireFoodBundle.MILK_CAULDRON, Items.MILK_BUCKET);
		registerBottleBehaviors(MILK_CAULDRON_BEHAVIOR, CulinaireFoodBundle.MILK_CAULDRON, CulinaireFoodBundle.MILK_BOTTLE);
		registerOtherCauldronsBehaviors(MILK_CAULDRON_BEHAVIOR);

		registerBottleBehaviors(DARK_CHOCOLATE_BEHAVIOR, CulinaireFoodBundle.DARK_CHOCOLATE_CAULDRON, CulinaireFoodBundle.DARK_CHOCOLATE_BOTTLE);
		registerOtherCauldronsBehaviors(DARK_CHOCOLATE_BEHAVIOR);

		registerBottleBehaviors(MILK_CHOCOLATE_BEHAVIOR, CulinaireFoodBundle.MILK_CHOCOLATE_CAULDRON, CulinaireFoodBundle.MILK_CHOCOLATE_BOTTLE);
		registerOtherCauldronsBehaviors(MILK_CHOCOLATE_BEHAVIOR);

		registerBottleBehaviors(WHITE_CHOCOLATE_BEHAVIOR, CulinaireFoodBundle.WHITE_CHOCOLATE_CAULDRON, CulinaireFoodBundle.WHITE_CHOCOLATE_BOTTLE);
		registerOtherCauldronsBehaviors(WHITE_CHOCOLATE_BEHAVIOR);
	}

	static void registerBucketBehaviors(Map<Item, CauldronBehavior> cauldronBehavior, Block cauldron, Item bucket) {
		cauldronBehavior.put(Items.BUCKET, (state, world, pos, player, hand, stack) -> CauldronBehavior.emptyCauldron(state, world, pos, player, hand, stack, new ItemStack(bucket), (statex) -> statex.get(ThreeLeveledCauldronBlock.LEVEL) == 3, SoundEvents.ITEM_BUCKET_FILL));
		CauldronBehavior fillCauldron = (state, world, pos, player, hand, stack) -> CauldronBehavior.fillCauldron(world, pos, player, hand, stack, cauldron.getDefaultState().with(ThreeLeveledCauldronBlock.LEVEL, 3), SoundEvents.ITEM_BUCKET_EMPTY);
		if(cauldronBehavior != EMPTY_CAULDRON_BEHAVIOR) EMPTY_CAULDRON_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != WATER_CAULDRON_BEHAVIOR) WATER_CAULDRON_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != LAVA_CAULDRON_BEHAVIOR) LAVA_CAULDRON_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != POWDER_SNOW_CAULDRON_BEHAVIOR) POWDER_SNOW_CAULDRON_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != MILK_CAULDRON_BEHAVIOR) MILK_CAULDRON_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != DARK_CHOCOLATE_BEHAVIOR) DARK_CHOCOLATE_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != MILK_CHOCOLATE_BEHAVIOR) MILK_CHOCOLATE_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != WHITE_CHOCOLATE_BEHAVIOR) WHITE_CHOCOLATE_BEHAVIOR.put(bucket, fillCauldron);
	}

	static void registerBottleBehaviors(Map<Item, CauldronBehavior> cauldronBehavior, Block cauldron, Item bottle) {
		EMPTY_CAULDRON_BEHAVIOR.put(bottle, (state, world, pos, player, hand, stack) -> pourBottleInEmpty(cauldron.getDefaultState().with(ThreeLeveledCauldronBlock.LEVEL, 1), world, pos, player, hand, stack));
		cauldronBehavior.put(bottle, CulinaireCauldronBehaviors::pourBottleInSameCauldron);
		cauldronBehavior.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> fillBottleFromCauldron(new ItemStack(bottle), state, world, pos, player, hand, stack));
	}

	static void registerOtherCauldronsBehaviors(Map<Item, CauldronBehavior> cauldronBehavior) {
		cauldronBehavior.put(Items.LAVA_BUCKET, FILL_WITH_LAVA);
		cauldronBehavior.put(Items.WATER_BUCKET, FILL_WITH_WATER);
		cauldronBehavior.put(Items.POWDER_SNOW_BUCKET, FILL_WITH_POWDER_SNOW);
		cauldronBehavior.put(Items.MILK_BUCKET, (state, world, pos, player, hand, stack) -> CauldronBehavior.fillCauldron(world, pos, player, hand, stack, CulinaireFoodBundle.MILK_CAULDRON.getDefaultState().with(ThreeLeveledCauldronBlock.LEVEL, 3), SoundEvents.ITEM_BUCKET_EMPTY));
	}

	static ActionResult pourBottleInSameCauldron(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
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
	}

	static ActionResult pourBottleInEmpty(BlockState newState, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
		if(!world.isClient) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
			player.incrementStat(Stats.USE_CAULDRON);
			player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
			world.setBlockState(pos, newState);
			world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
		}
		return ActionResult.success(world.isClient);
	}

	static ActionResult fillBottleFromCauldron(ItemStack newBottle, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
		if(!world.isClient) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, newBottle));
			player.incrementStat(Stats.USE_CAULDRON);
			player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
			AbstractLeveledCauldronBlock.decrementFluidLevel(state, world, pos);
			world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
		}
		return ActionResult.success(world.isClient);
	}
}
