package fr.hugman.culinaire.data.provider;

import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.client.render.item.tint.TeaTintSource;
import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

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
        gen.register(CulinaireItems.MARSHMALLOW_ON_A_STICK, Models.HANDHELD);
        gen.register(CulinaireItems.TOASTY_MARSHMALLOW_ON_A_STICK, Models.HANDHELD);
        gen.register(CulinaireItems.GOLDEN_MARSHMALLOW_ON_A_STICK, Models.HANDHELD);
        gen.register(CulinaireItems.BURNT_MARSHMALLOW_ON_A_STICK, Models.HANDHELD);

        gen.register(CulinaireItems.TEA_BAG, Models.GENERATED);
        registerTeaBottle(gen, CulinaireItems.TEA_BOTTLE);

        gen.register(CulinaireItems.CROISSANT, Models.GENERATED);
        gen.register(CulinaireItems.CHOUQUETTE, Models.GENERATED);
        gen.register(CulinaireItems.APPLE_PIE, Models.GENERATED);
        gen.register(CulinaireItems.SWEET_BERRY_PIE, Models.GENERATED);

        gen.register(CulinaireItems.SANDWICH, Models.GENERATED);

        gen.register(CulinaireItems.SALAD, Models.GENERATED);
        gen.register(CulinaireItems.MASHED_POTATOES, Models.GENERATED);
    }

    public final void registerTeaBottle(ItemModelGenerator gen, Item item) {
        Identifier identifier = gen.uploadTwoLayers(item, ModelIds.getMinecraftNamespacedItem("potion_overlay"), ModelIds.getItemModelId(Items.POTION));
        gen.output.accept(item, ItemModels.tinted(identifier, new TeaTintSource()));
    }
}
