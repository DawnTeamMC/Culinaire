package hugman.ce_foodstuffs.objects.block;

import hugman.ce_foodstuffs.init.CEFBlocks;
import hugman.ce_foodstuffs.init.CEFItems;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class KettleBlock extends HorizontalFacingBlock {
	public static final VoxelShape TOP = Block.createCuboidShape(2.0D, 10.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	public static final VoxelShape BODY = Block.createCuboidShape(3.0D, 1.0D, 3.0D, 13.0D, 10.0D, 13.0D);
	public static final VoxelShape BOTTOM = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 1.0D, 12.0D);

	public static final VoxelShape FAUCET_NORTH = Block.createCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 8.0D, 3.0D);
	public static final VoxelShape FAUCET_EAST = Block.createCuboidShape(13.0D, 6.0D, 6.0D, 16.0D, 8.0D, 10.0D);
	public static final VoxelShape FAUCET_SOUTH = Block.createCuboidShape(6.0D, 6.0D, 13.0D, 10.0D, 8.0D, 16.0D);
	public static final VoxelShape FAUCET_WEST = Block.createCuboidShape(0.0D, 6.0D, 6.0D, 3.0D, 8.0D, 10.0D);

	public static final VoxelShape HANDLE_NORTH = Block.createCuboidShape(7.0D, 2.0D, 13.0D, 9.0D, 8.0D, 16.0D);
	public static final VoxelShape HANDLE_EAST = Block.createCuboidShape(0.0D, 2.0D, 7.0D, 3.0D, 8.0D, 9.0D);
	public static final VoxelShape HANDLE_SOUTH = Block.createCuboidShape(7.0D, 2.0D, 0.0D, 9.0D, 8.0D, 3.0D);
	public static final VoxelShape HANDLE_WEST = Block.createCuboidShape(13.0D, 2.0D, 7.0D, 16.0D, 8.0D, 9.0D);

	public static final VoxelShape MAIN_SHAPE = VoxelShapes.union(TOP, BODY, BOTTOM);
	public static final VoxelShape NORTH_SHAPE = VoxelShapes.union(MAIN_SHAPE, FAUCET_NORTH, HANDLE_NORTH);
	public static final VoxelShape EAST_SHAPE = VoxelShapes.union(MAIN_SHAPE, FAUCET_EAST, HANDLE_EAST);
	public static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(MAIN_SHAPE, FAUCET_SOUTH, HANDLE_SOUTH);
	public static final VoxelShape WEST_SHAPE = VoxelShapes.union(MAIN_SHAPE, FAUCET_WEST, HANDLE_WEST);

	public KettleBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	private VoxelShape getShape(BlockState state) {
		switch(state.get(FACING)) {
			case NORTH:
			default:
				return NORTH_SHAPE;
			case EAST:
				return EAST_SHAPE;
			case SOUTH:
				return SOUTH_SHAPE;
			case WEST:
				return WEST_SHAPE;
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return this.getShape(state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return this.getShape(state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(new Property[]{FACING});
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

}
