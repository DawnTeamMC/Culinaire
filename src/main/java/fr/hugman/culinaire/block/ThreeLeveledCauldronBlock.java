package fr.hugman.culinaire.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ThreeLeveledCauldronBlock extends AbstractLeveledCauldronBlock {
    public static final IntProperty LEVEL = Properties.LEVEL_3;

    public ThreeLeveledCauldronBlock(Settings settings, CauldronBehavior.CauldronBehaviorMap behaviorMap) {
        super(settings, behaviorMap, 3);
    }

    @Override
    protected MapCodec<? extends AbstractCauldronBlock> getCodec() {
        return null; //TODO
    }

    @Override
    public IntProperty getLevelProperty() {
        return LEVEL;
    }

    @Override
    protected double getFluidHeight(BlockState state) {
        return (6.0D + (double) state.get(LEVEL) * 3.0D) / 16.0D;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos, Direction direction) {
        return state.get(LEVEL);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }
}