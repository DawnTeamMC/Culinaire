package hugman.ce_foodstuffs.objects.block;

import hugman.ce_foodstuffs.init.CEFItems;
import hugman.ce_foodstuffs.init.CEFSounds;
import hugman.ce_foodstuffs.init.data.CEFStats;
import hugman.ce_foodstuffs.objects.block.block_entity.KettleBlockEntity;
import hugman.ce_foodstuffs.objects.item.tea.TeaHelper;
import hugman.ce_foodstuffs.objects.item.tea.TeaType;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class KettleBlock extends BlockWithEntity {
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

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
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return this.getShape(state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return this.getShape(state);
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

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new KettleBlockEntity();
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if(itemStack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof KettleBlockEntity) {
				((KettleBlockEntity) blockEntity).setCustomName(itemStack.getName());
			}
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if(!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof KettleBlockEntity) {
				ItemScatterer.spawn(world, pos, (KettleBlockEntity) blockEntity);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack handStack = player.getStackInHand(hand);
		if(!world.isClient) {
			boolean shouldOpenScreen = true;
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof KettleBlockEntity) {
				if(!handStack.isEmpty()) {
					KettleBlockEntity kettleEntity = (KettleBlockEntity) blockEntity;
					if(handStack.getItem() == Items.WATER_BUCKET && kettleEntity.getFluid() != 2) {
						if(kettleEntity.addWater(3)) {
							shouldOpenScreen = false;
							if(!player.abilities.creativeMode) {
								ItemStack newStack = new ItemStack(Items.GLASS_BOTTLE);
								player.setStackInHand(hand, newStack);
							}
							world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
					}
					else if(handStack.getItem() == Items.POTION && PotionUtil.getPotion(handStack) == Potions.WATER && kettleEntity.getFluid() != 2) {
						if(kettleEntity.addWater(1)) {
							shouldOpenScreen = false;
							if(!player.abilities.creativeMode) {
								ItemStack newStack = new ItemStack(Items.GLASS_BOTTLE);
								player.setStackInHand(hand, newStack);
							}
							world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
					}
					else if(handStack.getItem() == Items.GLASS_BOTTLE && kettleEntity.getFluid() == 2) {
						List<TeaType> teaTypes = kettleEntity.getTeaTypes();
						if(kettleEntity.removeFluid(1)) {
							shouldOpenScreen = false;
							ItemStack newStack = TeaHelper.appendTeaTypes(new ItemStack(CEFItems.TEA_BOTTLE), teaTypes);
							handStack.decrement(1);
							if(handStack.isEmpty()) {
								player.setStackInHand(hand, newStack);
							}
							else if(!player.inventory.insertStack(newStack)) {
								player.dropItem(newStack, false);
							}
							else if(player instanceof ServerPlayerEntity) {
								((ServerPlayerEntity) player).openHandledScreen(player.playerScreenHandler);
							}
							world.playSound(null, pos, CEFSounds.ITEM_TEA_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
					}
				}
				if(shouldOpenScreen) {
					player.openHandledScreen((KettleBlockEntity) blockEntity);
					player.incrementStat(CEFStats.INTERACT_WITH_KETTLE);
				}
			}
		}
		return ActionResult.success(world.isClient);
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
}
