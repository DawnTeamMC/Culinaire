package com.hugman.culinaire.registry.content;

import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.block.KettleBlock;
import com.hugman.culinaire.block.entity.KettleBlockEntity;
import com.hugman.culinaire.item.TeaBagItem;
import com.hugman.culinaire.item.TeaBottleItem;
import com.hugman.culinaire.recipe.TeaBagMakingRecipe;
import com.hugman.culinaire.screen.handler.KettleScreenHandler;
import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import fr.hugman.dawn.item.DawnItemSettings;
import fr.hugman.dawn.item.ItemGroupHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class TeaContent {
    public static final Item TEA_BAG = new TeaBagItem(new Item.Settings().maxCount(16));
    public static final SpecialRecipeSerializer<TeaBagMakingRecipe> TEA_BAG_MAKING = new SpecialRecipeSerializer<>(TeaBagMakingRecipe::new);

    public static final Item TEA_BOTTLE = new TeaBottleItem(new Item.Settings().maxCount(1).recipeRemainder(Items.GLASS_BOTTLE));

    public static final SoundEvent TEA_BOTTLE_FILL_SOUND = SoundEvent.of(Culinaire.id("item.tea_bottle.fill"));

    public static final Block KETTLE = new KettleBlock(DawnBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().strength(5.0F, 1200.0F).sounds(BlockSoundGroup.STONE).item());
    public static final ScreenHandlerType<KettleScreenHandler> KETTLE_SCREEN_HANDLER = new ScreenHandlerType<>(KettleScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
    public static final BlockEntityType<KettleBlockEntity> KETTLE_ENTITY = FabricBlockEntityTypeBuilder.create(KettleBlockEntity::new, KETTLE).build();
    public static final Identifier KETTLE_INTERACTION_STAT = Culinaire.id("interact_with_kettle");
    public static final SoundEvent KETTLE_BREW_SOUND = SoundEvent.of(Culinaire.id("block.kettle.brew"));

    public static void register(Registrar r) {
        r.add("tea_bag", TEA_BAG);
        Registry.register(Registries.RECIPE_SERIALIZER, r.id("tea_bag_making"), TEA_BAG_MAKING); //TODO: add a method for recipe serializers to Dawn API
        r.add("tea_bottle", TEA_BOTTLE);

        r.add("kettle", KETTLE);
        r.add("kettle", KETTLE_SCREEN_HANDLER);
        r.add("kettle", KETTLE_ENTITY);
        Registry.register(Registries.CUSTOM_STAT, KETTLE_INTERACTION_STAT, KETTLE_INTERACTION_STAT);
        r.add(KETTLE_BREW_SOUND);

        ItemGroupHelper.append(ItemGroups.FUNCTIONAL, entries -> entries.addAfter(Blocks.BLAST_FURNACE, KETTLE));

        //TODO: append all tea types to creative tab
    }
}
