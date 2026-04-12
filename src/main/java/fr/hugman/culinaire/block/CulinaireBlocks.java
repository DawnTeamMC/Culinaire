package fr.hugman.culinaire.block;

import fr.hugman.culinaire.Culinaire;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class CulinaireBlocks {
    // VEGETABLES
    public static final Block LETTUCE = registerNoItem("lettuce", LettuceBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollision().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY));
    public static final Block TOMATOES = registerNoItem("tomatoes", TomatoesBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollision().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY));

    // DIARIES
    public static final Block MILK_CAULDRON = registerNoItem("milk_cauldron", s -> new MilkCauldronBlock(s, CulinaireCauldronBehaviors.MILK), BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAULDRON).randomTicks());

    public static final Block CHEESE_WHEEL = register("cheese_wheel", CheeseWheelBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE));
    public static final Block CHEESE_CAULDRON = registerNoItem("cheese_cauldron", CheeseCauldronBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON));

    // CANDIES
    public static final Block DARK_CHOCOLATE_CAULDRON = registerNoItem("dark_chocolate_cauldron", s -> new ThreeLeveledCauldronBlock(s, CulinaireCauldronBehaviors.DARK_CHOCOLATE), BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAULDRON));
    public static final Block MILK_CHOCOLATE_CAULDRON = registerNoItem("milk_chocolate_cauldron", s -> new ThreeLeveledCauldronBlock(s, CulinaireCauldronBehaviors.MILK_CHOCOLATE), BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAULDRON));
    public static final Block WHITE_CHOCOLATE_CAULDRON = registerNoItem("white_chocolate_cauldron", s -> new ThreeLeveledCauldronBlock(s, CulinaireCauldronBehaviors.WHITE_CHOCOLATE), BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAULDRON));

    // TEA
    public static final Block KETTLE = register("kettle", KettleBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).sound(SoundType.STONE).pushReaction(PushReaction.BLOCK));

    private static ResourceKey<Block> keyOf(String id) {
        return ResourceKey.create(Registries.BLOCK, Culinaire.id(id));
    }

    private static Block register(
            ResourceKey<Block> key,
            Function<BlockBehaviour.Properties, Block> factory,
            BlockBehaviour.Properties settings,
            Item.Properties itemSettings
    ) {
        if (factory == null) {
            throw new IllegalStateException("Cannot register block: factory is not set!");
        }
        var block = factory.apply(settings.setId(key));
        Registry.register(BuiltInRegistries.BLOCK, key, block);
        if (itemSettings instanceof Item.Properties) {
            var itemRegistryKey = ResourceKey.create(Registries.ITEM, key.identifier());
            Registry.register(BuiltInRegistries.ITEM, itemRegistryKey, new BlockItem(block, itemSettings.setId(itemRegistryKey)));
        }
        return block;
    }

    private static Block register(
            String id,
            Function<BlockBehaviour.Properties, Block> factory,
            BlockBehaviour.Properties settings,
            Item.Properties itemSettings
    ) {
        return register(keyOf(id), factory, settings, itemSettings);
    }

    private static Block register(
            String id,
            Function<BlockBehaviour.Properties, Block> factory,
            BlockBehaviour.Properties settings
    ) {
        return register(id, factory, settings, new Item.Properties().useBlockDescriptionPrefix());
    }

    private static Block registerNoItem(
            String id,
            Function<BlockBehaviour.Properties, Block> factory,
            BlockBehaviour.Properties settings
    ) {
        return register(id, factory, settings, null);
    }
}
