package com.hugman.culinaire.objects.recipe.serializer;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hugman.culinaire.objects.recipe.SandwichRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class SandwichRecipeSerializer implements RecipeSerializer<SandwichRecipe> {
	@Override
	public SandwichRecipe read(Identifier id, JsonObject json) {
		int count = JsonHelper.getInt(json, "count", 1);
		ItemStack stack = new ItemStack(Registry.ITEM.get(new Identifier(JsonHelper.getString(json, "result"))), count);
		Ingredient bread = JsonHelper.hasArray(json, "bread") ? Ingredient.fromJson(JsonHelper.getArray(json, "bread")) : Ingredient.fromJson(JsonHelper.getObject(json, "bread"));
		Ingredient ingredientBlacklist = JsonHelper.hasArray(json, "ingredient_blacklist") ? Ingredient.fromJson(JsonHelper.getArray(json, "ingredient_blacklist")) : Ingredient.fromJson(JsonHelper.getObject(json, "ingredient_blacklist"));
		float hungerModifierBase = JsonHelper.getFloat(json, "hunger_modifier_base");
		float hungerModifierBoosted = JsonHelper.getFloat(json, "hunger_modifier_boosted");
		float saturationModifierBase = JsonHelper.getFloat(json, "saturation_modifier_base");
		float saturationModifierBoosted = JsonHelper.getFloat(json, "saturation_modifier_boosted");
		Map<Ingredient, Ingredient> ingredientAssociations = readComplements(JsonHelper.getArray(json, "ingredient_associations", new JsonArray()));

		return new SandwichRecipe(id, stack, bread, ingredientBlacklist, hungerModifierBase, hungerModifierBoosted, saturationModifierBase, saturationModifierBoosted, ingredientAssociations);
	}

	@Override
	public void write(PacketByteBuf buf, SandwichRecipe recipe) {
		buf.writeItemStack(recipe.resultStack);
		recipe.bread.write(buf);
		recipe.ingredientBlacklist.write(buf);
		buf.writeFloat(recipe.hungerModifierBase);
		buf.writeFloat(recipe.hungerModifierBoosted);
		buf.writeFloat(recipe.saturationModifierBase);
		buf.writeFloat(recipe.saturationModifierBoosted);
		buf.writeMap(recipe.ingredientAssociations, (b, i) -> i.write(b), (b, i) -> i.write(b));
	}

	@Override
	public SandwichRecipe read(Identifier id, PacketByteBuf buf) {
		ItemStack stack = buf.readItemStack();
		Ingredient bread = Ingredient.fromPacket(buf);
		Ingredient ingredientBlacklist = Ingredient.fromPacket(buf);
		float hungerModifierBase = buf.readFloat();
		float hungerModifierBoosted = buf.readFloat();
		float saturationModifierBase = buf.readFloat();
		float saturationModifierBoosted  = buf.readFloat();
		Map<Ingredient, Ingredient> ingredientAssociations = buf.readMap(Ingredient::fromPacket, Ingredient::fromPacket);

		return new SandwichRecipe(id, stack, bread, ingredientBlacklist, hungerModifierBase, hungerModifierBoosted, saturationModifierBase, saturationModifierBoosted, ingredientAssociations);
	}

	static Map<Ingredient, Ingredient> readComplements(JsonArray json) {
		HashMap<Ingredient, Ingredient> map = Maps.newHashMap();
		for (int i = 0; i < json.size(); ++i) {
			if(json.get(i).isJsonObject()) {
				JsonObject subJson = json.get(i).getAsJsonObject();
				Ingredient ingredient_a = JsonHelper.hasArray(subJson, "ingredient_a") ? Ingredient.fromJson(JsonHelper.getArray(subJson, "ingredient_a")) : Ingredient.fromJson(JsonHelper.getObject(subJson, "ingredient_a"));
				Ingredient ingredient_b = JsonHelper.hasArray(subJson, "ingredient_b") ? Ingredient.fromJson(JsonHelper.getArray(subJson, "ingredient_b")) : Ingredient.fromJson(JsonHelper.getObject(subJson, "ingredient_b"));
				map.put(ingredient_a, ingredient_b);
			}
		}
		return map;
	}
}
