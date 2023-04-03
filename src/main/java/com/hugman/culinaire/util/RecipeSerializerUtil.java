package com.hugman.culinaire.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class RecipeSerializerUtil {
    public static Ingredient ingredientListOrObject(JsonObject json, String name) {
        return JsonHelper.hasArray(json, name) ? Ingredient.fromJson(JsonHelper.getArray(json, name)) : Ingredient.fromJson(JsonHelper.getObject(json, name));
    }

    public static DefaultedList<Ingredient> ingredientListFromJson(JsonObject json, String name) {
        JsonArray array = JsonHelper.getArray(json, "ingredients");
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        for (int i = 0; i < array.size(); ++i) {
            Ingredient ingredient = Ingredient.fromJson(array.get(i));
            if (ingredient.isEmpty()) continue;
            defaultedList.add(ingredient);
        }
        return defaultedList;
    }

    public static DefaultedList<Ingredient> ingredientListFromBuffer(PacketByteBuf buf) {
        int ingredientAmount = buf.readVarInt();
        DefaultedList<Ingredient> list = DefaultedList.ofSize(ingredientAmount, Ingredient.EMPTY);
        list.replaceAll(ignored -> Ingredient.fromPacket(buf));
        return list;
    }

    public static void ingredientListToBuffer(PacketByteBuf buf, DefaultedList<Ingredient> list) {
        buf.writeVarInt(list.size());
        for (Ingredient ingredient : list) {
            ingredient.write(buf);
        }
    }
}
