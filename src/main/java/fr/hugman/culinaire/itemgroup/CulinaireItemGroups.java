package fr.hugman.culinaire.itemgroup;

import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CulinaireItemGroups {
    public static final CreativeModeTab CULINAIRE = of(CulinaireItemGroupKeys.CULINAIRE, FabricCreativeModeTab.builder()
            .title(Component.translatable("item_group.culinaire.culinaire"))
            .icon(() -> new ItemStack(CulinaireItems.SANDWICH))
            .displayItems(CulinaireItemGroup::fill)
            .build());

    private static CreativeModeTab of(ResourceKey<CreativeModeTab> key, CreativeModeTab itemGroup) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, itemGroup);
    }
}
