package fr.hugman.culinaire.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MilkCauldronBlock extends ThreeLeveledCauldronBlock {
    public MilkCauldronBlock(Properties settings, CauldronInteraction.InteractionMap behaviorMap) {
        super(settings, behaviorMap);
    }

    @Override
    public Item asItem() {
        return Items.CAULDRON;
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl) {
        if (world instanceof ServerLevel serverWorld) {
            if (entity.mayInteract(serverWorld, pos) && entity instanceof LivingEntity living && living .removeAllEffects()) {
                world.setBlockAndUpdate(pos, changeLevel(state, -1));
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return super.isRandomlyTicking(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        // Formula: 1/(x/(68.27/60))
        // x = 30 (days)
        if (random.nextFloat() < 0.0379278F) {
            world.setBlock(pos, CulinaireBlocks.CHEESE_CAULDRON.defaultBlockState().setValue(CheeseCauldronBlock.LEVEL, getLevel(state)), Block.UPDATE_CLIENTS);
        }
    }
}
