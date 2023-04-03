package com.hugman.culinaire.registry.content;

import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.block.CheeseCauldronBlock;
import com.hugman.culinaire.block.CheeseWheelBlock;
import com.hugman.culinaire.block.CulinaireCauldronBehaviors;
import com.hugman.culinaire.block.MilkCauldronBlock;
import com.hugman.culinaire.item.MilkBottleItem;
import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import fr.hugman.dawn.item.DawnItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;

public class DairyContent {
    public static final Item MILK_BOTTLE = new MilkBottleItem(new Item.Settings().maxCount(Culinaire.CONFIG.features.milkBottlesMaxCount).recipeRemainder(Items.GLASS_BOTTLE));
    public static final Block MILK_CAULDRON = new MilkCauldronBlock(CulinaireCauldronBehaviors.MILK, DawnBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().ticksRandomly().strength(2.0F).nonOpaque());

    private static final FoodComponent CHEESE_FOOD = new FoodComponent.Builder().hunger(5).saturationModifier(0.5f).build();

    public static final Item CHEESE = new Item(new DawnItemSettings().food(CHEESE_FOOD).compostingChance(0.5f));
    public static final Block CHEESE_WHEEL = new CheeseWheelBlock(DawnBlockSettings.of(Material.CAKE).strength(0.5F).sounds(BlockSoundGroup.WOOL).item());
    public static final Block CHEESE_CAULDRON = new CheeseCauldronBlock(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0F).nonOpaque());

    public static void register(Registrar r) {
        r.add("milk_bottle", MILK_BOTTLE);
        r.add("milk_cauldron", MILK_CAULDRON);

        r.add("cheese", CHEESE);
        r.add("cheese_wheel", CHEESE_WHEEL);
        r.add("cheese_cauldron", CHEESE_CAULDRON);

        // TODO: Add item group
    }
}
