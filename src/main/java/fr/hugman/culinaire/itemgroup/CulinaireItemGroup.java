package fr.hugman.culinaire.itemgroup;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import net.minecraft.world.item.Items;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;

public final class CulinaireItemGroup {
    public static void fill(CreativeModeTab.ItemDisplayParameters displayContext, CreativeModeTab.Output entries) {
        Set<ItemStack> set = ItemStackLinkedSet.createTypeAndComponentsSet();

        for (CreativeModeTab itemGroup : BuiltInRegistries.CREATIVE_MODE_TAB) {
            if (itemGroup.getType() != CreativeModeTab.Type.SEARCH) {
                for (var stack : itemGroup.getSearchTabDisplayItems()) {
                    if (isCulinaire(BuiltInRegistries.ITEM.wrapAsHolder(stack.getItem()))) {
                        set.add(stack);
                    }
                }
            }
        }

        entries.acceptAll(set);

        // Paintings
        displayContext.holders()
                .lookup(Registries.PAINTING_VARIANT)
                .ifPresent(
                        registryWrapper -> addPaintings(
                                entries,
                                registryWrapper,
                                CulinaireItemGroup::isCulinaire,
                                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                        )
                );
    }

    private static boolean isCulinaire(Holder<?> entry) {
        return isCulinaire(entry.unwrapKey().orElseThrow());
    }


    private static boolean isCulinaire(ResourceKey<?> key) {
        return key.identifier().getNamespace().equals(Culinaire.MOD_ID);
    }

    // FROM Vanilla ItemGroups

    private static final Comparator<Holder<PaintingVariant>> PAINTING_VARIANT_COMPARATOR = Comparator.comparing(
            Holder::value, Comparator.comparingInt(PaintingVariant::area).thenComparing(PaintingVariant::width)
    );

    private static void addPaintings(
            CreativeModeTab.Output entries,
            HolderLookup.RegistryLookup<PaintingVariant> registryWrapper,
            Predicate<Holder<PaintingVariant>> filter,
            CreativeModeTab.TabVisibility stackVisibility
    ) {
        registryWrapper.listElements().filter(filter).sorted(PAINTING_VARIANT_COMPARATOR).forEach(reference -> {
            ItemStack itemStack = new ItemStack(Items.PAINTING);
            itemStack.set(DataComponents.PAINTING_VARIANT, reference);
            entries.accept(itemStack, stackVisibility);
        });
    }
}
