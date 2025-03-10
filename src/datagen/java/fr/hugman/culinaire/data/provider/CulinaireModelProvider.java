package fr.hugman.culinaire.data.provider;

import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.item.Items;

public class CulinaireModelProvider extends FabricModelProvider {
    public CulinaireModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        gen.registerItemModel(CulinaireBlocks.CHEESE_WHEEL.asItem());

        gen.registerItemModel(CulinaireBlocks.KETTLE.asItem());
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
        gen.register(CulinaireItems.LETTUCE, Models.GENERATED);
        gen.register(CulinaireItems.LETTUCE_SEEDS, Models.GENERATED);
        gen.register(CulinaireItems.TOMATO, Models.GENERATED);

        gen.register(CulinaireItems.MILK_BOTTLE, Models.GENERATED);
        gen.register(CulinaireItems.CHEESE, Models.GENERATED);

        gen.register(CulinaireItems.DARK_CHOCOLATE_BOTTLE, Models.GENERATED);
        gen.register(CulinaireItems.MILK_CHOCOLATE_BOTTLE, Models.GENERATED);
        gen.register(CulinaireItems.WHITE_CHOCOLATE_BOTTLE, Models.GENERATED);
        gen.register(CulinaireItems.DARK_CHOCOLATE_BAR, Models.GENERATED);
        gen.register(CulinaireItems.MILK_CHOCOLATE_BAR, Models.GENERATED);
        gen.register(CulinaireItems.WHITE_CHOCOLATE_BAR, Models.GENERATED);
        gen.register(CulinaireItems.DARK_CHOCOLATE_PIE, Models.GENERATED);
        gen.register(CulinaireItems.MILK_CHOCOLATE_PIE, Models.GENERATED);
        gen.register(CulinaireItems.WHITE_CHOCOLATE_PIE, Models.GENERATED);

        gen.register(CulinaireItems.MARSHMALLOW, Models.GENERATED);
        gen.register(CulinaireItems.MARSHMALLOW_ON_A_STICK, Models.GENERATED);
        gen.register(CulinaireItems.TOASTY_MARSHMALLOW_ON_A_STICK, Models.GENERATED);
        gen.register(CulinaireItems.GOLDEN_MARSHMALLOW_ON_A_STICK, Models.GENERATED);
        gen.register(CulinaireItems.BURNT_MARSHMALLOW_ON_A_STICK, Models.GENERATED);

        gen.register(CulinaireItems.TEA_BAG, Models.GENERATED);
        gen.registerPotion(CulinaireItems.TEA_BOTTLE);

        gen.register(CulinaireItems.CROISSANT, Models.GENERATED);
        gen.register(CulinaireItems.CHOUQUETTE, Models.GENERATED);
        gen.register(CulinaireItems.APPLE_PIE, Models.GENERATED);
        gen.register(CulinaireItems.SWEET_BERRY_PIE, Models.GENERATED);

        gen.register(CulinaireItems.SANDWICH, Models.GENERATED);

        gen.register(CulinaireItems.SALAD, Models.GENERATED);
        gen.register(CulinaireItems.MASHED_POTATOES, Models.GENERATED);
    }
}
