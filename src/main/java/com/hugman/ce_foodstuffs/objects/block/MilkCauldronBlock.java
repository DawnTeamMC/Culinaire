package com.hugman.ce_foodstuffs.objects.block;

import com.hugman.ce_foodstuffs.init.CEFBlocks;
import com.hugman.ce_foodstuffs.init.CEFItems;
import com.hugman.ce_foodstuffs.objects.block.block_entity.MilkCauldronBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MilkCauldronBlock extends BlockWithEntity {
	public static final IntProperty LEVEL = CEFBlocks.Properties.LEVEL_1_3;
	public static final BooleanProperty COAGULATED = CEFBlocks.Properties.COAGULATED;

	public MilkCauldronBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(COAGULATED, false).with(LEVEL, 3));
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new MilkCauldronBlockEntity();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		boolean isReady = state.get(COAGULATED);
		int i = state.get(LEVEL);
		ItemStack handStack = player.getStackInHand(hand);
		if(isReady) {
			if(!world.isClient) {
				float f = 0.7F;
				double d = (world.random.nextFloat() * f) + 0.15D;
				double e = (world.random.nextFloat() * f) + 0.66D;
				double g = (world.random.nextFloat() * f) + 0.15D;
				ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + g, new ItemStack(CEFItems.CHEESE, 2));
				itemEntity.setToDefaultPickupDelay();
				world.spawnEntity(itemEntity);
				player.incrementStat(Stats.USE_CAULDRON);
				world.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), 2);
			}
			return ActionResult.success(world.isClient);
		}
		else {
			if(handStack.isEmpty()) {
				return ActionResult.PASS;
			}
			else {
				Item item = handStack.getItem();
				if(item == Items.GLASS_BOTTLE) {
					if(i > 0 && !world.isClient) {
						if(!player.abilities.creativeMode) {
							ItemStack milkBottleStack = new ItemStack(CEFItems.MILK_BOTTLE);
							player.incrementStat(Stats.USE_CAULDRON);
							handStack.decrement(1);
							if(handStack.isEmpty()) {
								player.setStackInHand(hand, milkBottleStack);
							}
							else if(!player.inventory.insertStack(milkBottleStack)) {
								player.dropItem(milkBottleStack, false);
							}
							else if(player instanceof ServerPlayerEntity) {
								((ServerPlayerEntity) player).openHandledScreen(player.playerScreenHandler);
							}
						}
						world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
						if(i == 1) {
							world.setBlockState(pos, Blocks.CAULDRON.getDefaultState().with(CauldronBlock.LEVEL, 0), 2);
						}
						else {
							this.setLevel(world, pos, state, i - 1);
						}
					}
					return ActionResult.success(world.isClient);
				}
				else if(item == CEFItems.MILK_BOTTLE) {
					if(i < 3 && !world.isClient) {
						if(!player.abilities.creativeMode) {
							ItemStack stack = new ItemStack(Items.GLASS_BOTTLE);
							player.incrementStat(Stats.USE_CAULDRON);
							player.setStackInHand(hand, stack);
							if(player instanceof ServerPlayerEntity) {
								((ServerPlayerEntity) player).openHandledScreen(player.playerScreenHandler);
							}
						}
						world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
						this.setLevel(world, pos, state, i + 1);
					}
					return ActionResult.success(world.isClient);
				}
				else if(item == Items.BUCKET) {
					if(i == 3 && !world.isClient) {
						if(!player.abilities.creativeMode) {
							handStack.decrement(1);
							if(handStack.isEmpty()) {
								player.setStackInHand(hand, new ItemStack(Items.MILK_BUCKET));
							}
							else if(!player.inventory.insertStack(new ItemStack(Items.MILK_BUCKET))) {
								player.dropItem(new ItemStack(Items.MILK_BUCKET), false);
							}
						}
						player.incrementStat(Stats.USE_CAULDRON);
						world.setBlockState(pos, Blocks.CAULDRON.getDefaultState().with(CauldronBlock.LEVEL, 0), 2);
						world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
					return ActionResult.success(world.isClient);
				}
				else {
					return ActionResult.PASS;
				}
			}
		}
	}

	public void setLevel(World world, BlockPos pos, BlockState state, int level) {
		world.setBlockState(pos, state.with(LEVEL, MathHelper.clamp(level, 1, 3)), 2);
		world.updateComparators(pos, this);
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

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(COAGULATED, LEVEL);
	}

	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(LEVEL);
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
}
