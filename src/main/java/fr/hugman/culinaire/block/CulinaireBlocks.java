package fr.hugman.culinaire.block;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.Function;

public class CulinaireBlocks {
    // VEGETABLES
    public static final Block LETTUCE = registerNoItem("lettuce", LettuceBlock::new, AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block TOMATOES = registerNoItem("tomatoes", TomatoesBlock::new, AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).pistonBehavior(PistonBehavior.DESTROY));

    // DIARIES
    public static final Block MILK_CAULDRON = registerNoItem("milk_cauldron", s -> new MilkCauldronBlock(s, CulinaireCauldronBehaviors.MILK), AbstractBlock.Settings.copyShallow(Blocks.CAULDRON).ticksRandomly());

    public static final Block CHEESE_WHEEL = register("cheese_wheel", CheeseWheelBlock::new, AbstractBlock.Settings.copy(Blocks.CAKE));
    public static final Block CHEESE_CAULDRON = registerNoItem("cheese_cauldron", CheeseCauldronBlock::new, AbstractBlock.Settings.copy(Blocks.CAULDRON));

    // CANDIES
    public static final Block DARK_CHOCOLATE_CAULDRON = registerNoItem("dark_chocolate_cauldron", s -> new ThreeLeveledCauldronBlock(s, CulinaireCauldronBehaviors.DARK_CHOCOLATE), AbstractBlock.Settings.copyShallow(Blocks.CAULDRON));
    public static final Block MILK_CHOCOLATE_CAULDRON = registerNoItem("milk_chocolate_cauldron", s -> new ThreeLeveledCauldronBlock(s, CulinaireCauldronBehaviors.MILK_CHOCOLATE), AbstractBlock.Settings.copyShallow(Blocks.CAULDRON));
    public static final Block WHITE_CHOCOLATE_CAULDRON = registerNoItem("white_chocolate_cauldron", s -> new ThreeLeveledCauldronBlock(s, CulinaireCauldronBehaviors.WHITE_CHOCOLATE), AbstractBlock.Settings.copyShallow(Blocks.CAULDRON));

    // TEA
    public static final Block KETTLE = register("kettle", KettleBlock::new, AbstractBlock.Settings.create().mapColor(MapColor.IRON_GRAY).requiresTool().strength(5.0F, 1200.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK));

    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, Culinaire.id(id));
    }

    private static Block register(
            RegistryKey<Block> key,
            Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings,
            Item.Settings itemSettings
    ) {
        if (factory == null) {
            throw new IllegalStateException("Cannot register block: factory is not set!");
        }
        var block = factory.apply(settings.registryKey(key));
        Registry.register(Registries.BLOCK, key, block);
        if (itemSettings instanceof Item.Settings) {
            var itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, key.getValue());
            Registry.register(Registries.ITEM, itemRegistryKey, new BlockItem(block, itemSettings.registryKey(itemRegistryKey)));
        }
        return block;
    }

    private static Block register(
            String id,
            Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings,
            Item.Settings itemSettings
    ) {
        return register(keyOf(id), factory, settings, itemSettings);
    }

    private static Block register(
            String id,
            Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings
    ) {
        return register(id, factory, settings, new Item.Settings().useBlockPrefixedTranslationKey());
    }

    private static Block registerNoItem(
            String id,
            Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings
    ) {
        return register(id, factory, settings, null);
    }
}
