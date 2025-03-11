package fr.hugman.culinaire.tea;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;

import java.util.ArrayList;
import java.util.List;

public class TeaHelper {
    public static List<TeaType> getIngredientTypes(RegistryEntryLookup.RegistryLookup lookup, ItemStack stack) {
        List<TeaType> teaTypes = new ArrayList<>();
        for (TeaType teaType : getAllTypes()) {
            var tag = lookup.getOrThrow(RegistryKeys.ITEM).getOptional(teaType.getTagKey());
            tag.ifPresent(t -> {
                if (Ingredient.fromTag(t).test(stack)) {
                    teaTypes.add(teaType);
                }
            });
        }
        return teaTypes;
    }

    public static List<TeaType> getAllTypes() {
        List<TeaType> teaTypes = new ArrayList<>();
        for (TeaType.Flavor flavor : TeaType.Flavor.values()) {
            for (TeaType.Strength strength : TeaType.Strength.values()) {
                teaTypes.add(new TeaType(strength, flavor));
            }
        }
        return teaTypes;
    }

    public static int getColor(List<TeaType> teaTypes) {
        if (teaTypes.isEmpty()) {
            return 15112486;
        } else {
            float f = 0.0F;
            float g = 0.0F;
            float h = 0.0F;
            int j = 0;
            for (TeaType teaType : teaTypes) {
                int k = teaType.getFlavor().getColor();
                int l = teaType.getStrength().getPotency() + 1;
                f += (float) (l * (k >> 16 & 255)) / 255.0F;
                g += (float) (l * (k >> 8 & 255)) / 255.0F;
                h += (float) (l * (k & 255)) / 255.0F;
                j += l;
            }
            if (j == 0) {
                return 0;
            } else {
                f = f / (float) j * 255.0F;
                g = g / (float) j * 255.0F;
                h = h / (float) j * 255.0F;
                return (int) f << 16 | (int) g << 8 | (int) h;
            }
        }
    }
}
