package com.hugman.culinaire.objects.block;

import com.hugman.culinaire.init.FoodBundle;
import com.hugman.dawn.api.object.block.ThreeLeveledCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CheeseCauldronBlock extends ThreeLeveledCauldronBlock {
	public CheeseCauldronBlock(Settings settings) {
		super(settings, CauldronBehavior.createMap());
	}

	@Override
	public Item asItem() {
		return Items.CAULDRON;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ActionResult result = super.onUse(state, world, pos, player, hand, hit);
		if(result.isAccepted()) {
			return result;
		}
		else if(!world.isClient) {
			int level = state.get(this.getLevelProperty());
			player.incrementStat(Stats.USE_CAULDRON);
			player.incrementStat(Stats.USED.getOrCreateStat(player.getStackInHand(hand).getItem()));
			float f = 0.7F;
			double x = (world.random.nextFloat() * f) + 0.15D;
			double y = (world.random.nextFloat() * f) + 0.66D;
			double z = (world.random.nextFloat() * f) + 0.15D;
			ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + x, (double) pos.getY() + y, (double) pos.getZ() + z, new ItemStack(FoodBundle.CHEESE));
			itemEntity.setToDefaultPickupDelay();
			world.spawnEntity(itemEntity);
			if(level > 1) {
				world.setBlockState(pos, changeLevel(state, -1));
			}
			else {
				world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
			}
		}
		return ActionResult.success(world.isClient);
	}

	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		return createCuboidShape(2.0D, getFluidHeight(state) * 16.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), getRaycastShape(state, world, pos)), BooleanBiFunction.ONLY_FIRST);
	}
}
