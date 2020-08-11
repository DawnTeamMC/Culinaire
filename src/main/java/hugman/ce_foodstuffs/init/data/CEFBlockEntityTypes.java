package hugman.ce_foodstuffs.init.data;

import hugman.ce_foodstuffs.CEFoodstuffs;
import hugman.ce_foodstuffs.init.CEFBlocks;
import hugman.ce_foodstuffs.objects.block.block_entity.MilkCauldronBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CEFBlockEntityTypes {
	public static final BlockEntityType<MilkCauldronBlockEntity> MILK_CAULDRON = register("milk_cauldron", BlockEntityType.Builder.create(MilkCauldronBlockEntity::new, CEFBlocks.MILK_CAULDRON));

	private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.Builder<T> builder) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(CEFoodstuffs.MOD_ID, name), builder.build(null));
	}
}
