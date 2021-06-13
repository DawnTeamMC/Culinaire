package com.hugman.culinaire.objects.block;

import com.hugman.culinaire.init.CulinaireBlocks;
import com.hugman.culinaire.objects.block.block_entity.MilkCauldronBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

public class MilkCauldronBlock extends ThreeLeveledCauldronBlock implements BlockEntityProvider {
	public static final BooleanProperty COAGULATED = CulinaireBlocks.Properties.COAGULATED;

	public MilkCauldronBlock(Settings settings, Map<Item, CauldronBehavior> behaviorMap) {
		super(settings, behaviorMap);
		this.setDefaultState(this.stateManager.getDefaultState().with(COAGULATED, false).with(getLevelProperty(), 1));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(COAGULATED, getLevelProperty());
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new MilkCauldronBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : checkType(type, CulinaireBlocks.MILK_CAULDRON_ENTITY, MilkCauldronBlockEntity::tick);
	}

	@Nullable
	protected static <A extends BlockEntity, E extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
		return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public Item asItem() {
		return Items.CAULDRON;
	}

	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		return createCuboidShape(2.0D, state.get(COAGULATED) ? 6.0D + (state.get(LEVEL) * 3) : 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), getRaycastShape(state, world, pos)), BooleanBiFunction.ONLY_FIRST);
	}
}
