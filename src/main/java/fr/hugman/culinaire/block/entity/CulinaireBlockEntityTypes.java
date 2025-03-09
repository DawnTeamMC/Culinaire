package fr.hugman.culinaire.block.entity;

import fr.hugman.culinaire.block.CulinaireBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public class CulinaireBlockEntityTypes {
    public static final BlockEntityType<KettleBlockEntity> KETTLE = FabricBlockEntityTypeBuilder.create(KettleBlockEntity::new, CulinaireBlocks.KETTLE).build();
}
