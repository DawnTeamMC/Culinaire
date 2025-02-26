package fr.hugman.culinaire.itemgroup;

import fr.hugman.culinaire.block.CulinaireBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;

import java.util.Collections;
import java.util.function.Predicate;

import static fr.hugman.culinaire.item.CulinaireItems.*;

public class CulinaireItemGroupAdditions {
    public static void appendItemGroups() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
                entries.addBefore(Items.CARROT, LETTUCE, TOMATO);

                entries.addAfter(Items.MILK_BUCKET, MILK_BOTTLE, CHEESE, CulinaireBlocks.CHEESE_WHEEL);

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
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.addBefore(Items.BEETROOT_SEEDS, LETTUCE_SEEDS);
        });
    }

    public static void appendSpawnEgg(Item spawnEgg) {
        var itemGroup = Registries.ITEM_GROUP.get(ItemGroups.SPAWN_EGGS);
        String path = Registries.ITEM.getId(spawnEgg).getPath();

        if (itemGroup == null) {
            return;
        }

        Predicate<ItemStack> predicate = stack1 -> {
            String path1 = Registries.ITEM.getId(stack1.getItem()).getPath();
            for (ItemStack stack2 : itemGroup.getDisplayStacks()) {
                String path2 = Registries.ITEM.getId(stack2.getItem()).getPath();
                if (path1.matches(".*_spawn_egg") && path2.matches(".*_spawn_egg")) {
                    // check if path is lexicographically between path1 and path2
                    if (path.compareTo(path1) > 0 && path.compareTo(path2) < 0) {
                        return true;
                    }
                }
            }
            return false;
        };
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(e -> e.addAfter(predicate, Collections.singleton(new ItemStack(spawnEgg)), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS));
    }
}
