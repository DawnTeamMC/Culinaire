package fr.hugman.culinaire.item;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.CulinaireConsumableComponents;
import fr.hugman.culinaire.component.CulinaireFoodComponents;
import fr.hugman.culinaire.component.TeaTypesComponent;
import fr.hugman.culinaire.config.CulinaireConfig;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.block.Block;

public class CulinaireItems {
    // VEGETABLES
    public static final Item LETTUCE = register("lettuce", new Item.Properties().food(CulinaireFoodComponents.LETTUCE));
    public static final Item LETTUCE_SEEDS = register("lettuce_seeds", blockItemWithUniqueName(CulinaireBlocks.LETTUCE));
    public static final Item TOMATO = register("tomato", blockItemWithUniqueName(CulinaireBlocks.TOMATOES), new Item.Properties().food(CulinaireFoodComponents.TOMATO));

    // DIARIES
    public static final Item MILK_BOTTLE = register("milk_bottle", ItemSettings.drink(CulinaireFoodComponents.MILK_BOTTLE, Consumables.MILK_BUCKET).stacksTo(CulinaireConfig.get().milkBottlesMaxCount()));

    public static final Item CHEESE = register("cheese", new Item.Properties().food(CulinaireFoodComponents.CHEESE));

    // CANDIES
    public static final Item DARK_CHOCOLATE_BOTTLE = register("dark_chocolate_bottle", ItemSettings.drink(CulinaireFoodComponents.CHOCOLATE_BOTTLE).stacksTo(16));
    public static final Item MILK_CHOCOLATE_BOTTLE = register("milk_chocolate_bottle", ItemSettings.drink(CulinaireFoodComponents.CHOCOLATE_BOTTLE).stacksTo(16));
    public static final Item WHITE_CHOCOLATE_BOTTLE = register("white_chocolate_bottle", ItemSettings.drink(CulinaireFoodComponents.CHOCOLATE_BOTTLE).stacksTo(16));

    public static final Item DARK_CHOCOLATE_BAR = register("dark_chocolate_bar", new Item.Properties().food(CulinaireFoodComponents.CHOCOLATE_BAR));
    public static final Item MILK_CHOCOLATE_BAR = register("milk_chocolate_bar", new Item.Properties().food(CulinaireFoodComponents.CHOCOLATE_BAR));
    public static final Item WHITE_CHOCOLATE_BAR = register("white_chocolate_bar", new Item.Properties().food(CulinaireFoodComponents.CHOCOLATE_BAR));

    public static final Item DARK_CHOCOLATE_PIE = register("dark_chocolate_pie", new Item.Properties().food(CulinaireFoodComponents.CHOCOLATE_PIE));
    public static final Item MILK_CHOCOLATE_PIE = register("milk_chocolate_pie", new Item.Properties().food(CulinaireFoodComponents.CHOCOLATE_PIE));
    public static final Item WHITE_CHOCOLATE_PIE = register("white_chocolate_pie", new Item.Properties().food(CulinaireFoodComponents.CHOCOLATE_PIE));

    public static final Item MARSHMALLOW = register("marshmallow", new Item.Properties().food(CulinaireFoodComponents.MARSHMALLOW, CulinaireConsumableComponents.SNACK_FOOD));
    public static final Item BURNT_MARSHMALLOW_ON_A_STICK = register("burnt_marshmallow_on_a_stick", BurnableItem::new, ItemSettings.burnableFoodOnStick(CulinaireFoodComponents.BURNT_MARSHMALLOW, CulinaireConsumableComponents.BURNT_SNACK_FOOD, 60, Items.STICK));
    public static final Item GOLDEN_MARSHMALLOW_ON_A_STICK = register("golden_marshmallow_on_a_stick", BurnableItem::new, ItemSettings.burnableFoodOnStick(CulinaireFoodComponents.GOLDEN_MARSHMALLOW, CulinaireConsumableComponents.SNACK_FOOD, 20, BURNT_MARSHMALLOW_ON_A_STICK));
    public static final Item TOASTY_MARSHMALLOW_ON_A_STICK = register("toasty_marshmallow_on_a_stick", BurnableItem::new, ItemSettings.burnableFoodOnStick(CulinaireFoodComponents.TOASTY_MARSHMALLOW, CulinaireConsumableComponents.SNACK_FOOD, 75, GOLDEN_MARSHMALLOW_ON_A_STICK));
    public static final Item MARSHMALLOW_ON_A_STICK = register("marshmallow_on_a_stick", BurnableItem::new, ItemSettings.burnableFoodOnStick(CulinaireFoodComponents.MARSHMALLOW, CulinaireConsumableComponents.SNACK_FOOD, 150, TOASTY_MARSHMALLOW_ON_A_STICK));

    // TEA
    public static final Item TEA_BAG = register("tea_bag", TeaItem::new, new Item.Properties().component(CulinaireComponentTypes.TEA_TYPES, TeaTypesComponent.DEFAULT).stacksTo(16));
    public static final Item TEA_BOTTLE = register("tea_bottle", TeaBottleItem::new, new Item.Properties().component(CulinaireComponentTypes.TEA_TYPES, TeaTypesComponent.DEFAULT).stacksTo(1).craftRemainder(Items.GLASS_BOTTLE).usingConvertsTo(Items.GLASS_BOTTLE).component(DataComponents.CONSUMABLE, CulinaireConsumableComponents.TEA));

    // PASTRIES
    public static final Item CROISSANT = register("croissant", new Item.Properties().food(CulinaireFoodComponents.CROISSANT, CulinaireConsumableComponents.SNACK_FOOD));
    public static final Item CHOUQUETTE = register("chouquette", new Item.Properties().food(CulinaireFoodComponents.CHOUQUETTE, CulinaireConsumableComponents.SNACK_FOOD));

    public static final Item APPLE_PIE = register("apple_pie", new Item.Properties().food(CulinaireFoodComponents.APPLE_PIE));
    public static final Item SWEET_BERRY_PIE = register("sweet_berry_pie", new Item.Properties().food(CulinaireFoodComponents.SWEET_BERRY_PIE));

    // SANDWICHES
    public static final Item SANDWICH = register("sandwich", new Item.Properties().food(CulinaireFoodComponents.SANDWICH).stacksTo(1));

    // MEALS
    public static final Item SALAD = register("salad", ItemSettings.stew(CulinaireFoodComponents.SALAD));
    public static final Item MASHED_POTATOES = register("mashed_potatoes", ItemSettings.stew(CulinaireFoodComponents.MASHED_POTATOES));


    private static Function<Item.Properties, Item> blockItemWithUniqueName(Block block) {
        return settings -> new BlockItem(block, settings.useItemDescriptionPrefix());
    }

    private static ResourceKey<Item> keyOf(String path) {
        return ResourceKey.create(Registries.ITEM, Culinaire.id(path));
    }

    private static ResourceKey<Item> keyOf(ResourceKey<Block> blockKey) {
        return ResourceKey.create(Registries.ITEM, blockKey.identifier());
    }

    public static <O extends Item> O register(Block block, BiFunction<Block, Item.Properties, O> factory, Item.Properties settings) {
        return register(block, factory, settings, true);
    }

    public static <O extends Item> O register(Block block, BiFunction<Block, Item.Properties, O> factory, Item.Properties settings, boolean useBlockName) {
        return register(keyOf(block.builtInRegistryHolder().key()), itemSettings -> factory.apply(block, itemSettings), useBlockName ? settings.useBlockDescriptionPrefix() : settings);

    }

    public static <O extends Item> O register(ResourceKey<Item> key, Function<Item.Properties, O> factory, Item.Properties settings) {
        O item = factory.apply(settings.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static <O extends Item> O register(String id, Function<Item.Properties, O> factory, Item.Properties settings) {
        return register(keyOf(id), factory, settings);
    }

    public static <O extends Item> O register(String id, Function<Item.Properties, O> factory) {
        return register(keyOf(id), factory, new Item.Properties());
    }

    public static Item register(String id, Item.Properties settings) {
        return register(keyOf(id), Item::new, settings);
    }
}
