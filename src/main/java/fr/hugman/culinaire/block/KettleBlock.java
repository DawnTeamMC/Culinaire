package fr.hugman.culinaire.block;

import com.mojang.serialization.MapCodec;
import fr.hugman.culinaire.block.entity.CulinaireBlockEntityTypes;
import fr.hugman.culinaire.block.entity.KettleBlockEntity;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.item.CulinaireItems;
import fr.hugman.culinaire.sound.CulinaireSoundEvents;
import fr.hugman.culinaire.stat.CulinaireStats;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Optional;

public class KettleBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final VoxelShape TOP = Block.box(2.0D, 10.0D, 2.0D, 14.0D, 12.0D, 14.0D);
    public static final VoxelShape BODY = Block.box(3.0D, 1.0D, 3.0D, 13.0D, 10.0D, 13.0D);
    public static final VoxelShape BOTTOM = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 1.0D, 12.0D);

    public static final VoxelShape FAUCET_NORTH = Block.box(6.0D, 6.0D, 0.0D, 10.0D, 8.0D, 3.0D);
    public static final VoxelShape FAUCET_EAST = Block.box(13.0D, 6.0D, 6.0D, 16.0D, 8.0D, 10.0D);
    public static final VoxelShape FAUCET_SOUTH = Block.box(6.0D, 6.0D, 13.0D, 10.0D, 8.0D, 16.0D);
    public static final VoxelShape FAUCET_WEST = Block.box(0.0D, 6.0D, 6.0D, 3.0D, 8.0D, 10.0D);

    public static final VoxelShape HANDLE_NORTH = Block.box(7.0D, 2.0D, 13.0D, 9.0D, 8.0D, 16.0D);
    public static final VoxelShape HANDLE_EAST = Block.box(0.0D, 2.0D, 7.0D, 3.0D, 8.0D, 9.0D);
    public static final VoxelShape HANDLE_SOUTH = Block.box(7.0D, 2.0D, 0.0D, 9.0D, 8.0D, 3.0D);
    public static final VoxelShape HANDLE_WEST = Block.box(13.0D, 2.0D, 7.0D, 16.0D, 8.0D, 9.0D);

    public static final VoxelShape MAIN_SHAPE = Shapes.or(TOP, BODY, BOTTOM);
    public static final VoxelShape NORTH_SHAPE = Shapes.or(MAIN_SHAPE, FAUCET_NORTH, HANDLE_NORTH);
    public static final VoxelShape EAST_SHAPE = Shapes.or(MAIN_SHAPE, FAUCET_EAST, HANDLE_EAST);
    public static final VoxelShape SOUTH_SHAPE = Shapes.or(MAIN_SHAPE, FAUCET_SOUTH, HANDLE_SOUTH);
    public static final VoxelShape WEST_SHAPE = Shapes.or(MAIN_SHAPE, FAUCET_WEST, HANDLE_WEST);

    public KettleBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null; //TODO
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return this.getShape(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return this.getShape(state);
    }

    private VoxelShape getShape(BlockState state) {
        return switch (state.getValue(FACING)) {
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KettleBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return world.isClientSide() ? null : createTickerHelper(type, CulinaireBlockEntityTypes.KETTLE, KettleBlockEntity::serverTick);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        Containers.updateNeighboursAfterDestroy(state, world, pos);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world instanceof ServerLevel) {
            MenuProvider namedScreenHandlerFactory = this.getMenuProvider(state, world, pos);
            if (namedScreenHandlerFactory != null) {
                player.openMenu(namedScreenHandlerFactory);
                player.awardStat(this.getOpenStat());
            }
        }

        return InteractionResult.SUCCESS;
    }

    protected Stat<Identifier> getOpenStat() {
        return Stats.CUSTOM.get(CulinaireStats.INTERACT_WITH_KETTLE);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof KettleBlockEntity kettle && !stack.isEmpty()) {
            PotionContents potionContentsComponent = stack.get(DataComponents.POTION_CONTENTS);
            if (stack.getItem() == Items.WATER_BUCKET && kettle.getFluid() != KettleBlockEntity.Fluid.TEA) {
                if (kettle.addWater(3)) {
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                    return InteractionResult.SUCCESS;
                }
            } else if (stack.getItem() == Items.POTION && potionContentsComponent != null && potionContentsComponent.is(Potions.WATER) && kettle.getFluid() != KettleBlockEntity.Fluid.TEA) {
                if (kettle.addWater(1)) {
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                    return InteractionResult.SUCCESS;
                }
            } else if (stack.getItem() == Items.GLASS_BOTTLE && kettle.getFluid() == KettleBlockEntity.Fluid.TEA) {
                var teaTypes = kettle.getTeaTypes();
                if (kettle.removeFluid(1)) {
                    var newStack = new ItemStack(CulinaireItems.TEA_BOTTLE);

                    var effectList = new ArrayList<MobEffectInstance>();

                    for (var entry : teaTypes.getTeaTypeEntries()) {
                        var effect = entry.getKey().value().effect();
                        effectList.add(new MobEffectInstance(
                                effect.getEffect(),
                                effect.mapDuration(i -> i * entry.getIntValue()),
                                effect.getAmplifier(),
                                effect.isAmbient(),
                                effect.isVisible()
                        ));
                    }
                    newStack.set(CulinaireComponentTypes.TEA_TYPES, teaTypes);

                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, newStack));
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    world.playSound(null, pos, CulinaireSoundEvents.TEA_BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
