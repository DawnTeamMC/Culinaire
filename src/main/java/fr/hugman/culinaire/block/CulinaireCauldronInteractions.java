package fr.hugman.culinaire.block;

import fr.hugman.culinaire.block.cauldron.CauldronInteractionBuilder;
import fr.hugman.culinaire.block.cauldron.CauldronUtil;
import fr.hugman.culinaire.item.CulinaireItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;

public class CulinaireCauldronInteractions {
    public static CauldronInteraction.Dispatcher MILK = CauldronInteractions.newDispatcher("culinaire:milk");
    public static CauldronInteraction.Dispatcher DARK_CHOCOLATE = CauldronInteractions.newDispatcher("culinaire:dark_chocolate");
    public static CauldronInteraction.Dispatcher MILK_CHOCOLATE = CauldronInteractions.newDispatcher("culinaire:milk_chocolate");
    public static CauldronInteraction.Dispatcher WHITE_CHOCOLATE = CauldronInteractions.newDispatcher("culinaire:white_chocolate");

    public static void register() {
        // Milk Cauldron
        CauldronUtil.addBottleInteractions(MILK, CulinaireBlocks.MILK_CAULDRON, CulinaireItems.MILK_BOTTLE);
        CauldronUtil.addBucketInteractions(MILK, CulinaireBlocks.MILK_CAULDRON, Items.MILK_BUCKET);
        MILK.put(Items.SUGAR, CauldronInteractionBuilder.create().addLevel(0).cauldron(CulinaireBlocks.WHITE_CHOCOLATE_CAULDRON).build());

        // Dark Chocolate Cauldron
        CauldronUtil.addBottleInteractions(DARK_CHOCOLATE, CulinaireBlocks.DARK_CHOCOLATE_CAULDRON, CulinaireItems.DARK_CHOCOLATE_BOTTLE);
        DARK_CHOCOLATE.put(Items.MILK_BUCKET, CauldronInteractionBuilder.create().addLevel(3).cauldron(CulinaireBlocks.MILK_CHOCOLATE_CAULDRON).sound(SoundEvents.BUCKET_EMPTY).build());
        DARK_CHOCOLATE.put(CulinaireItems.MILK_BOTTLE, CauldronInteractionBuilder.create().addLevel(1).cauldron(CulinaireBlocks.MILK_CHOCOLATE_CAULDRON).sound(SoundEvents.BOTTLE_EMPTY).build());

        // Milk Chocolate Cauldron
        CauldronUtil.addBottleInteractions(MILK_CHOCOLATE, CulinaireBlocks.MILK_CHOCOLATE_CAULDRON, CulinaireItems.MILK_CHOCOLATE_BOTTLE);

        // White Chocolate Cauldron
        CauldronUtil.addBottleInteractions(WHITE_CHOCOLATE, CulinaireBlocks.WHITE_CHOCOLATE_CAULDRON, CulinaireItems.WHITE_CHOCOLATE_BOTTLE);
        WHITE_CHOCOLATE.put(Items.COCOA_BEANS, CauldronInteractionBuilder.create().addLevel(0).cauldron(CulinaireBlocks.MILK_CHOCOLATE_CAULDRON).build());
    }
}
