package hugman.common_expansion.init.data;

import hugman.common_expansion.CommonExpansion;
import hugman.common_expansion.init.CEBlocks;
import hugman.common_expansion.objects.block.block_entity.MilkCauldronBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CEBlockEntityTypes {
	public static final BlockEntityType<MilkCauldronBlockEntity> MILK_CAULDRON = register("milk_cauldron", BlockEntityType.Builder.create(MilkCauldronBlockEntity::new, CEBlocks.MILK_CAULDRON));

	private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.Builder<T> builder) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(CommonExpansion.MOD_ID, name), builder.build(null));
	}
}
