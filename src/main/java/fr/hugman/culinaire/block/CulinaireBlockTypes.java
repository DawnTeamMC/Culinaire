package fr.hugman.culinaire.block;

import com.mojang.serialization.MapCodec;
import fr.hugman.culinaire.Culinaire;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public class CulinaireBlockTypes {
    private static <B extends Block> MapCodec<B> of(String path, MapCodec<B> blockType) {
        return Registry.register(BuiltInRegistries.BLOCK_TYPE, Culinaire.id(path), blockType);
    }
}
