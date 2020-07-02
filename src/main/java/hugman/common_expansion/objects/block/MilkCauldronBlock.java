package hugman.common_expansion.objects.block;

import hugman.common_expansion.init.CEItems;
import hugman.common_expansion.init.data.CEProperties;
import hugman.common_expansion.objects.block.block_entity.MilkCauldronBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MilkCauldronBlock extends BlockWithEntity {
	public static final BooleanProperty COAGULATED = CEProperties.COAGULATED;
	private static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE), BooleanBiFunction.ONLY_FIRST);

	public MilkCauldronBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(COAGULATED, false));
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new MilkCauldronBlockEntity();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		boolean isReady = state.get(COAGULATED);
		ItemStack itemStack = player.getStackInHand(hand);
		if(isReady) {
			if(!world.isClient) {
				float f = 0.7F;
				double d = (world.random.nextFloat() * f) + 0.15D;
				double e = (world.random.nextFloat() * f) + 0.66D;
				double g = (world.random.nextFloat() * f) + 0.15D;
				ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + g, new ItemStack(CEItems.CHEESE));
				itemEntity.setToDefaultPickupDelay();
				world.spawnEntity(itemEntity);
				player.incrementStat(Stats.USE_CAULDRON);
				world.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), 2);
			}
			return ActionResult.success(world.isClient);
		}
		else {
			if(itemStack.isEmpty()) {
				return ActionResult.PASS;
			}
			else {
				Item item = itemStack.getItem();
				if(item == Items.BUCKET) {
					if(!world.isClient) {
						if(!player.abilities.creativeMode) {
							itemStack.decrement(1);
							if(itemStack.isEmpty()) {
								player.setStackInHand(hand, new ItemStack(Items.MILK_BUCKET));
							}
							else if(!player.inventory.insertStack(new ItemStack(Items.MILK_BUCKET))) {
								player.dropItem(new ItemStack(Items.MILK_BUCKET), false);
							}
						}
						player.incrementStat(Stats.USE_CAULDRON);
						world.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), 2);
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

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public Item asItem() {
		return Items.CAULDRON;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return OUTLINE_SHAPE;
	}

	@Override
	public VoxelShape getRayTraceShape(BlockState state, BlockView world, BlockPos pos) {
		return RAY_TRACE_SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(COAGULATED);
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
}
