package hugman.ce_foodstuffs.init;

import hugman.ce_foodstuffs.CEFoodstuffs;
import hugman.ce_foodstuffs.objects.block.LettuceBlock;
import hugman.ce_foodstuffs.objects.block.MilkCauldronBlock;
import hugman.ce_foodstuffs.objects.block.TomatoesBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class CEFBlocks {
	public static final Block MILK_CAULDRON = register("milk_cauldron", new MilkCauldronBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.STONE).requiresTool().strength(2.0F).nonOpaque()));
	public static final Block LETTUCE = register("lettuce", new LettuceBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)));
	public static final Block TOMATOES = register("tomatoes", new TomatoesBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)));

	private static Block register(String name, Block block) {
		return Registry.register(Registry.BLOCK, CEFoodstuffs.id(name), block);
	}
}
