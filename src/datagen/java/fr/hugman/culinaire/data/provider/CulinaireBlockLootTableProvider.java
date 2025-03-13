package fr.hugman.culinaire.data.provider;

import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.block.TomatoesBlock;
import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class CulinaireBlockLootTableProvider extends FabricBlockLootTableProvider {
    public CulinaireBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.addDrop(CulinaireBlocks.LETTUCE, this.cropDrops(
                CulinaireBlocks.LETTUCE, CulinaireItems.LETTUCE, CulinaireItems.LETTUCE_SEEDS,
                BlockStatePropertyLootCondition.builder(CulinaireBlocks.LETTUCE)
                        .properties(StatePredicate.Builder.create().exactMatch(CropBlock.AGE, 3))
        ));
        this.addDrop(CulinaireBlocks.TOMATOES, this.cropDrops(
                CulinaireBlocks.TOMATOES, CulinaireItems.TOMATO,
                BlockStatePropertyLootCondition.builder(CulinaireBlocks.TOMATOES)
                        .properties(StatePredicate.Builder.create().exactMatch(TomatoesBlock.HALF, DoubleBlockHalf.LOWER)),
                BlockStatePropertyLootCondition.builder(CulinaireBlocks.TOMATOES)
                        .properties(StatePredicate.Builder.create().exactMatch(TomatoesBlock.HALF, DoubleBlockHalf.LOWER).exactMatch(CropBlock.AGE, 3))
        ));

        // DIARIES
        this.addDrop(CulinaireBlocks.MILK_CAULDRON, Blocks.CAULDRON);
        this.addDrop(CulinaireBlocks.CHEESE_WHEEL, dropsNothing());
        this.addDrop(CulinaireBlocks.CHEESE_CAULDRON, Blocks.CAULDRON);

        this.addDrop(CulinaireBlocks.CHEESE_CAULDRON, Blocks.CAULDRON);

        this.addDrop(CulinaireBlocks.DARK_CHOCOLATE_CAULDRON, Blocks.CAULDRON);
        this.addDrop(CulinaireBlocks.MILK_CHOCOLATE_CAULDRON, Blocks.CAULDRON);
        this.addDrop(CulinaireBlocks.WHITE_CHOCOLATE_CAULDRON, Blocks.CAULDRON);

        // TEA
        this.addDrop(CulinaireBlocks.KETTLE);

        this.lootTables.forEach((id, lootTable) -> lootTable.randomSequenceId(id.getValue()));
    }

    public LootTable.Builder cropDrops(Block crop, Item product, LootCondition.Builder condition1, LootCondition.Builder condition2) {
        RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);
        return this.applyExplosionDecay(
                crop,
                LootTable.builder()
                        .pool(LootPool.builder().with(
                                ItemEntry.builder(product).conditionally(condition1)
                        ))
                        .pool(
                                LootPool.builder()
                                        .conditionally(condition2)
                                        .with(ItemEntry.builder(product)
                                                .apply(ApplyBonusLootFunction.binomialWithBonusCount(impl.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3))
                                        )
                        )
        );
    }
}