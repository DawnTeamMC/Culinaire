package hugman.ce_foodstuffs.objects.block;

import hugman.ce_foodstuffs.init.data.CEFFoods;
import hugman.ce_foodstuffs.init.data.CEFProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class CheeseWheelBlock extends Block {
	public static final IntProperty BITES = CEFProperties.BITES_4;
	protected static final VoxelShape[] BITES_TO_SHAPE = new VoxelShape[]{Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D), Block.createCuboidShape(4.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D), Block.createCuboidShape(6.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D), Block.createCuboidShape(8.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D), Block.createCuboidShape(10.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D), Block.createCuboidShape(12.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D)};

	public CheeseWheelBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(BITES, 0));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return BITES_TO_SHAPE[state.get(BITES)];
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(world.isClient) {
			ItemStack itemStack = player.getStackInHand(hand);
			if(this.tryEat(world, pos, state, player).isAccepted()) {
				return ActionResult.SUCCESS;
			}
			if(itemStack.isEmpty()) {
				return ActionResult.CONSUME;
			}
		}
		return this.tryEat(world, pos, state, player);
	}

	private ActionResult tryEat(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player) {
		if(!player.canConsume(false)) {
			return ActionResult.PASS;
		}
		else {
			player.incrementStat(Stats.EAT_CAKE_SLICE);
			player.getHungerManager().add(CEFFoods.CHEESE.getHunger(), CEFFoods.CHEESE.getSaturationModifier());
			int i = state.get(BITES);
			if(i < 5) {
				world.setBlockState(pos, state.with(BITES, i + 1), 3);
			}
			else {
				world.removeBlock(pos, false);
			}
			return ActionResult.SUCCESS;
		}
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		return direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).getMaterial().isSolid();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(BITES);
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return (6 - state.get(BITES)) * 2;
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
}
