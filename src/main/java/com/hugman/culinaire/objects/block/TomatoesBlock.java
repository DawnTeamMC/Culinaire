package com.hugman.culinaire.objects.block;

import com.hugman.culinaire.init.FoodBundle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class TomatoesBlock extends CropBlock {
	public static final IntProperty AGE = Properties.AGE_3;
	public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
	private static final VoxelShape LOWER_SHAPE_0 = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D);
	private static final VoxelShape LOWER_SHAPE_1 = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);
	private static final VoxelShape UPPER_SHAPE_2 = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	private static final VoxelShape UPPER_SHAPE_3 = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);

	public TomatoesBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(this.getAgeProperty(), 0).with(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if(random.nextInt(3) != 0 && state.get(HALF) == DoubleBlockHalf.LOWER) {
			if(world.getBaseLightLevel(pos, 0) >= 9) {
				if(!this.isMature(state)) {
					float f = getAvailableMoisture(this, world, pos);
					if(random.nextInt((int) (25.0F / f) + 1) == 0) {
						int age = this.getAge(state) + 1;
						if(age <= 1) {
							world.setBlockState(pos, this.withAge(age).with(HALF, DoubleBlockHalf.LOWER), 2);
						}
						if(age == 2) {
							if(world.getBlockState(pos.up()).isAir()) {
								world.setBlockState(pos, this.withAge(age).with(HALF, DoubleBlockHalf.LOWER), 2);
								world.setBlockState(pos.up(), this.withAge(age).with(HALF, DoubleBlockHalf.UPPER), 2);
							}
						}
						if(age >= 3) {
							if(world.getBlockState(pos.up()).isOf(this)) {
								world.setBlockState(pos, this.withAge(age).with(HALF, DoubleBlockHalf.LOWER), 2);
								world.setBlockState(pos.up(), this.withAge(age).with(HALF, DoubleBlockHalf.UPPER), 2);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		DoubleBlockHalf doubleBlockHalf = state.get(HALF);
		BlockPos otherHalfPos = doubleBlockHalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
		int age = this.getAge(state) + 1;
		if(age == 2) {
			if(!world.getBlockState(otherHalfPos).isAir()) {
				return false;
			}
		}
		if(age >= 3) {
			if(!world.getBlockState(otherHalfPos).isOf(this)) {
				return false;
			}
		}
		return !this.isMature(state);
	}

	@Override
	public void applyGrowth(World world, BlockPos pos, BlockState state) {
		DoubleBlockHalf doubleBlockHalf = state.get(HALF);
		int age = this.getAge(state) + this.getGrowthAmount(world);
		int maxAge = this.getMaxAge();
		if(age > maxAge) {
			age = maxAge;
		}
		world.setBlockState(pos, this.withAge(age).with(HALF, doubleBlockHalf), 2);
		if(age >= 2) {
			world.setBlockState(doubleBlockHalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down(), this.withAge(age).with(HALF, doubleBlockHalf == DoubleBlockHalf.LOWER ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER), 2);
		}
	}

	@Override
	public IntProperty getAgeProperty() {
		return AGE;
	}

	@Override
	public int getMaxAge() {
		return 3;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected ItemConvertible getSeedsItem() {
		return FoodBundle.TOMATO;
	}

	@Override
	protected int getGrowthAmount(World world) {
		return super.getGrowthAmount(world) / 3;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE, HALF);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		DoubleBlockHalf doubleBlockHalf = state.get(HALF);
		int age = this.getAge(state);
		if(doubleBlockHalf == DoubleBlockHalf.LOWER) {
			if(age == 0) {
				return LOWER_SHAPE_0;
			}
			if(age == 1) {
				return LOWER_SHAPE_1;
			}
		}
		if(doubleBlockHalf == DoubleBlockHalf.UPPER) {
			if(age == 2) {
				return UPPER_SHAPE_2;
			}
			if(age == 3) {
				return UPPER_SHAPE_3;
			}
		}
		return VoxelShapes.fullCube();
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if(state.getBlock() instanceof TomatoesBlock) {
			DoubleBlockHalf doubleBlockHalf = state.get(HALF);
			BlockPos otherHalfPos = doubleBlockHalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
			int age = this.getAge(state);
			if(age >= 2) {
				if(world.getBlockState(otherHalfPos).isOf(this)) {
					if(doubleBlockHalf == DoubleBlockHalf.UPPER) {
						return super.canPlaceAt(state, world, pos);
					}
				}
				else {
					return false;
				}
			}
		}
		return super.canPlaceAt(state, world, pos);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		DoubleBlockHalf doubleBlockHalf = state.get(HALF);
		if(direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP) && (!newState.isOf(this) || newState.get(HALF) == doubleBlockHalf)) {
			return Blocks.AIR.getDefaultState();
		}
		else {
			return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
		}
	}
}
