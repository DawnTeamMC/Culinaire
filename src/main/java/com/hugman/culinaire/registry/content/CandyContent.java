package com.hugman.culinaire.registry.content;

import com.hugman.culinaire.block.CulinaireCauldronBehaviors;
import com.hugman.culinaire.item.ChocolateBottleItem;
import com.hugman.culinaire.item.MarshmallowOnAStickItem;
import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.ThreeLeveledCauldronBlock;
import fr.hugman.dawn.item.DawnItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CandyContent {
    private static final FoodComponent CHOCOLATE_PIE_FOOD = new FoodComponent.Builder().hunger(6).saturationModifier(0.3F).build();
    private static final FoodComponent CHOCOLATE_BAR_FOOD = new FoodComponent.Builder().hunger(3).saturationModifier(0.2F).build();

    public static final Item DARK_CHOCOLATE_BOTTLE = new ChocolateBottleItem(new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE));
    public static final Item MILK_CHOCOLATE_BOTTLE = new ChocolateBottleItem(new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE));
    public static final Item WHITE_CHOCOLATE_BOTTLE = new ChocolateBottleItem(new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE));

    public static final Item DARK_CHOCOLATE_BAR = new Item(new Item.Settings().food(CHOCOLATE_BAR_FOOD));
    public static final Item MILK_CHOCOLATE_BAR = new Item(new Item.Settings().food(CHOCOLATE_BAR_FOOD));
    public static final Item WHITE_CHOCOLATE_BAR = new Item(new Item.Settings().food(CHOCOLATE_BAR_FOOD));

    public static final Item DARK_CHOCOLATE_PIE = new Item(new Item.Settings().food(CHOCOLATE_PIE_FOOD));
    public static final Item MILK_CHOCOLATE_PIE = new Item(new Item.Settings().food(CHOCOLATE_PIE_FOOD));
    public static final Item WHITE_CHOCOLATE_PIE = new Item(new Item.Settings().food(CHOCOLATE_PIE_FOOD));

    public static final Block DARK_CHOCOLATE_CAULDRON = new ThreeLeveledCauldronBlock(CulinaireCauldronBehaviors.DARK_CHOCOLATE, FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0F).nonOpaque());
    public static final Block MILK_CHOCOLATE_CAULDRON = new ThreeLeveledCauldronBlock(CulinaireCauldronBehaviors.MILK_CHOCOLATE, FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0F).nonOpaque());
    public static final Block WHITE_CHOCOLATE_CAULDRON = new ThreeLeveledCauldronBlock(CulinaireCauldronBehaviors.WHITE_CHOCOLATE, FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0F).nonOpaque());

    private static final FoodComponent MARSHMALLOW_FOOD = new FoodComponent.Builder().hunger(1).saturationModifier(0.2F).snack().build();
    private static final FoodComponent TOASTY_MARSHMALLOW_FOOD = new FoodComponent.Builder().hunger(3).saturationModifier(0.4F).snack().build();
    private static final FoodComponent GOLDEN_MARSHMALLOW_FOOD = new FoodComponent.Builder().hunger(5).saturationModifier(0.3F).snack().build();
    private static final FoodComponent BURNT_MARSHMALLOW_FOOD = new FoodComponent.Builder().hunger(1).saturationModifier(0F).snack().statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 200), 0.2f).build();

    public static final Item MARSHMALLOW = new Item(new DawnItemSettings().food(MARSHMALLOW_FOOD).compostingChance(0.3f));
    public static final Item MARSHMALLOW_ON_A_STICK = new MarshmallowOnAStickItem(new DawnItemSettings().food(MARSHMALLOW_FOOD).maxCount(1).recipeRemainder(Items.STICK));
    public static final Item TOASTY_MARSHMALLOW_ON_A_STICK = new MarshmallowOnAStickItem(new DawnItemSettings().food(TOASTY_MARSHMALLOW_FOOD).maxCount(1).recipeRemainder(Items.STICK));
    public static final Item GOLDEN_MARSHMALLOW_ON_A_STICK = new MarshmallowOnAStickItem(new DawnItemSettings().food(GOLDEN_MARSHMALLOW_FOOD).maxCount(1).recipeRemainder(Items.STICK));
    public static final Item BURNT_MARSHMALLOW_ON_A_STICK = new MarshmallowOnAStickItem(new DawnItemSettings().food(BURNT_MARSHMALLOW_FOOD).maxCount(1).recipeRemainder(Items.STICK));

    public static void register(Registrar r) {
        r.add("dark_chocolate_bottle", DARK_CHOCOLATE_BOTTLE);
        r.add("milk_chocolate_bottle", MILK_CHOCOLATE_BOTTLE);
        r.add("white_chocolate_bottle", WHITE_CHOCOLATE_BOTTLE);

        r.add("dark_chocolate_bar", DARK_CHOCOLATE_BAR);
        r.add("milk_chocolate_bar", MILK_CHOCOLATE_BAR);
        r.add("white_chocolate_bar", WHITE_CHOCOLATE_BAR);

        r.add("dark_chocolate_pie", DARK_CHOCOLATE_PIE);
        r.add("milk_chocolate_pie", MILK_CHOCOLATE_PIE);
        r.add("white_chocolate_pie", WHITE_CHOCOLATE_PIE);

        r.add("dark_chocolate_cauldron", DARK_CHOCOLATE_CAULDRON);
        r.add("milk_chocolate_cauldron", MILK_CHOCOLATE_CAULDRON);
        r.add("white_chocolate_cauldron", WHITE_CHOCOLATE_CAULDRON);

        r.add("marshmallow", MARSHMALLOW);
        r.add("marshmallow_on_a_stick", MARSHMALLOW_ON_A_STICK);
        r.add("toasty_marshmallow_on_a_stick", TOASTY_MARSHMALLOW_ON_A_STICK);
        r.add("golden_marshmallow_on_a_stick", GOLDEN_MARSHMALLOW_ON_A_STICK);
        r.add("burnt_marshmallow_on_a_stick", BURNT_MARSHMALLOW_ON_A_STICK);

        // TODO: Add item group
    }
}
