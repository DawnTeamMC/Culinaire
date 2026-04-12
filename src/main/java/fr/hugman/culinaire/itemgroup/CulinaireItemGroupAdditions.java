package fr.hugman.culinaire.itemgroup;

import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.TeaTypesComponent;
import fr.hugman.culinaire.item.CulinaireItems;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import fr.hugman.culinaire.tea.TeaType;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import java.util.stream.Stream;

import static fr.hugman.culinaire.block.CulinaireBlocks.CHEESE_WHEEL;
import static fr.hugman.culinaire.block.CulinaireBlocks.KETTLE;
import static fr.hugman.culinaire.item.CulinaireItems.*;

public class CulinaireItemGroupAdditions {
    public static void registerEvents() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.addBefore(Items.CARROT, CulinaireItems.LETTUCE, TOMATO);

            entries.addAfter(Items.MILK_BUCKET, MILK_BOTTLE, CHEESE, CHEESE_WHEEL);

            entries.addBefore(Items.COOKIE,
                    DARK_CHOCOLATE_BOTTLE, MILK_CHOCOLATE_BOTTLE, WHITE_CHOCOLATE_BOTTLE,
                    DARK_CHOCOLATE_BAR, MILK_CHOCOLATE_BAR, WHITE_CHOCOLATE_BAR,
                    DARK_CHOCOLATE_PIE, MILK_CHOCOLATE_PIE, WHITE_CHOCOLATE_PIE,
                    MARSHMALLOW, MARSHMALLOW_ON_A_STICK, TOASTY_MARSHMALLOW_ON_A_STICK, GOLDEN_MARSHMALLOW_ON_A_STICK, BURNT_MARSHMALLOW_ON_A_STICK
            );

            entries.addBefore(Items.MUSHROOM_STEW, SALAD, MASHED_POTATOES);

            //TODO: add sandwich

            entries.addAfter(Items.BREAD, CROISSANT, CHOUQUETTE);

            entries.addBefore(Items.PUMPKIN_PIE, APPLE_PIE, SWEET_BERRY_PIE);

            entries.getContext().holders()
                    .lookup(CulinaireRegistryKeys.TEA_TYPE)
                    .ifPresent(
                            registryWrapper -> entries.addAfter(Items.HONEY_BOTTLE,
                                    Stream.concat(getTeaStacks(TEA_BAG, registryWrapper), getTeaStacks(TEA_BOTTLE, registryWrapper)).toArray(ItemStack[]::new)
                            )
                    );
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> entries.addBefore(Items.BEETROOT_SEEDS, LETTUCE_SEEDS));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> entries.addAfter(Blocks.BLAST_FURNACE, KETTLE));

    }

    private static Stream<ItemStack> getTeaStacks(Item item, HolderLookup.RegistryLookup<TeaType> registryWrapper) {
        return registryWrapper.listElements().map(
                teaTypeEntry -> {
                    ItemStack itemStack = new ItemStack(item);
                    itemStack.set(CulinaireComponentTypes.TEA_TYPES, TeaTypesComponent.builder().add(teaTypeEntry, 1).build());
                    return itemStack;
                }
        );
        //TODO: sort by tag order
    }
}
