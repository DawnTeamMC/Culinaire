package fr.hugman.culinaire.registry.content;

import fr.hugman.culinaire.block.CheeseCauldronBlock;
import fr.hugman.culinaire.block.CheeseWheelBlock;
import fr.hugman.culinaire.block.CulinaireCauldronBehaviors;
import fr.hugman.culinaire.block.MilkCauldronBlock;
import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import fr.hugman.dawn.item.ItemGroupHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

public class DairyContent {
    public static final Block MILK_CAULDRON = new MilkCauldronBlock(CulinaireCauldronBehaviors.MILK, DawnBlockSettings.copy(Blocks.CAULDRON).ticksRandomly());

    public static final Block CHEESE_WHEEL = new CheeseWheelBlock(DawnBlockSettings.copy(Blocks.CAKE).item());
    public static final Block CHEESE_CAULDRON = new CheeseCauldronBlock(DawnBlockSettings.copy(Blocks.CAULDRON));

    public static void register(Registrar r) {
        r.add("milk_cauldron", MILK_CAULDRON);

        r.add("cheese_wheel", CHEESE_WHEEL);
        r.add("cheese_cauldron", CHEESE_CAULDRON);

        ItemGroupHelper.append(ItemGroups.FOOD_AND_DRINK, entries -> entries.addAfter(Items.MILK_BUCKET, MILK_BOTTLE, CHEESE, CHEESE_WHEEL));
    }
}
