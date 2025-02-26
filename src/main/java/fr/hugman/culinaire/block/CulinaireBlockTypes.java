package fr.hugman.culinaire.block;

import com.mojang.serialization.MapCodec;
import fr.hugman.culinaire.Culinaire;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CulinaireBlockTypes {
    private static <B extends Block> MapCodec<B> of(String path, MapCodec<B> blockType) {
        return Registry.register(Registries.BLOCK_TYPE, Culinaire.id(path), blockType);
    }
}
