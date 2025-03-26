package fr.hugman.culinaire.item;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.CulinaireConsumableComponents;
import fr.hugman.culinaire.component.CulinaireFoodComponents;
import fr.hugman.culinaire.component.TeaTypesComponent;
import fr.hugman.culinaire.config.CulinaireConfig;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CulinaireItems {
    // VEGETABLES
    public static final Item LETTUCE = register("lettuce", new Item.Settings().food(CulinaireFoodComponents.LETTUCE));
    public static final Item LETTUCE_SEEDS = register("lettuce_seeds", blockItemWithUniqueName(CulinaireBlocks.LETTUCE));
    public static final Item TOMATO = register("tomato", blockItemWithUniqueName(CulinaireBlocks.TOMATOES), new Item.Settings().food(CulinaireFoodComponents.TOMATO));

    // DIARIES
    public static final Item MILK_BOTTLE = register("milk_bottle", ItemSettings.drink(CulinaireFoodComponents.MILK_BOTTLE, ConsumableComponents.MILK_BUCKET).maxCount(CulinaireConfig.get().milkBottlesMaxCount()));

    public static final Item CHEESE = register("cheese", new Item.Settings().food(CulinaireFoodComponents.CHEESE));

    // CANDIES
    public static final Item DARK_CHOCOLATE_BOTTLE = register("dark_chocolate_bottle", ItemSettings.drink(CulinaireFoodComponents.CHOCOLATE_BOTTLE).maxCount(16));
    public static final Item MILK_CHOCOLATE_BOTTLE = register("milk_chocolate_bottle", ItemSettings.drink(CulinaireFoodComponents.CHOCOLATE_BOTTLE).maxCount(16));
    public static final Item WHITE_CHOCOLATE_BOTTLE = register("white_chocolate_bottle", ItemSettings.drink(CulinaireFoodComponents.CHOCOLATE_BOTTLE).maxCount(16));

    public static final Item DARK_CHOCOLATE_BAR = register("dark_chocolate_bar", new Item.Settings().food(CulinaireFoodComponents.CHOCOLATE_BAR));
    public static final Item MILK_CHOCOLATE_BAR = register("milk_chocolate_bar", new Item.Settings().food(CulinaireFoodComponents.CHOCOLATE_BAR));
    public static final Item WHITE_CHOCOLATE_BAR = register("white_chocolate_bar", new Item.Settings().food(CulinaireFoodComponents.CHOCOLATE_BAR));

    public static final Item DARK_CHOCOLATE_PIE = register("dark_chocolate_pie", new Item.Settings().food(CulinaireFoodComponents.CHOCOLATE_PIE));
    public static final Item MILK_CHOCOLATE_PIE = register("milk_chocolate_pie", new Item.Settings().food(CulinaireFoodComponents.CHOCOLATE_PIE));
    public static final Item WHITE_CHOCOLATE_PIE = register("white_chocolate_pie", new Item.Settings().food(CulinaireFoodComponents.CHOCOLATE_PIE));

    public static final Item MARSHMALLOW = register("marshmallow", new Item.Settings().food(CulinaireFoodComponents.MARSHMALLOW, CulinaireConsumableComponents.SNACK_FOOD));
    public static final Item BURNT_MARSHMALLOW_ON_A_STICK = register("burnt_marshmallow_on_a_stick", BurnableItem::new, ItemSettings.burnableFoodOnStick(CulinaireFoodComponents.BURNT_MARSHMALLOW, CulinaireConsumableComponents.BURNT_SNACK_FOOD, 60, Items.STICK));
    public static final Item GOLDEN_MARSHMALLOW_ON_A_STICK = register("golden_marshmallow_on_a_stick", BurnableItem::new, ItemSettings.burnableFoodOnStick(CulinaireFoodComponents.GOLDEN_MARSHMALLOW, CulinaireConsumableComponents.SNACK_FOOD, 20, BURNT_MARSHMALLOW_ON_A_STICK));
    public static final Item TOASTY_MARSHMALLOW_ON_A_STICK = register("toasty_marshmallow_on_a_stick", BurnableItem::new, ItemSettings.burnableFoodOnStick(CulinaireFoodComponents.TOASTY_MARSHMALLOW, CulinaireConsumableComponents.SNACK_FOOD, 75, GOLDEN_MARSHMALLOW_ON_A_STICK));
    public static final Item MARSHMALLOW_ON_A_STICK = register("marshmallow_on_a_stick", BurnableItem::new, ItemSettings.burnableFoodOnStick(CulinaireFoodComponents.MARSHMALLOW, CulinaireConsumableComponents.SNACK_FOOD, 150, TOASTY_MARSHMALLOW_ON_A_STICK));

    // TEA
    public static final Item TEA_BAG = register("tea_bag", new Item.Settings().component(CulinaireComponentTypes.TEA_TYPES, TeaTypesComponent.DEFAULT).maxCount(16));
    public static final Item TEA_BOTTLE = register("tea_bottle", new Item.Settings().component(CulinaireComponentTypes.TEA_TYPES, TeaTypesComponent.DEFAULT).maxCount(1).recipeRemainder(Items.GLASS_BOTTLE).useRemainder(Items.GLASS_BOTTLE).component(DataComponentTypes.CONSUMABLE, CulinaireConsumableComponents.TEA));

    // PASTRIES
    public static final Item CROISSANT = register("croissant", new Item.Settings().food(CulinaireFoodComponents.CROISSANT, CulinaireConsumableComponents.SNACK_FOOD));
    public static final Item CHOUQUETTE = register("chouquette", new Item.Settings().food(CulinaireFoodComponents.CHOUQUETTE, CulinaireConsumableComponents.SNACK_FOOD));

    public static final Item APPLE_PIE = register("apple_pie", new Item.Settings().food(CulinaireFoodComponents.APPLE_PIE));
    public static final Item SWEET_BERRY_PIE = register("sweet_berry_pie", new Item.Settings().food(CulinaireFoodComponents.SWEET_BERRY_PIE));

    // SANDWICHES
    public static final Item SANDWICH = register("sandwich", new Item.Settings().food(CulinaireFoodComponents.SANDWICH).maxCount(1));

    // MEALS
    public static final Item SALAD = register("salad", ItemSettings.stew(CulinaireFoodComponents.SALAD));
    public static final Item MASHED_POTATOES = register("mashed_potatoes", ItemSettings.stew(CulinaireFoodComponents.MASHED_POTATOES));


    private static Function<Item.Settings, Item> blockItemWithUniqueName(Block block) {
        return settings -> new BlockItem(block, settings.useItemPrefixedTranslationKey());
    }

    private static RegistryKey<Item> keyOf(String path) {
        return RegistryKey.of(RegistryKeys.ITEM, Culinaire.id(path));
    }

    private static RegistryKey<Item> keyOf(RegistryKey<Block> blockKey) {
        return RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());
    }

    public static <O extends Item> O register(Block block, BiFunction<Block, Item.Settings, O> factory, Item.Settings settings) {
        return register(block, factory, settings, true);
    }

    public static <O extends Item> O register(Block block, BiFunction<Block, Item.Settings, O> factory, Item.Settings settings, boolean useBlockName) {
        return register(keyOf(block.getRegistryEntry().registryKey()), itemSettings -> factory.apply(block, itemSettings), useBlockName ? settings.useBlockPrefixedTranslationKey() : settings);

    }

    public static <O extends Item> O register(RegistryKey<Item> key, Function<Item.Settings, O> factory, Item.Settings settings) {
        O item = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.ITEM, key, item);
    }

    public static <O extends Item> O register(String id, Function<Item.Settings, O> factory, Item.Settings settings) {
        return register(keyOf(id), factory, settings);
    }

    public static <O extends Item> O register(String id, Function<Item.Settings, O> factory) {
        return register(keyOf(id), factory, new Item.Settings());
    }

    public static Item register(String id, Item.Settings settings) {
        return register(keyOf(id), Item::new, settings);
    }
}
