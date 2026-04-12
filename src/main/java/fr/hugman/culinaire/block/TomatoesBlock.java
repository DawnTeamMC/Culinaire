package fr.hugman.culinaire.block;

import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TomatoesBlock extends CropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    private static final VoxelShape LOWER_SHAPE_0 = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D);
    private static final VoxelShape LOWER_SHAPE_1 = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);
    private static final VoxelShape UPPER_SHAPE_2 = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
    private static final VoxelShape UPPER_SHAPE_3 = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);

    public TomatoesBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), 0).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextInt(3) != 0 && state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            if (world.getRawBrightness(pos, 0) >= 9) {
                if (!this.isMaxAge(state)) {
                    float f = getGrowthSpeed(this, world, pos);
                    if (random.nextInt((int) (25.0F / f) + 1) == 0) {
                        int age = this.getAge(state) + 1;
                        if (age <= 1) {
                            world.setBlock(pos, this.getStateForAge(age).setValue(HALF, DoubleBlockHalf.LOWER), 2);
                        }
                        if (age == 2) {
                            if (world.getBlockState(pos.above()).isAir()) {
                                world.setBlock(pos, this.getStateForAge(age).setValue(HALF, DoubleBlockHalf.LOWER), 2);
                                world.setBlock(pos.above(), this.getStateForAge(age).setValue(HALF, DoubleBlockHalf.UPPER), 2);
                            }
                        }
                        if (age >= 3) {
                            if (world.getBlockState(pos.above()).is(this)) {
                                world.setBlock(pos, this.getStateForAge(age).setValue(HALF, DoubleBlockHalf.LOWER), 2);
                                world.setBlock(pos.above(), this.getStateForAge(age).setValue(HALF, DoubleBlockHalf.UPPER), 2);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        DoubleBlockHalf doubleBlockHalf = state.getValue(HALF);
        BlockPos otherHalfPos = doubleBlockHalf == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
        int age = this.getAge(state) + 1;
        if (age == 2) {
            if (!world.getBlockState(otherHalfPos).isAir()) {
                return false;
            }
        }
        if (age >= 3) {
            if (!world.getBlockState(otherHalfPos).is(this)) {
                return false;
            }
        }
        return !this.isMaxAge(state);
    }

    @Override
    public void growCrops(Level world, BlockPos pos, BlockState state) {
        DoubleBlockHalf doubleBlockHalf = state.getValue(HALF);
        int age = this.getAge(state) + this.getBonemealAgeIncrease(world);
        int maxAge = this.getMaxAge();
        if (age > maxAge) {
            age = maxAge;
        }
        world.setBlock(pos, this.getStateForAge(age).setValue(HALF, doubleBlockHalf), 2);
        if (age >= 2) {
            world.setBlock(doubleBlockHalf == DoubleBlockHalf.LOWER ? pos.above() : pos.below(), this.getStateForAge(age).setValue(HALF, doubleBlockHalf == DoubleBlockHalf.LOWER ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER), 2);
        }
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected ItemLike getBaseSeedId() {
        return CulinaireItems.TOMATO;
    }

    @Override
    protected int getBonemealAgeIncrease(Level world) {
        return super.getBonemealAgeIncrease(world) / 3;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, HALF);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        DoubleBlockHalf doubleBlockHalf = state.getValue(HALF);
        int age = this.getAge(state);
        if (doubleBlockHalf == DoubleBlockHalf.LOWER) {
            if (age == 0) {
                return LOWER_SHAPE_0;
            }
            if (age == 1) {
                return LOWER_SHAPE_1;
            }
        }
        if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
            if (age == 2) {
                return UPPER_SHAPE_2;
            }
            if (age == 3) {
                return UPPER_SHAPE_3;
            }
        }
        return Shapes.block();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (state.getBlock() instanceof TomatoesBlock) {
            DoubleBlockHalf doubleBlockHalf = state.getValue(HALF);
            BlockPos otherHalfPos = doubleBlockHalf == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
            int age = this.getAge(state);
            if (age >= 2) {
                if (world.getBlockState(otherHalfPos).is(this)) {
                    if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
        return super.canSurvive(state, world, pos);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        DoubleBlockHalf doubleBlockHalf = state.getValue(HALF);
        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP) && (!neighborState.is(this) || neighborState.getValue(HALF) == doubleBlockHalf)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
        }
    }
}
