package fr.hugman.culinaire.block.cauldron;

import fr.hugman.culinaire.block.AbstractLeveledCauldronBlock;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class CauldronUtil {
    public static boolean isNotFull(BlockState state) {
        return !isFull(state);
    }

    public static boolean isFull(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof AbstractLeveledCauldronBlock abstractLeveledCauldronBlock) {
            return abstractLeveledCauldronBlock.isFull(state);
        } else if (block instanceof LayeredCauldronBlock leveledCauldronBlock) {
            return leveledCauldronBlock.isFull(state);
        } else {
            return false;
        }
    }

    public static int getLevel(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof AbstractLeveledCauldronBlock abstractLeveledCauldronBlock) {
            return abstractLeveledCauldronBlock.getLevel(state);
        } else if (block instanceof LayeredCauldronBlock) {
            return state.getValue(LayeredCauldronBlock.LEVEL);
        } else {
            return 0;
        }
    }

    public static BlockState modifyCauldron(BlockState currentCauldronState, Block newCauldron, int level) {
        if (newCauldron instanceof AbstractLeveledCauldronBlock abstractLeveled) {
            return currentCauldronState.is(abstractLeveled) ? abstractLeveled.setLevel(currentCauldronState, level) : abstractLeveled.defaultWithLevel(level);
        } else if (newCauldron instanceof LayeredCauldronBlock leveled) {
            if (currentCauldronState.is(leveled)) {
                int i = currentCauldronState.getValue(LayeredCauldronBlock.LEVEL) + level;
                i = Math.min(i, 3);
                return i <= 0 ? Blocks.CAULDRON.defaultBlockState() : currentCauldronState.setValue(LayeredCauldronBlock.LEVEL, i);
            } else {
                return leveled.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, level);
            }
        } else {
            return newCauldron.defaultBlockState();
        }
    }

    public static void addBottleInteractions(CauldronInteraction.InteractionMap map, Block cauldron, Item bottle) {
        CauldronInteraction pourBottleBehavior = CauldronInteractionBuilder.create().cauldron(cauldron).addLevel(1).item(Items.GLASS_BOTTLE).sound(SoundEvents.BOTTLE_EMPTY).build();

        CauldronInteraction.EMPTY.map().put(bottle, pourBottleBehavior); // Pour bottle into empty cauldron
        map.map().put(bottle, pourBottleBehavior); // Pour bottle into same cauldron

        map.map().put(Items.GLASS_BOTTLE, CauldronInteractionBuilder.create().addLevel(-1).item(bottle).sound(SoundEvents.BOTTLE_FILL).build()); // Fill bottle from cauldron
    }

    public static void addBucketInteractions(CauldronInteraction.InteractionMap map, Block cauldron, Item bucket) {
        CauldronInteraction pourBucketBehavior = CauldronInteractionBuilder.create().cauldron(cauldron).addLevel(3).item(Items.BUCKET).sound(SoundEvents.BUCKET_EMPTY).build();

        CauldronInteraction.EMPTY.map().put(bucket, pourBucketBehavior); // Pour bucket into any empty cauldron
        map.map().put(bucket, pourBucketBehavior); // Pour bucket into same cauldron

        map.map().put(Items.BUCKET, CauldronInteractionBuilder.create().addLevel(-3).item(bucket).sound(SoundEvents.BUCKET_FILL).build()); // Fill bucket from cauldron
    }
}