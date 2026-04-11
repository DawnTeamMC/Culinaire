package fr.hugman.culinaire.block;

import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.CollisionEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class AbstractLeveledCauldronBlock extends AbstractCauldronBlock {
    private final int maxLevel;
    private final IntProperty levelProperty;
    private final VoxelShape[] insideCollisionShapeByLevel;

    public AbstractLeveledCauldronBlock(
            Settings settings,
            CauldronBehavior.CauldronBehaviorMap behaviorMap,
            int maxLevel
    ) {
        super(settings, behaviorMap);
        this.maxLevel = maxLevel;
        this.levelProperty = maxLevel == 3 ? Properties.LEVEL_3 : IntProperty.of("level", 1, maxLevel);
        this.setDefaultState(this.stateManager.getDefaultState().with(this.levelProperty, 1));
        this.insideCollisionShapeByLevel = Util.make(() -> Block.createShapeArray(
                maxLevel - 1, level -> VoxelShapes.union(AbstractCauldronBlock.OUTLINE_SHAPE, Block.createColumnShape(12.0, 4.0, getFluidHeight(level + 1)))
        ));
    }

    public int getLevel(BlockState state) {
        return state.get(this.getLevelProperty());
    }

    public BlockState defaultWithLevel(int amount) {
        return setLevel(getDefaultState(), amount);
    }

    public BlockState setLevel(BlockState state, int amount) {
        int i = Math.min(amount, this.maxLevel);
        return i <= 0 ? Blocks.CAULDRON.getDefaultState() : state.with(this.getLevelProperty(), i);
    }

    public BlockState changeLevel(BlockState state, int amount) {
        return setLevel(state, getLevel(state) + amount);
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public IntProperty getLevelProperty() {
        return this.levelProperty;
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.get(this.getLevelProperty()) == this.getMaxLevel();
    }

    @Override
    protected double getFluidHeight(BlockState state) {
        return getFluidHeight(state.get(this.getLevelProperty())) / 16.0;
    }

    private static double getFluidHeight(int level) {
        //TODO: this is incorrect
        return 6.0 + level * 3.0;
    }

    @Override
    protected VoxelShape getInsideCollisionShape(BlockState state, BlockView world, BlockPos pos, Entity entity) {
        return insideCollisionShapeByLevel[state.get(this.getLevelProperty()) - 1];
    }
}