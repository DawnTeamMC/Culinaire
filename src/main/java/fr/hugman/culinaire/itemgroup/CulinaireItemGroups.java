package fr.hugman.culinaire.itemgroup;

import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

public class CulinaireItemGroups {
    public static final ItemGroup CULINAIRE = of(CulinaireItemGroupKeys.CULINAIRE, FabricItemGroup.builder()
            .displayName(Text.translatable("item_group.culinaire.culinaire"))
            .icon(() -> new ItemStack(CulinaireItems.SANDWICH))
            .entries(CulinaireItemGroup::fill)
            .build());

    private static ItemGroup of(RegistryKey<ItemGroup> key, ItemGroup itemGroup) {
        return Registry.register(Registries.ITEM_GROUP, key, itemGroup);
    }
}
