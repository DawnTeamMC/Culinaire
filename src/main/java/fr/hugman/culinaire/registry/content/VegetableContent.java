package fr.hugman.culinaire.registry.content;

import fr.hugman.culinaire.block.LettuceBlock;
import fr.hugman.culinaire.block.TomatoesBlock;
import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.item.DawnItemSettings;
import fr.hugman.dawn.item.ItemGroupHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;

public class VegetableContent {
	public static final Block LETTUCE_BLOCK = new LettuceBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_GREEN).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).pistonBehavior(PistonBehavior.DESTROY));
	public static final Block TOMATO_BLOCK = new TomatoesBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_GREEN).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).pistonBehavior(PistonBehavior.DESTROY));

	public static void register(Registrar r) {
		r.add("lettuce", LETTUCE_BLOCK);
		r.add("tomatoes", TOMATO_BLOCK);
	}
}
