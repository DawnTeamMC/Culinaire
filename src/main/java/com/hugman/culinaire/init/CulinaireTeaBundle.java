package com.hugman.culinaire.init;

import com.hugman.culinaire.objects.block.KettleBlock;
import com.hugman.culinaire.objects.block_entity.KettleBlockEntity;
import com.hugman.culinaire.objects.item.TeaBagItem;
import com.hugman.culinaire.objects.item.TeaBottleItem;
import com.hugman.culinaire.objects.recipe.TeaBagMakingRecipe;
import com.hugman.culinaire.objects.screen.handler.KettleScreenHandler;
import com.hugman.dawn.api.creator.BlockCreator;
import com.hugman.dawn.api.creator.BlockEntityCreator;
import com.hugman.dawn.api.creator.EffectCreator;
import com.hugman.dawn.api.creator.ItemCreator;
import com.hugman.dawn.api.creator.RecipeSerializerCreator;
import com.hugman.dawn.api.creator.ScreenHandlerCreator;
import com.hugman.dawn.api.creator.SoundCreator;
import com.hugman.dawn.api.creator.StatCreator;
import com.hugman.dawn.mod.object.effect.SimpleEffect;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.sound.BlockSoundGroup;

public class CulinaireTeaBundle extends CulinaireBundle {
	public static final Item TEA_BAG = add(new ItemCreator.Builder("tea_bag", TeaBagItem::new, new Item.Settings().group(ItemGroup.FOOD).maxCount(16)).build());
	public static final SpecialRecipeSerializer<TeaBagMakingRecipe> TEA_BAG_MAKING = add(new RecipeSerializerCreator<>("tea_bag_making", new SpecialRecipeSerializer<>(TeaBagMakingRecipe::new)));

	public static final Item TEA_BOTTLE = add(new ItemCreator.Builder("tea_bottle", TeaBottleItem::new, new Item.Settings().group(ItemGroup.FOOD).maxCount(1).recipeRemainder(Items.GLASS_BOTTLE)).build());
	public static final SoundCreator TEA_BOTTLE_FILL_SOUND = creator(new SoundCreator("item.tea_bottle.fill"));

	public static final Block KETTLE = add(new BlockCreator.Builder("kettle", KettleBlock::new, FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().strength(5.0F, 1200.0F).sounds(BlockSoundGroup.STONE)).itemGroup(ItemGroup.DECORATIONS).build());
	public static final ScreenHandlerCreator<KettleScreenHandler> KETTLE_SCREEN_HANDLER = creator(new ScreenHandlerCreator<>("kettle", KettleScreenHandler::new));
	public static final BlockEntityType<KettleBlockEntity> KETTLE_ENTITY = add(new BlockEntityCreator<>("kettle", FabricBlockEntityTypeBuilder.create(KettleBlockEntity::new, KETTLE)));
	public static final StatCreator KETTLE_INTERACTION_STAT = creator(new StatCreator("interact_with_kettle"));
	public static final SoundCreator KETTLE_BREW_SOUND = creator(new SoundCreator("block.kettle.brew"));

	public static final StatusEffect FORESIGHT = add(new EffectCreator("foresight", new SimpleEffect(StatusEffectType.BENEFICIAL, 9966823)));
	public static final StatusEffect FULFILLMENT = add(new EffectCreator("fulfillment", new SimpleEffect(StatusEffectType.BENEFICIAL, 6048577)));
	public static final StatusEffect GUARD = add(new EffectCreator("guard", new SimpleEffect(StatusEffectType.BENEFICIAL, 16440596)));
	public static final StatusEffect POISON_RESISTANCE = add(new EffectCreator("poison_resistance", new SimpleEffect(StatusEffectType.BENEFICIAL, 7596722)));

	public static void init() {
	}
}
