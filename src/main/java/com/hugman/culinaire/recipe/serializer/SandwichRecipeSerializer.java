package com.hugman.culinaire.recipe.serializer;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hugman.culinaire.recipe.SandwichRecipe;
import com.hugman.culinaire.util.RecipeSerializerUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.HashMap;
import java.util.Map;

public class SandwichRecipeSerializer implements RecipeSerializer<SandwichRecipe> {
    @Override
    public SandwichRecipe read(Identifier id, JsonObject json) {
        CraftingRecipeCategory category = CraftingRecipeCategory.CODEC.byId(JsonHelper.getString(json, "category", null), CraftingRecipeCategory.MISC);
        Ingredient bread = RecipeSerializerUtil.ingredientListOrObject(json, "bread");
        Ingredient ingredientBlacklist = RecipeSerializerUtil.ingredientListOrObject(json, "ingredient_blacklist");
        float hungerModifierBase = JsonHelper.getFloat(json, "hunger_modifier_base");
        float hungerModifierBoosted = JsonHelper.getFloat(json, "hunger_modifier_boosted");
        float saturationModifierBase = JsonHelper.getFloat(json, "saturation_modifier_base");
        float saturationModifierBoosted = JsonHelper.getFloat(json, "saturation_modifier_boosted");
        Map<Ingredient, Ingredient> ingredientAssociations = readComplements(JsonHelper.getArray(json, "ingredient_associations", new JsonArray()));
        int resultCount = JsonHelper.getInt(json, "count", 1);
        ItemStack resultStack = new ItemStack(Registries.ITEM.get(new Identifier(JsonHelper.getString(json, "result"))), resultCount);

        return new SandwichRecipe(id, category, bread, ingredientBlacklist, hungerModifierBase, hungerModifierBoosted, saturationModifierBase, saturationModifierBoosted, ingredientAssociations, resultStack);
    }

    private static Map<Ingredient, Ingredient> readComplements(JsonArray json) {
        HashMap<Ingredient, Ingredient> map = Maps.newHashMap();
        for (int i = 0; i < json.size(); ++i) {
            if (json.get(i).isJsonObject()) {
                JsonObject subJson = json.get(i).getAsJsonObject();
                Ingredient ingredient_a = JsonHelper.hasArray(subJson, "ingredient_a") ? Ingredient.fromJson(JsonHelper.getArray(subJson, "ingredient_a")) : Ingredient.fromJson(JsonHelper.getObject(subJson, "ingredient_a"));
                Ingredient ingredient_b = JsonHelper.hasArray(subJson, "ingredient_b") ? Ingredient.fromJson(JsonHelper.getArray(subJson, "ingredient_b")) : Ingredient.fromJson(JsonHelper.getObject(subJson, "ingredient_b"));
                map.put(ingredient_a, ingredient_b);
            }
        }
        return map;
    }

    @Override
    public SandwichRecipe read(Identifier id, PacketByteBuf buf) {
        CraftingRecipeCategory category = buf.readEnumConstant(CraftingRecipeCategory.class);
        Ingredient bread = Ingredient.fromPacket(buf);
        Ingredient ingredientBlacklist = Ingredient.fromPacket(buf);
        float hungerModifierBase = buf.readFloat();
        float hungerModifierBoosted = buf.readFloat();
        float saturationModifierBase = buf.readFloat();
        float saturationModifierBoosted = buf.readFloat();
        Map<Ingredient, Ingredient> ingredientAssociations = buf.readMap(Ingredient::fromPacket, Ingredient::fromPacket);
        ItemStack resultStack = buf.readItemStack();

        return new SandwichRecipe(id, category, bread, ingredientBlacklist, hungerModifierBase, hungerModifierBoosted, saturationModifierBase, saturationModifierBoosted, ingredientAssociations, resultStack);
    }

    @Override
    public void write(PacketByteBuf buf, SandwichRecipe recipe) {
        recipe.bread.write(buf);
        recipe.ingredientBlacklist.write(buf);
        buf.writeFloat(recipe.hungerModifierBase);
        buf.writeFloat(recipe.hungerModifierBoosted);
        buf.writeFloat(recipe.saturationModifierBase);
        buf.writeFloat(recipe.saturationModifierBoosted);
        buf.writeMap(recipe.ingredientAssociations, (b, i) -> i.write(b), (b, i) -> i.write(b));
        buf.writeItemStack(recipe.resultItem);
    }
}
