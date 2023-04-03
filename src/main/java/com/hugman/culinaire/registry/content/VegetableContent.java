package com.hugman.culinaire.registry.content;

import com.hugman.culinaire.block.LettuceBlock;
import com.hugman.culinaire.block.TomatoesBlock;
import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.item.DawnItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;

public class VegetableContent {
    private static final FoodComponent LETTUCE_FOOD = new FoodComponent.Builder().hunger(2).saturationModifier(0.2F).build();
    public static final Block LETTUCE_BLOCK = new LettuceBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));
    public static final Item LETTUCE_SEEDS = new AliasedBlockItem(LETTUCE_BLOCK, new DawnItemSettings().compostingChance(0.3f));
    public static final Item LETTUCE = new Item(new DawnItemSettings().food(LETTUCE_FOOD).compostingChance(0.3f));

    private static final FoodComponent TOMATO_FOOD = new FoodComponent.Builder().hunger(3).saturationModifier(0.5F).build();
    public static final Block TOMATO_BLOCK = new TomatoesBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));
    public static final Item TOMATO = new AliasedBlockItem(TOMATO_BLOCK, new DawnItemSettings().food(TOMATO_FOOD).compostingChance(0.3f));

    public static void register(Registrar r) {
        r.add("lettuce", LETTUCE_BLOCK); //TODO: client CUTOUT render
        r.add("lettuce_seeds", LETTUCE_SEEDS);
        r.add("lettuce", LETTUCE);
        r.add("tomatoes", TOMATO_BLOCK); //TODO: client CUTOUT render
        r.add("tomato", TOMATO);
    }
}
