package com.hugman.culinaire.objects.recipe.serializer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hugman.culinaire.objects.recipe.TeaRecipe;
import com.hugman.culinaire.util.RecipeSerializerUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class TeaRecipeSerializer implements RecipeSerializer<TeaRecipe> {
	@Override
	public TeaRecipe read(Identifier id, JsonObject json) {
		DefaultedList<Ingredient> defaultedList = RecipeSerializerUtil.ingredientListFromJson(json, "ingredients");
		if (defaultedList.isEmpty()) {
			throw new JsonParseException("No base ingredients for tea recipe");
		}
		if (defaultedList.size() > 8) {
			throw new JsonParseException("Too many base ingredients for tea recipe");
		}
		int resultCount = JsonHelper.getInt(json, "count", 1);
		ItemStack resultStack = new ItemStack(Registry.ITEM.get(new Identifier(JsonHelper.getString(json, "result"))), resultCount);

		return new TeaRecipe(id, defaultedList, resultStack);
	}

	@Override
	public TeaRecipe read(Identifier id, PacketByteBuf buf) {
		DefaultedList<Ingredient> defaultedList = RecipeSerializerUtil.ingredientListFromBuffer(buf);
		ItemStack resultStack = buf.readItemStack();

		return new TeaRecipe(id, defaultedList, resultStack);
	}

	@Override
	public void write(PacketByteBuf buf, TeaRecipe recipe) {
		RecipeSerializerUtil.ingredientListToBuffer(buf, recipe.baseIngredients);
		buf.writeItemStack(recipe.resultStack);
	}
}
