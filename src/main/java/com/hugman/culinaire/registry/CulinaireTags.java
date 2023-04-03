package com.hugman.culinaire.registry;

import com.hugman.culinaire.Culinaire;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class CulinaireTags {
    public static class Blocks {
        public static final TagKey<Block> KETTLE_HOT_BLOCKS = register("kettle_hot_blocks");

        private static TagKey<Block> register(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Culinaire.id(name));
        }
    }

    public static class Items {
        private static TagKey<Item> register(String name) {
            return TagKey.of(RegistryKeys.ITEM, Culinaire.id(name));
        }
    }
}
