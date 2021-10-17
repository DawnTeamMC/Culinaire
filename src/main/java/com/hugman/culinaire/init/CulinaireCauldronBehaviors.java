package com.hugman.culinaire.init;

import com.hugman.culinaire.objects.block.cauldron.AbstractLeveledCauldronBlock;
import com.hugman.culinaire.objects.block.cauldron.ThreeLeveledCauldronBlock;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
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
		registerBottleBehaviors(MILK_CAULDRON_BEHAVIOR, (AbstractLeveledCauldronBlock) CulinaireFoodBundle.MILK_CAULDRON, CulinaireFoodBundle.MILK_BOTTLE);
		registerOtherCauldronsBehaviors(MILK_CAULDRON_BEHAVIOR);

		registerBottleBehaviors(DARK_CHOCOLATE_BEHAVIOR, (AbstractLeveledCauldronBlock) CulinaireFoodBundle.DARK_CHOCOLATE_CAULDRON, CulinaireFoodBundle.DARK_CHOCOLATE_BOTTLE);
		registerOtherCauldronsBehaviors(DARK_CHOCOLATE_BEHAVIOR);

		registerBottleBehaviors(MILK_CHOCOLATE_BEHAVIOR, (AbstractLeveledCauldronBlock) CulinaireFoodBundle.MILK_CHOCOLATE_CAULDRON, CulinaireFoodBundle.MILK_CHOCOLATE_BOTTLE);
		registerOtherCauldronsBehaviors(MILK_CHOCOLATE_BEHAVIOR);

		registerBottleBehaviors(WHITE_CHOCOLATE_BEHAVIOR, (AbstractLeveledCauldronBlock) CulinaireFoodBundle.WHITE_CHOCOLATE_CAULDRON, CulinaireFoodBundle.WHITE_CHOCOLATE_BOTTLE);
		registerOtherCauldronsBehaviors(WHITE_CHOCOLATE_BEHAVIOR);
	}

	static void registerBucketBehaviors(Map<Item, CauldronBehavior> cauldronBehavior, Block cauldron, Item bucket) {
		cauldronBehavior.put(Items.BUCKET, (state, world, pos, player, hand, stack) -> CauldronBehavior.emptyCauldron(state, world, pos, player, hand, stack, new ItemStack(bucket), (statex) -> statex.get(ThreeLeveledCauldronBlock.LEVEL) == 3, SoundEvents.ITEM_BUCKET_FILL));
		CauldronBehavior fillCauldron = fillCauldronFromBucket((AbstractLeveledCauldronBlock)cauldron);
		if(cauldronBehavior != EMPTY_CAULDRON_BEHAVIOR) EMPTY_CAULDRON_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != WATER_CAULDRON_BEHAVIOR) WATER_CAULDRON_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != LAVA_CAULDRON_BEHAVIOR) LAVA_CAULDRON_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != POWDER_SNOW_CAULDRON_BEHAVIOR) POWDER_SNOW_CAULDRON_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != MILK_CAULDRON_BEHAVIOR) MILK_CAULDRON_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != DARK_CHOCOLATE_BEHAVIOR) DARK_CHOCOLATE_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != MILK_CHOCOLATE_BEHAVIOR) MILK_CHOCOLATE_BEHAVIOR.put(bucket, fillCauldron);
		if(cauldronBehavior != WHITE_CHOCOLATE_BEHAVIOR) WHITE_CHOCOLATE_BEHAVIOR.put(bucket, fillCauldron);
	}

	static void registerBottleBehaviors(Map<Item, CauldronBehavior> cauldronBehavior, AbstractLeveledCauldronBlock cauldron, Item bottle) {
		EMPTY_CAULDRON_BEHAVIOR.put(bottle, pourBottleInEmptyCauldron(cauldron));
		cauldronBehavior.put(bottle, pourBottle());
		cauldronBehavior.put(Items.GLASS_BOTTLE, fillBottle(new ItemStack(bottle)));
	}

	static void registerOtherCauldronsBehaviors(Map<Item, CauldronBehavior> cauldronBehavior) {
		cauldronBehavior.put(Items.LAVA_BUCKET, FILL_WITH_LAVA);
		cauldronBehavior.put(Items.WATER_BUCKET, FILL_WITH_WATER);
		cauldronBehavior.put(Items.POWDER_SNOW_BUCKET, FILL_WITH_POWDER_SNOW);
		cauldronBehavior.put(Items.MILK_BUCKET, fillCauldronFromBucket((AbstractLeveledCauldronBlock) CulinaireFoodBundle.MILK_CAULDRON));
	}

	static CauldronBehavior pourBottle() {
		return addToCauldron(new ItemStack(Items.GLASS_BOTTLE), SoundEvents.ITEM_BOTTLE_EMPTY, 1);
	}

	/**
	 * @param cauldron the new cauldron. Must be made out of the bucket's content.
	 */
	static CauldronBehavior fillCauldronFromBucket(AbstractLeveledCauldronBlock cauldron) {
		return replaceCauldron(cauldron.getDefaultState().with(cauldron.getLevelProperty(), Math.max(cauldron.getMaxLevel(), 3)), new ItemStack(Items.BUCKET), Stats.FILL_CAULDRON, SoundEvents.ITEM_BUCKET_EMPTY);
	}

	/**
	 * @param cauldron the new cauldron. Must be made out of the bottle's content.
	 */
	static CauldronBehavior pourBottleInEmptyCauldron(AbstractLeveledCauldronBlock cauldron) {
		return replaceCauldron(cauldron.getDefaultState().with(cauldron.getLevelProperty(), 1), new ItemStack(Items.GLASS_BOTTLE), Stats.USE_CAULDRON, SoundEvents.ITEM_BOTTLE_EMPTY);
	}

	/**
	 * @param bottle the new bottle. Must be made out of the cauldron's content.
	 */
	static CauldronBehavior fillBottle(ItemStack bottle) {
		return getFromCauldron(bottle, SoundEvents.ITEM_BOTTLE_FILL, 1);
	}

	static CauldronBehavior replaceCauldron(BlockState afterState, ItemStack afterStack, Identifier stat, SoundEvent soundEvent) {
		return (state, world, pos, player, hand, stack) -> {
			if(!world.isClient) {
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, afterStack));
				player.incrementStat(stat);
				player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
				world.setBlockState(pos, afterState);
				world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
			}
			return ActionResult.success(world.isClient);
		};
	}

	static CauldronBehavior addToCauldron(ItemStack afterStack, SoundEvent soundEvent, int amount) {
		return (state, world, pos, player, hand, stack) -> {
			if(!((AbstractCauldronBlock)state.getBlock()).isFull(state)) {
				if(!world.isClient) {
					player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, afterStack));
					player.incrementStat(Stats.USE_CAULDRON);
					player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
					AbstractLeveledCauldronBlock.incrementFluidLevel(state, world, pos, amount);
					world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
					world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
				}
				return ActionResult.success(world.isClient);
			}
			else {
				return ActionResult.PASS;
			}
		};
	}

	static CauldronBehavior getFromCauldron(ItemStack afterStack, SoundEvent soundEvent, int amount) {
		return (state, world, pos, player, hand, stack) -> {
			if(!world.isClient) {
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, afterStack));
				player.incrementStat(Stats.USE_CAULDRON);
				player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
				AbstractLeveledCauldronBlock.decrementFluidLevel(state, world, pos, amount);
				world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
			}
			return ActionResult.success(world.isClient);
		};
	}
}
