package fr.hugman.culinaire.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

import static fr.hugman.culinaire.block.CulinaireBlocks.*;
import static fr.hugman.culinaire.tag.CulinaireBlockTags.*;

public class CulinaireBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public CulinaireBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        // Culinaire
        getOrCreateTagBuilder(KETTLE_HOT_BLOCKS)
                .forceAddTag(BlockTags.CAMPFIRES)
                .forceAddTag(BlockTags.FIRE)
                .add(Blocks.MAGMA_BLOCK, Blocks.LAVA, Blocks.LAVA_CAULDRON);

        // Vanilla
        getOrCreateTagBuilder(BlockTags.CAULDRONS).add(MILK_CAULDRON, CHEESE_CAULDRON, DARK_CHOCOLATE_CAULDRON, MILK_CHOCOLATE_CAULDRON, WHITE_CHOCOLATE_CAULDRON);

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(KETTLE);
        getOrCreateTagBuilder(BlockTags.CROPS).add(TOMATOES, LETTUCE);
        getOrCreateTagBuilder(BlockTags.MAINTAINS_FARMLAND).add(TOMATOES, LETTUCE);

        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(TOMATOES, LETTUCE);
    }
}