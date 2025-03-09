package fr.hugman.culinaire.block.entity;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.block.CulinaireBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CulinaireBlockEntityTypes {
    public static final BlockEntityType<KettleBlockEntity> KETTLE = register("kettle", KettleBlockEntity::new, CulinaireBlocks.KETTLE);

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Block... blocks) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Culinaire.id(name), FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
