package fr.hugman.culinaire.block;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

public class CulinaireBlocks {
    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, Culinaire.id(id));
    }

    private static Block register(
            RegistryKey<Block> key,
            Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings,
            Item.Settings itemSettings
    ) {
        if (factory == null) {
            throw new IllegalStateException("Cannot register block: factory is not set!");
        }
        var block = factory.apply(settings.registryKey(key));
        Registry.register(Registries.BLOCK, key, block);
        if (itemSettings instanceof Item.Settings) {
            var itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, key.getValue());
            Registry.register(Registries.ITEM, itemRegistryKey, new BlockItem(block, itemSettings.registryKey(itemRegistryKey)));
        }
        return block;
    }

    private static Block register(
            String id,
            Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings,
            Item.Settings itemSettings
    ) {
        return register(keyOf(id), factory, settings, itemSettings);
    }

    private static Block register(
            String id,
            Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings
    ) {
        return register(id, factory, settings, new Item.Settings().useBlockPrefixedTranslationKey());
    }
}
