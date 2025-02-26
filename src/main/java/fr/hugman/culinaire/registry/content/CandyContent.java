package fr.hugman.culinaire.registry.content;

import fr.hugman.culinaire.block.CulinaireCauldronBehaviors;
import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import fr.hugman.dawn.block.ThreeLeveledCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class CandyContent {
    public static final Block DARK_CHOCOLATE_CAULDRON = new ThreeLeveledCauldronBlock(CulinaireCauldronBehaviors.DARK_CHOCOLATE, DawnBlockSettings.copy(Blocks.CAULDRON));
    public static final Block MILK_CHOCOLATE_CAULDRON = new ThreeLeveledCauldronBlock(CulinaireCauldronBehaviors.MILK_CHOCOLATE, DawnBlockSettings.copy(Blocks.CAULDRON));
    public static final Block WHITE_CHOCOLATE_CAULDRON = new ThreeLeveledCauldronBlock(CulinaireCauldronBehaviors.WHITE_CHOCOLATE, DawnBlockSettings.copy(Blocks.CAULDRON));

    public static void register(Registrar r) {
        r.add("dark_chocolate_cauldron", DARK_CHOCOLATE_CAULDRON);
        r.add("milk_chocolate_cauldron", MILK_CHOCOLATE_CAULDRON);
        r.add("white_chocolate_cauldron", WHITE_CHOCOLATE_CAULDRON);
    }
}
