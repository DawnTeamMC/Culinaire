package fr.hugman.culinaire.block.entity;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.block.CulinaireBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CulinaireBlockEntityTypes {
    public static final BlockEntityType<KettleBlockEntity> KETTLE = register("kettle", KettleBlockEntity::new, CulinaireBlocks.KETTLE);

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Block... blocks) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Culinaire.id(name), FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
