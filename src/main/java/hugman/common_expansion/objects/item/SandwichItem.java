package hugman.common_expansion.objects.item;

import com.sun.istack.internal.Nullable;
import hugman.common_expansion.init.CEItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SandwichItem extends Item
{
	public SandwichItem(Settings settings)
	{
		super(settings);
	}

	public void addIngredient(ItemStack sandwich, Item item)
	{
		CompoundTag compoundTag = sandwich.getOrCreateTag();
		for (int index = 1; index < 3; index++)
		{
			Item ingredient = getIngredient(sandwich, index);
			if (ingredient == null)
			{
				compoundTag.putString("Ingredient" + index, Registry.ITEM.getId(item).toString());
			}
		}
	}

	@Nullable
	public Item getIngredient(ItemStack sandwich, int index)
	{
		CompoundTag compoundTag = sandwich.getTag();
		if (compoundTag != null && compoundTag.contains("Ingredient" + index))
		{
			return Registry.ITEM.get(new Identifier(compoundTag.getString("Ingredient" + index)));
		}
		return null;
	}

	public List<Pair<Item, Integer>> getIngredients(ItemStack sandwich)
	{
		List<Pair<Item, Integer>> ingredientList = new ArrayList<>();
		for (int i = 1; i < 3; i++)
		{
			Item ingredient = getIngredient(sandwich, i);
			if (ingredient != null)
			{
				ingredientList.add(Pair.of(ingredient, i));
			}
		}
		return ingredientList;
	}

	public List<Pair<Integer, Integer>> getComplements(ItemStack sandwich)
	{
		List<Pair<Integer, Integer>> complements = new ArrayList<>();
		List<Pair<Item, Integer>> ingredientList = getIngredients(sandwich);
		for (Pair pair : getComplementaryIngredients())
		{
			if (ingredientList.size() == 2)
			{
				if (pair.equals(Pair.of(ingredientList.get(0), ingredientList.get(1))) || pair.equals(Pair.of(ingredientList.get(1), ingredientList.get(0))))
				{
					complements.add(Pair.of(1, 2));
				}
			}
			if (ingredientList.size() == 3)
			{
				if (pair.equals(Pair.of(ingredientList.get(0), ingredientList.get(2))) || pair.equals(Pair.of(ingredientList.get(2), ingredientList.get(0))))
				{
					complements.add(Pair.of(1, 3));
				}
				if (pair.equals(Pair.of(ingredientList.get(1), ingredientList.get(2))) || pair.equals(Pair.of(ingredientList.get(2), ingredientList.get(1))))
				{
					complements.add(Pair.of(2, 3));
				}
			}
		}
		return complements;
	}

	public static List<Pair<Item, Item>> getComplementaryIngredients()
	{
		List<Pair<Item, Item>> list = new ArrayList<>();
		list.add(Pair.of(Items.APPLE, CEItems.CHOCOLATE));
		list.add(Pair.of(Items.CHICKEN, Items.HONEY_BOTTLE));
		list.add(Pair.of(Items.COOKED_BEEF, CEItems.CHEESE));
		list.add(Pair.of(Items.ENCHANTED_GOLDEN_APPLE, Items.BEETROOT));
		list.add(Pair.of(Items.GOLDEN_APPLE, Items.DRIED_KELP));
		list.add(Pair.of(CEItems.MARSHMALLOW, CEItems.CHOCOLATE));
		list.add(Pair.of(CEItems.MARSHMALLOW, Items.HONEY_BOTTLE));
		list.add(Pair.of(Items.RABBIT, Items.BEETROOT));
		list.add(Pair.of(Items.SPIDER_EYE, CEItems.CHOCOLATE));
		list.add(Pair.of(CEItems.TOMATO, CEItems.CHEESE));
		list.add(Pair.of(CEItems.TOMATO, CEItems.LETTUCE));
		return list;
	}

	@Override
	public ItemStack finishUsing(ItemStack sandwich, World world, LivingEntity user)
	{
		List<Pair<Item, Integer>> ingredientList = getIngredients(sandwich);
		if (!ingredientList.isEmpty() && user instanceof PlayerEntity)
		{
			for (Pair<Item, Integer> ingredientEntry : ingredientList)
			{
				Item ingredient = ingredientEntry.getLeft();
				if (ingredient.isFood())
				{
					PlayerEntity player = (PlayerEntity) user;
					FoodComponent foodComponent = ingredient.getFoodComponent();
					int providedHunger = (int) Math.ceil(foodComponent.getHunger() / 3);
					int providedSaturation = (int) Math.ceil(foodComponent.getSaturationModifier() / 3);
					player.getHungerManager().add(providedHunger, providedSaturation);
					List<Pair<StatusEffectInstance, Float>> list = foodComponent.getStatusEffects();
					Iterator effects = list.iterator();
					while (effects.hasNext())
					{
						Pair<StatusEffectInstance, Float> pair = (Pair) effects.next();
						if (!world.isClient && pair.getLeft() != null && world.random.nextFloat() < pair.getRight())
						{
							player.addStatusEffect(new StatusEffectInstance(pair.getLeft()));
						}
					}
				}
			}
		}
		return super.finishUsing(sandwich, world, user);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack sandwich, @Nullable World world, List<Text> tooltip, TooltipContext context)
	{
		List<Pair<Item, Integer>> ingredientList = getIngredients(sandwich);
		if (ingredientList != null)
		{
			for (Pair<Item, Integer> ingredientEntry : ingredientList)
			{
				Item ingredient = ingredientEntry.getLeft();
				tooltip.add((new LiteralText("- ")).append(new TranslatableText(ingredient.getTranslationKey())).formatted(Formatting.GRAY));
			}
		}
	}

	@Override
	public boolean hasEnchantmentGlint(ItemStack sandwich)
	{
		List<Pair<Item, Integer>> ingredientList = getIngredients(sandwich);
		if (ingredientList != null)
		{
			for (Pair<Item, Integer> ingredientEntry : ingredientList)
			{
				Item ingredient = ingredientEntry.getLeft();
				if (ingredient.hasEnchantmentGlint(sandwich))
				{
					return true;
				}
			}
		}
		return false;
	}
}
