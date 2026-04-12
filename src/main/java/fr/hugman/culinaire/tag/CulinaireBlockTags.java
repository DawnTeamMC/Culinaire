package fr.hugman.culinaire.tag;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CulinaireBlockTags {
    public static final TagKey<Block> KETTLE_HOT_BLOCKS = of("kettle_hot_blocks");

    private static TagKey<Block> of(String path) {
        return TagKey.create(Registries.BLOCK, Culinaire.id(path));
    }
}
