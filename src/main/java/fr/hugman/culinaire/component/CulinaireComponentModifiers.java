package fr.hugman.culinaire.component;

import fr.hugman.culinaire.config.CulinaireConfig;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Items;

public final class CulinaireComponentModifiers {
    public static void registerEvents() {
        DefaultItemComponentEvents.MODIFY.register(modifyContext -> modifyContext.modify(Items.MILK_BUCKET, builder -> {
                    if (!CulinaireConfig.get().canDrinkMilkBucket()) {
                        builder.add(DataComponentTypes.CONSUMABLE, null);
                    }
                }
        ));
    }
}
