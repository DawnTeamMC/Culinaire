package com.hugman.culinaire.tea;

import com.hugman.culinaire.Culinaire;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeaHelper {
    @Environment(EnvType.CLIENT)
    public static void appendTeaTooltip(List<Text> tooltips, List<TeaType> teaTypes) {
        for (TeaType teaType : teaTypes) {
            tooltips.add(Text.translatable("tea_type." + Culinaire.REGISTRAR.modId() + "." + teaType.getFlavor().getName() + "." + teaType.getStrength().getName()).formatted(Formatting.GRAY));
        }
    }

    public static ItemStack appendTeaType(ItemStack stack, TeaType teaTypes) {
        return appendTeaTypes(stack, new ArrayList<>(Collections.singleton(teaTypes)));
    }

    public static ItemStack appendTeaTypes(ItemStack stack, List<TeaType> teaTypes) {
        NbtList NbtList = new NbtList();
        for (TeaType teaType : teaTypes) {
            NbtCompound typeTag = new NbtCompound();
            typeTag.putString("Flavor", teaType.getFlavor().getName());
            typeTag.putString("Strength", teaType.getStrength().getName());
            NbtList.add(typeTag);
        }
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.put("TeaTypes", NbtList);
        return stack;
    }

    public static List<TeaType> getIngredientTypes(ItemStack stack) {
        List<TeaType> teaTypes = new ArrayList<>();
        for (TeaType teaType : getAllTypes()) {
            if (Ingredient.fromTag(teaType.getTag()).test(stack)) {
                teaTypes.add(teaType);
            }
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

    public static List<TeaType> getTeaTypesByCompound(NbtCompound nbtCompound) {
        List<TeaType> list = new ArrayList<>();
        if (nbtCompound != null) {
            if (nbtCompound.contains("TeaTypes")) {
                NbtList teaTypeList = nbtCompound.getList("TeaTypes", 10);
                if (!teaTypeList.isEmpty()) {
                    for (int i = 0; i < teaTypeList.size(); ++i) {
                        NbtCompound typeTag = teaTypeList.getCompound(i);
                        TeaType teaType = new TeaType(typeTag.getString("Strength"), typeTag.getString("Flavor"));
                        if (teaType.isCorrect()) {
                            list.add(teaType);
                        }
                    }
                }
            }
        }
        return list;
    }

    public static int getColor(ItemStack stack) {
        return getColor(getTeaTypesByCompound(stack.getNbt()));
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
                h += (float) (l * (k >> 0 & 255)) / 255.0F;
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
