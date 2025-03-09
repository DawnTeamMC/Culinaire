package fr.hugman.culinaire.loot;

import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.RandomChanceWithEnchantedBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class CulinaireLootTables {
    private static final Identifier ZOMBIE_ENTITY = Identifier.ofVanilla("entities/zombie");
    private static final Identifier ZOMBIE_VILLAGER_ENTITY = Identifier.ofVanilla("entities/zombie_villager");
    private static final Identifier HUSK_ENTITY = Identifier.ofVanilla("entities/husk");

    public static void addToVanillaTables() {
        LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
            // Lettuce Seeds
            if (LootTables.SIMPLE_DUNGEON_CHEST.equals(key) || LootTables.ABANDONED_MINESHAFT_CHEST.equals(key) || LootTables.WOODLAND_MANSION_CHEST.equals(key)) {
                LootPool.Builder pool = LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0F, 3.0F))
                        .conditionally(RandomChanceLootCondition.builder(0.3F))
                        .with(ItemEntry.builder(CulinaireItems.LETTUCE_SEEDS).weight(10).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))));
                builder.pool(pool);
            }

            // Tomato
            if (LootTables.PILLAGER_OUTPOST_CHEST.equals(key)) {
                LootPool.Builder pool = LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(2.0F, 3.0F))
                        .conditionally(RandomChanceLootCondition.builder(0.45F))
                        .with(ItemEntry.builder(CulinaireItems.TOMATO).weight(5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 5.0F))));
                builder.pool(pool);
            }
            if (LootTables.SHIPWRECK_SUPPLY_CHEST.equals(key)) {
                LootPool.Builder pool = LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0F, 3.0F))
                        .conditionally(RandomChanceLootCondition.builder(0.45F))
                        .with(ItemEntry.builder(CulinaireItems.TOMATO).weight(7).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 6.0F))));
                builder.pool(pool);
            }
            if (LootTables.VILLAGE_TAIGA_HOUSE_CHEST.equals(key) || LootTables.VILLAGE_SNOWY_HOUSE_CHEST.equals(key) || LootTables.VILLAGE_PLAINS_CHEST.equals(key)) {
                LootPool.Builder pool = LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0F, 3.0F))
                        .conditionally(RandomChanceLootCondition.builder(0.25F))
                        .with(ItemEntry.builder(CulinaireItems.TOMATO).weight(10).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))));
                builder.pool(pool);
            }
            if (ZOMBIE_ENTITY.equals(key) || ZOMBIE_VILLAGER_ENTITY.equals(key) || HUSK_ENTITY.equals(key)) {
                LootPool.Builder pool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.025f, 0.01f))
                        .with(ItemEntry.builder(CulinaireItems.TOMATO));
                builder.pool(pool);
            }
        });
    }
}
