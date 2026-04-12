package fr.hugman.culinaire.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractLeveledCauldronBlock extends AbstractCauldronBlock {
    private final int maxLevel;
    private final IntegerProperty levelProperty;
    private final VoxelShape[] insideCollisionShapeByLevel;

    public AbstractLeveledCauldronBlock(
            Properties settings,
            CauldronInteraction.Dispatcher interactions,
            int maxLevel
    ) {
        super(settings, interactions);
        this.maxLevel = maxLevel;
        this.levelProperty = maxLevel == 3 ? BlockStateProperties.LEVEL_CAULDRON : IntegerProperty.create("level", 1, maxLevel);
        this.registerDefaultState(this.stateDefinition.any().setValue(this.levelProperty, 1));
        this.insideCollisionShapeByLevel = Util.make(() -> Block.boxes(
                maxLevel - 1, level -> Shapes.or(AbstractCauldronBlock.SHAPE, Block.column(12.0, 4.0, getFluidHeight(level + 1)))
        ));
    }

    public int getLevel(BlockState state) {
        return state.getValue(this.getLevelProperty());
    }

    public BlockState defaultWithLevel(int amount) {
        return setLevel(defaultBlockState(), amount);
    }

    public BlockState setLevel(BlockState state, int amount) {
        int i = Math.min(amount, this.maxLevel);
        return i <= 0 ? Blocks.CAULDRON.defaultBlockState() : state.setValue(this.getLevelProperty(), i);
    }

    public BlockState changeLevel(BlockState state, int amount) {
        return setLevel(state, getLevel(state) + amount);
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public IntegerProperty getLevelProperty() {
        return this.levelProperty;
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.getValue(this.getLevelProperty()) == this.getMaxLevel();
    }

    @Override
    protected double getContentHeight(BlockState state) {
        return getFluidHeight(state.getValue(this.getLevelProperty())) / 16.0;
    }

    private static double getFluidHeight(int level) {
        //TODO: this is incorrect
        return 6.0 + level * 3.0;
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter world, BlockPos pos, Entity entity) {
        return insideCollisionShapeByLevel[state.getValue(this.getLevelProperty()) - 1];
    }
}