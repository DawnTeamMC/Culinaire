package hugman.common_expansion.objects.item;

import com.sun.istack.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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

	@Override
	public ItemStack finishUsing(ItemStack sandwich, World world, LivingEntity user)
	{
		List<Item> ingredientList = getIngredients(sandwich);
		if (ingredientList != null && user instanceof PlayerEntity)
		{
			for (Item ingredient : ingredientList)
			{
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
		List<Item> ingredientList = getIngredients(sandwich);
		if (ingredientList != null)
		{
			for (Item ingredient : ingredientList)
			{
				tooltip.add((new LiteralText("- ")).append(new TranslatableText(ingredient.getTranslationKey())).formatted(Formatting.GRAY));
			}
		}
	}

	@Override
	public boolean hasEnchantmentGlint(ItemStack sandwich)
	{
		List<Item> ingredientList = getIngredients(sandwich);
		if (ingredientList != null)
		{
			for (Item ingredient : ingredientList)
			{
				if (ingredient.hasEnchantmentGlint(sandwich))
				{
					return true;
				}
			}
		}
		return false;
	}

	public static void addIngredient(ItemStack sandwich, Item item)
	{
		CompoundTag compoundTag = sandwich.getOrCreateTag();
		ListTag listTag = compoundTag.getList("Ingredients", 9);
		CompoundTag compoundTag2 = new CompoundTag();
		compoundTag2.putString("Item", Registry.ITEM.getId(item).toString());
		listTag.add(compoundTag2);
		compoundTag.put("Ingredients", listTag);
	}

	@Nullable
	public List<Item> getIngredients(ItemStack sandwich)
	{
		CompoundTag compoundTag = sandwich.getTag();
		if (compoundTag != null && compoundTag.contains("Ingredients", 9))
		{
			ListTag listTag = compoundTag.getList("Ingredients", 10);
			List<Item> ingredientList = new ArrayList<>();
			for (int i = 0; i < listTag.size(); ++i)
			{
				CompoundTag compoundTag2 = listTag.getCompound(i);
				Item ingredientItem = Registry.ITEM.get(new Identifier(compoundTag2.getString("Item")));
				ingredientList.add(ingredientItem);
			}
			return ingredientList;
		}
		return null;
	}
}
