package com.hugman.culinaire.objects.block;

import com.hugman.culinaire.init.CulinaireFoodBundle;
import com.hugman.culinaire.objects.block_entity.MilkCauldronBlockEntity;
import com.hugman.culinaire.objects.block.cauldron.ThreeLeveledCauldronBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

public class MilkCauldronBlock extends ThreeLeveledCauldronBlock implements BlockEntityProvider {
	public MilkCauldronBlock(Settings settings, Map<Item, CauldronBehavior> behaviorMap) {
		super(settings, behaviorMap);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new MilkCauldronBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : checkType(type, CulinaireFoodBundle.MILK_CAULDRON_ENTITY, MilkCauldronBlockEntity::tick);
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
}
