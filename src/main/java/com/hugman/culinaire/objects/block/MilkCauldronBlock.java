package com.hugman.culinaire.objects.block;

import com.hugman.culinaire.init.FoodBundle;
import com.hugman.dawn.api.object.block.ThreeLeveledCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Random;

public class MilkCauldronBlock extends ThreeLeveledCauldronBlock {
	public MilkCauldronBlock(Settings settings, Map<Item, CauldronBehavior> behaviorMap) {
		super(settings, behaviorMap);
	}

	@Override
	public Item asItem() {
		return Items.CAULDRON;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if(!world.isClient && entity instanceof LivingEntity livingEntity && this.isEntityTouchingFluid(state, pos, entity)) {
			if(entity.canModifyAt(world, pos) && livingEntity.clearStatusEffects()) {
				world.setBlockState(pos, changeLevel(state, -1));
			}
		}
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return super.hasRandomTicks(state);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		// Formula: 1/(x/(68.27/60))
		// x = 30 (days)
		if(random.nextFloat() < 0.0379278F) {
			world.setBlockState(pos, FoodBundle.CHEESE_CAULDRON.getDefaultState().with(CheeseCauldronBlock.LEVEL, getLevel(state)), Block.NOTIFY_LISTENERS);
		}
	}
}
