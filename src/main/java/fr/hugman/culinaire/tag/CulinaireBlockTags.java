package fr.hugman.culinaire.tag;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class CulinaireBlockTags {
    public static final TagKey<Block> KETTLE_HOT_BLOCKS = of("kettle_hot_blocks");

    private static TagKey<Block> of(String path) {
        return TagKey.of(RegistryKeys.BLOCK, Culinaire.id(path));
    }
}
