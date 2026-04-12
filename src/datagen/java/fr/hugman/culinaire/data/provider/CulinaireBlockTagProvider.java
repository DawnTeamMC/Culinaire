package fr.hugman.culinaire.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import java.util.concurrent.CompletableFuture;

import static fr.hugman.culinaire.block.CulinaireBlocks.*;
import static fr.hugman.culinaire.tag.CulinaireBlockTags.*;

public class CulinaireBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public CulinaireBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        // Culinaire

        valueLookupBuilder(KETTLE_HOT_BLOCKS)
                .forceAddTag(BlockTags.CAMPFIRES)
                .forceAddTag(BlockTags.FIRE)
                .add(Blocks.MAGMA_BLOCK, Blocks.LAVA, Blocks.LAVA_CAULDRON);

        // Vanilla
        valueLookupBuilder(BlockTags.CAULDRONS).add(MILK_CAULDRON, CHEESE_CAULDRON, DARK_CHOCOLATE_CAULDRON, MILK_CHOCOLATE_CAULDRON, WHITE_CHOCOLATE_CAULDRON);

        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(KETTLE);
        valueLookupBuilder(BlockTags.CROPS).add(TOMATOES, LETTUCE);
        valueLookupBuilder(BlockTags.MAINTAINS_FARMLAND).add(TOMATOES, LETTUCE);

        valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE).add(TOMATOES, LETTUCE);
    }
}