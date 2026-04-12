package fr.hugman.culinaire.data.provider;

import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.block.TomatoesBlock;
import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.concurrent.CompletableFuture;

public class CulinaireBlockLootTableProvider extends FabricBlockLootSubProvider {
    public CulinaireBlockLootTableProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.add(CulinaireBlocks.LETTUCE, this.createCropDrops(
                CulinaireBlocks.LETTUCE, CulinaireItems.LETTUCE, CulinaireItems.LETTUCE_SEEDS,
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(CulinaireBlocks.LETTUCE)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 3))
        ));
        this.add(CulinaireBlocks.TOMATOES, this.createCropDrops(
                CulinaireBlocks.TOMATOES, CulinaireItems.TOMATO,
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(CulinaireBlocks.TOMATOES)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TomatoesBlock.HALF, DoubleBlockHalf.LOWER)),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(CulinaireBlocks.TOMATOES)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TomatoesBlock.HALF, DoubleBlockHalf.LOWER).hasProperty(CropBlock.AGE, 3))
        ));

        // DIARIES
        this.dropOther(CulinaireBlocks.MILK_CAULDRON, Blocks.CAULDRON);
        this.add(CulinaireBlocks.CHEESE_WHEEL, noDrop());
        this.dropOther(CulinaireBlocks.CHEESE_CAULDRON, Blocks.CAULDRON);

        this.dropOther(CulinaireBlocks.CHEESE_CAULDRON, Blocks.CAULDRON);

        this.dropOther(CulinaireBlocks.DARK_CHOCOLATE_CAULDRON, Blocks.CAULDRON);
        this.dropOther(CulinaireBlocks.MILK_CHOCOLATE_CAULDRON, Blocks.CAULDRON);
        this.dropOther(CulinaireBlocks.WHITE_CHOCOLATE_CAULDRON, Blocks.CAULDRON);

        // TEA
        this.dropSelf(CulinaireBlocks.KETTLE);
    }

    public LootTable.Builder createCropDrops(Block crop, Item product, LootItemCondition.Builder condition1, LootItemCondition.Builder condition2) {
        HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.applyExplosionDecay(
                crop,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool().add(
                                LootItem.lootTableItem(product).when(condition1)
                        ))
                        .withPool(
                                LootPool.lootPool()
                                        .when(condition2)
                                        .add(LootItem.lootTableItem(product)
                                                .apply(ApplyBonusCount.addBonusBinomialDistributionCount(impl.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3))
                                        )
                        )
        );
    }
}