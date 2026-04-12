package fr.hugman.culinaire.block;

import fr.hugman.culinaire.item.CulinaireItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CheeseCauldronBlock extends ThreeLeveledCauldronBlock {
    public CheeseCauldronBlock(Properties settings) {
        super(settings, CauldronInteraction.newInteractionMap("culinaire:cheese"));
    }

    @Override
    public Item asItem() {
        return Items.CAULDRON;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        InteractionResult result = super.useWithoutItem(state, world, pos, player, hit);
        if (result.consumesAction()) {
            return result;
        } else if (!world.isClientSide()) {
            int level = state.getValue(this.getLevelProperty());
            player.awardStat(Stats.USE_CAULDRON);
            float f = 0.7F;
            double x = (world.random.nextFloat() * f) + 0.15D;
            double y = (world.random.nextFloat() * f) + 0.66D;
            double z = (world.random.nextFloat() * f) + 0.15D;
            ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + x, (double) pos.getY() + y, (double) pos.getZ() + z, new ItemStack(CulinaireItems.CHEESE));
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity(itemEntity);
            if (level > 1) {
                world.setBlockAndUpdate(pos, changeLevel(state, -1));
            } else {
                world.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return box(2.0D, getContentHeight(state) * 16.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), getInteractionShape(state, world, pos)), BooleanOp.ONLY_FIRST);
    }
}
