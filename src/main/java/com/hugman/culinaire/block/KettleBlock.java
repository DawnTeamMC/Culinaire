package com.hugman.culinaire.block;

import com.hugman.culinaire.block.entity.KettleBlockEntity;
import com.hugman.culinaire.registry.content.TeaContent;
import com.hugman.culinaire.tea.TeaHelper;
import com.hugman.culinaire.tea.TeaType;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
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
import net.minecraft.world.event.GameEvent;

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
        return switch (state.get(FACING)) {
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
        return world.isClient ? null : checkType(type, TeaContent.KETTLE_ENTITY, KettleBlockEntity::serverTick);
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof KettleBlockEntity kettleEntity) {
                kettleEntity.setCustomName(itemStack.getName());
            }
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof KettleBlockEntity) {
                ItemScatterer.spawn(world, pos, (KettleBlockEntity) blockEntity);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient) {
            boolean openScreen = true;
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof KettleBlockEntity kettle) {
                if (!stack.isEmpty()) {
                    if (stack.getItem() == Items.WATER_BUCKET && kettle.getFluid() != KettleBlockEntity.Fluid.TEA) {
                        if (kettle.addWater(3)) {
                            openScreen = false;
                            player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
                            player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                        }
                    } else if (stack.getItem() == Items.POTION && PotionUtil.getPotion(stack) == Potions.WATER && kettle.getFluid() != KettleBlockEntity.Fluid.TEA) {
                        if (kettle.addWater(1)) {
                            openScreen = false;
                            player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                            player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                        }
                    } else if (stack.getItem() == Items.GLASS_BOTTLE && kettle.getFluid() == KettleBlockEntity.Fluid.TEA) {
                        List<TeaType> teaTypes = kettle.getTeaTypes();
                        if (kettle.removeFluid(1)) {
                            openScreen = false;
                            player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, TeaHelper.appendTeaTypes(new ItemStack(TeaContent.TEA_BOTTLE), teaTypes)));
                            player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                            world.playSound(null, pos, TeaContent.TEA_BOTTLE_FILL_SOUND, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
                        }
                    }
                }
                if (openScreen) {
                    player.openHandledScreen((KettleBlockEntity) blockEntity);
                }
            }
            player.incrementStat(TeaContent.KETTLE_INTERACTION_STAT);
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
