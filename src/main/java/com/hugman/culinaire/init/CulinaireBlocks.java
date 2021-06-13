package com.hugman.culinaire.init;

import com.hugman.culinaire.objects.block.CheeseWheelBlock;
import com.hugman.culinaire.objects.block.KettleBlock;
import com.hugman.culinaire.objects.block.LettuceBlock;
import com.hugman.culinaire.objects.block.MilkCauldronBlock;
import com.hugman.culinaire.objects.block.TomatoesBlock;
import com.hugman.culinaire.objects.block.block_entity.KettleBlockEntity;
import com.hugman.culinaire.objects.block.block_entity.MilkCauldronBlockEntity;
import com.hugman.culinaire.objects.screen.handler.KettleScreenHandler;
import com.hugman.dawn.api.creator.BlockCreator;
import com.hugman.dawn.api.creator.BlockEntityCreator;
import com.hugman.dawn.api.creator.ScreenHandlerCreator;
import com.hugman.dawn.api.creator.SoundCreator;
import com.hugman.dawn.api.creator.StatCreator;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;

public class CulinaireBlocks extends CulinaireBundle {
	public static final Block CHEESE_WHEEL = add(new BlockCreator.Builder("cheese_wheel", CheeseWheelBlock::new, FabricBlockSettings.of(Material.CAKE).strength(0.5F).sounds(BlockSoundGroup.WOOL)).itemGroup(ItemGroup.FOOD).build());
	public static final Block LETTUCE = add(new BlockCreator.Builder("lettuce", LettuceBlock::new, FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)).render(BlockCreator.Render.CUTOUT).noItem().build());
	public static final Block TOMATOES = add(new BlockCreator.Builder("tomatoes", TomatoesBlock::new, FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)).render(BlockCreator.Render.CUTOUT).noItem().build());
	public static final Block MILK_CAULDRON = add(new BlockCreator.Builder("milk_cauldron", s -> new MilkCauldronBlock(s, CulinaireCauldronBehaviors.MILK_CAULDRON_BEHAVIOR), FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0F).nonOpaque()).noItem().build());
	public static final BlockEntityType<MilkCauldronBlockEntity> MILK_CAULDRON_ENTITY = add(new BlockEntityCreator<>("milk_cauldron", FabricBlockEntityTypeBuilder.create(MilkCauldronBlockEntity::new, CulinaireBlocks.MILK_CAULDRON)));
	public static final Block KETTLE = add(new BlockCreator.Builder("kettle", KettleBlock::new, FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().strength(5.0F, 1200.0F).sounds(BlockSoundGroup.STONE)).itemGroup(ItemGroup.DECORATIONS).build());
	public static final ScreenHandlerCreator<KettleScreenHandler> KETTLE_SCREEN_HANDLER = creator(new ScreenHandlerCreator<>("kettle", KettleScreenHandler::new));
	public static final BlockEntityType<KettleBlockEntity> KETTLE_ENTITY = add(new BlockEntityCreator<>("kettle", FabricBlockEntityTypeBuilder.create(KettleBlockEntity::new, CulinaireBlocks.KETTLE)));
	public static final Identifier KETTLE_INTERACTION_STAT = add(new StatCreator("interact_with_kettle"));
	public static final SoundEvent KETTLE_BREW_SOUND = creator(new SoundCreator("block.kettle.brew")).getSound();

	public static void init() {
	}

	public static class Properties {
		public static final BooleanProperty COAGULATED = BooleanProperty.of("coagulated");
		public static final IntProperty BITES_4 = IntProperty.of("bites", 0, 5);
		public static final IntProperty LEVEL_1_3 = IntProperty.of("level", 1, 3);
	}
}
