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
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;

public class CulinaireBlocks extends CulinairePack {
	public static final Block CHEESE_WHEEL = register(new BlockCreator.Builder("cheese_wheel", new CheeseWheelBlock(FabricBlockSettings.of(Material.CAKE).strength(0.5F).sounds(BlockSoundGroup.WOOL))).itemGroup(ItemGroup.FOOD));
	public static final Block LETTUCE = register(new BlockCreator.Builder("lettuce", new LettuceBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP))).render(BlockCreator.Render.CUTOUT).noItem());
	public static final Block TOMATOES = register(new BlockCreator.Builder("tomatoes", new TomatoesBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP))).render(BlockCreator.Render.CUTOUT).noItem());
	public static final ScreenHandlerType<KettleScreenHandler> KETTLE_SCREEN_HANDLER = register(new ScreenHandlerCreator.Builder<>("kettle", KettleScreenHandler::new));
	public static final Block MILK_CAULDRON = register(new BlockCreator.Builder("milk_cauldron", new MilkCauldronBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.STONE).requiresTool().strength(2.0F).nonOpaque())).noItem());
	public static final BlockEntityType<MilkCauldronBlockEntity> MILK_CAULDRON_ENTITY = register(new BlockEntityCreator.Builder<>("milk_cauldron", BlockEntityType.Builder.create(MilkCauldronBlockEntity::new, CulinaireBlocks.MILK_CAULDRON)));
	public static final Block KETTLE = register(new BlockCreator.Builder("kettle", new KettleBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().strength(5.0F, 1200.0F).sounds(BlockSoundGroup.STONE))).itemGroup(ItemGroup.DECORATIONS));
	public static final BlockEntityType<KettleBlockEntity> KETTLE_ENTITY = register(new BlockEntityCreator.Builder<>("kettle", BlockEntityType.Builder.create(KettleBlockEntity::new, CulinaireBlocks.KETTLE)));
	public static final Identifier KETTLE_INTERACTION_STAT = register(new StatCreator.Builder("interact_with_kettle"));
	public static final SoundEvent KETTLE_BREW_SOUND = register(new SoundCreator.Builder("block.kettle.brew"));

	public static void init() {
	}

	public static class Properties {
		public static final BooleanProperty COAGULATED = BooleanProperty.of("coagulated");
		public static final IntProperty BITES_4 = IntProperty.of("bites", 0, 5);
		public static final IntProperty LEVEL_1_3 = IntProperty.of("level", 1, 3);
	}
}
