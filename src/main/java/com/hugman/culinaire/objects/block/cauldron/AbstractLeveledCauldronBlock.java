package com.hugman.culinaire.objects.block.cauldron;

import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public abstract class AbstractLeveledCauldronBlock extends AbstractCauldronBlock {
	public AbstractLeveledCauldronBlock(Settings settings, Map<Item, CauldronBehavior> behaviorMap) {
		super(settings, behaviorMap);
		this.setDefaultState(this.stateManager.getDefaultState().with(getLevelProperty(), 1));
	}

	public static int getFluidLevel(BlockState state) {
		return state.get(((AbstractLeveledCauldronBlock) state.getBlock()).getLevelProperty());
	}

	public static void setFluidLevel(BlockState state, World world, BlockPos pos, int amount) {
		IntProperty level = ((AbstractLeveledCauldronBlock) state.getBlock()).getLevelProperty();
		int max = ((AbstractLeveledCauldronBlock) state.getBlock()).getMaxLevel();
		int i = Math.min(amount, max);
		world.setBlockState(pos, i <= 0 ? Blocks.CAULDRON.getDefaultState() : state.with(level, i));
	}

	public static void decrementFluidLevel(BlockState state, World world, BlockPos pos, int amount) {
		setFluidLevel(state, world, pos, state.get(((AbstractLeveledCauldronBlock) state.getBlock()).getLevelProperty()) - amount);
	}

	public static void decrementFluidLevel(BlockState state, World world, BlockPos pos) {
		decrementFluidLevel(state, world, pos, 1);
	}

	public static void incrementFluidLevel(BlockState state, World world, BlockPos pos, int amount) {
		setFluidLevel(state, world, pos, state.get(((AbstractLeveledCauldronBlock) state.getBlock()).getLevelProperty()) + amount);
	}

	public static void incrementFluidLevel(BlockState state, World world, BlockPos pos) {
		incrementFluidLevel(state, world, pos, 1);
	}

	public abstract IntProperty getLevelProperty();

	public abstract int getMaxLevel();

	@Override
	public boolean isFull(BlockState state) {
		return state.get(getLevelProperty()) == getMaxLevel();
	}
}
