package fr.hugman.culinaire.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.CollisionEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class MilkCauldronBlock extends ThreeLeveledCauldronBlock {
    public MilkCauldronBlock(Settings settings, CauldronBehavior.CauldronBehaviorMap behaviorMap) {
        super(settings, behaviorMap);
    }

    @Override
    public Item asItem() {
        return Items.CAULDRON;
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, boolean bl) {
        if (world instanceof ServerWorld serverWorld) {
            if (entity.canModifyAt(serverWorld, pos) && entity instanceof LivingEntity living && living .clearStatusEffects()) {
                world.setBlockState(pos, changeLevel(state, -1));
            }
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return super.hasRandomTicks(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // Formula: 1/(x/(68.27/60))
        // x = 30 (days)
        if (random.nextFloat() < 0.0379278F) {
            world.setBlockState(pos, CulinaireBlocks.CHEESE_CAULDRON.getDefaultState().with(CheeseCauldronBlock.LEVEL, getLevel(state)), Block.NOTIFY_LISTENERS);
        }
    }
}
