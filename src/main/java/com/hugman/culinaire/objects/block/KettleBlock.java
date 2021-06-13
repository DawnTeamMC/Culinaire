package com.hugman.culinaire.objects.block;

import com.hugman.culinaire.init.CulinaireBlocks;
import com.hugman.culinaire.init.CulinaireItems;
import com.hugman.culinaire.objects.block.block_entity.KettleBlockEntity;
import com.hugman.culinaire.objects.block.block_entity.MilkCauldronBlockEntity;
import com.hugman.culinaire.objects.item.tea.TeaHelper;
import com.hugman.culinaire.objects.item.tea.TeaType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
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
		return switch(state.get(FACING)) {
			default -> NORTH_SHAPE;
			case EAST -> EAST_SHAPE;
			case SOUTH -> SOUTH_SHAPE;
			case WEST -> WEST_SHAPE;
		};
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new KettleBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : checkType(type, CulinaireBlocks.KETTLE_ENTITY, KettleBlockEntity::tick);
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
					if(handStack.getItem() == Items.WATER_BUCKET && kettleEntity.getFluid() != KettleBlockEntity.Fluid.TEA) {
						if(kettleEntity.addWater(3)) {
							shouldOpenScreen = false;
							if(!player.getAbilities().creativeMode) {
								ItemStack newStack = new ItemStack(Items.BUCKET);
								player.setStackInHand(hand, newStack);
							}
							world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
					}
					else if(handStack.getItem() == Items.POTION && PotionUtil.getPotion(handStack) == Potions.WATER && kettleEntity.getFluid() != KettleBlockEntity.Fluid.TEA) {
						if(kettleEntity.addWater(1)) {
							shouldOpenScreen = false;
							if(!player.getAbilities().creativeMode) {
								ItemStack newStack = new ItemStack(Items.GLASS_BOTTLE);
								player.setStackInHand(hand, newStack);
							}
							world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
					}
					else if(handStack.getItem() == Items.GLASS_BOTTLE && kettleEntity.getFluid() == KettleBlockEntity.Fluid.TEA) {
						List<TeaType> teaTypes = kettleEntity.getTeaTypes();
						if(kettleEntity.removeFluid(1)) {
							shouldOpenScreen = false;
							ItemStack newStack = TeaHelper.appendTeaTypes(new ItemStack(CulinaireItems.TEA_BOTTLE), teaTypes);
							handStack.decrement(1);
							if(handStack.isEmpty()) {
								player.setStackInHand(hand, newStack);
							}
							else if(!player.getInventory().insertStack(newStack)) {
								player.dropItem(newStack, false);
							}
							world.playSound(null, pos, CulinaireItems.TEA_BOTTLE_FILL_SOUND, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
					}
				}
				if(shouldOpenScreen) {
					player.openHandledScreen((KettleBlockEntity) blockEntity);
					player.incrementStat(CulinaireBlocks.KETTLE_INTERACTION_STAT);
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
