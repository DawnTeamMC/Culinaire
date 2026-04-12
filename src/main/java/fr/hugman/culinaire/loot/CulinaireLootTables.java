package fr.hugman.culinaire.loot;

import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class CulinaireLootTables {
    private static final Identifier ZOMBIE_ENTITY = Identifier.withDefaultNamespace("entities/zombie");
    private static final Identifier ZOMBIE_VILLAGER_ENTITY = Identifier.withDefaultNamespace("entities/zombie_villager");
    private static final Identifier HUSK_ENTITY = Identifier.withDefaultNamespace("entities/husk");

    public static void addToVanillaTables() {
        LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
            // Lettuce Seeds
            if (BuiltInLootTables.SIMPLE_DUNGEON.equals(key) || BuiltInLootTables.ABANDONED_MINESHAFT.equals(key) || BuiltInLootTables.WOODLAND_MANSION.equals(key)) {
                LootPool.Builder pool = LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1.0F, 3.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.3F))
                        .add(LootItem.lootTableItem(CulinaireItems.LETTUCE_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))));
                builder.withPool(pool);
            }

            // Tomato
            if (BuiltInLootTables.PILLAGER_OUTPOST.equals(key)) {
                LootPool.Builder pool = LootPool.lootPool()
                        .setRolls(UniformGenerator.between(2.0F, 3.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.45F))
                        .add(LootItem.lootTableItem(CulinaireItems.TOMATO).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))));
                builder.withPool(pool);
            }
            if (BuiltInLootTables.SHIPWRECK_SUPPLY.equals(key)) {
                LootPool.Builder pool = LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1.0F, 3.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.45F))
                        .add(LootItem.lootTableItem(CulinaireItems.TOMATO).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))));
                builder.withPool(pool);
            }
            if (BuiltInLootTables.VILLAGE_TAIGA_HOUSE.equals(key) || BuiltInLootTables.VILLAGE_SNOWY_HOUSE.equals(key) || BuiltInLootTables.VILLAGE_PLAINS_HOUSE.equals(key)) {
                LootPool.Builder pool = LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1.0F, 3.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.25F))
                        .add(LootItem.lootTableItem(CulinaireItems.TOMATO).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))));
                builder.withPool(pool);
            }
            if (ZOMBIE_ENTITY.equals(key) || ZOMBIE_VILLAGER_ENTITY.equals(key) || HUSK_ENTITY.equals(key)) {
                LootPool.Builder pool = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, 0.025f, 0.01f))
                        .add(LootItem.lootTableItem(CulinaireItems.TOMATO));
                builder.withPool(pool);
            }
        });
    }
}
