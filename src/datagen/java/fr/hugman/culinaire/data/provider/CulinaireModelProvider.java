package fr.hugman.culinaire.data.provider;

import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.client.render.item.tint.TeaTintSource;
import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CulinaireModelProvider extends FabricModelProvider {
    public CulinaireModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        gen.registerSimpleFlatItemModel(CulinaireBlocks.CHEESE_WHEEL.asItem());

        gen.registerSimpleFlatItemModel(CulinaireBlocks.KETTLE.asItem());
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        gen.generateFlatItem(CulinaireItems.LETTUCE, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.LETTUCE_SEEDS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.TOMATO, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(CulinaireItems.MILK_BOTTLE, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.CHEESE, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(CulinaireItems.DARK_CHOCOLATE_BOTTLE, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.MILK_CHOCOLATE_BOTTLE, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.WHITE_CHOCOLATE_BOTTLE, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.DARK_CHOCOLATE_BAR, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.MILK_CHOCOLATE_BAR, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.WHITE_CHOCOLATE_BAR, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.DARK_CHOCOLATE_PIE, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.MILK_CHOCOLATE_PIE, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.WHITE_CHOCOLATE_PIE, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(CulinaireItems.MARSHMALLOW, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.MARSHMALLOW_ON_A_STICK, ModelTemplates.FLAT_HANDHELD_ITEM);
        gen.generateFlatItem(CulinaireItems.TOASTY_MARSHMALLOW_ON_A_STICK, ModelTemplates.FLAT_HANDHELD_ITEM);
        gen.generateFlatItem(CulinaireItems.GOLDEN_MARSHMALLOW_ON_A_STICK, ModelTemplates.FLAT_HANDHELD_ITEM);
        gen.generateFlatItem(CulinaireItems.BURNT_MARSHMALLOW_ON_A_STICK, ModelTemplates.FLAT_HANDHELD_ITEM);

        gen.generateFlatItem(CulinaireItems.TEA_BAG, ModelTemplates.FLAT_ITEM);
        registerTeaBottle(gen, CulinaireItems.TEA_BOTTLE);

        gen.generateFlatItem(CulinaireItems.CROISSANT, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.CHOUQUETTE, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.APPLE_PIE, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.SWEET_BERRY_PIE, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(CulinaireItems.SANDWICH, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(CulinaireItems.SALAD, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(CulinaireItems.MASHED_POTATOES, ModelTemplates.FLAT_ITEM);
    }

    public final void registerTeaBottle(ItemModelGenerators gen, Item item) {
        Identifier identifier = gen.generateLayeredItem(item, ModelLocationUtils.decorateItemModelLocation("potion_overlay"), ModelLocationUtils.getModelLocation(Items.POTION));
        gen.itemModelOutput.accept(item, ItemModelUtils.tintedModel(identifier, new TeaTintSource()));
    }
}
