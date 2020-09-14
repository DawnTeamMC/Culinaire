package hugman.ce_foodstuffs.objects.item;

import hugman.ce_foodstuffs.CEFoodstuffs;
import hugman.ce_foodstuffs.objects.item.tea.TeaType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TeaBottleItem extends Item {
	public TeaBottleItem(Settings settings) {
		super(settings);
	}

	public static List<TeaType> getTeaTypes(ItemStack stack) {
		List<TeaType> teaTypes = new ArrayList<>();
		CompoundTag compoundTag = stack.getTag();
		if(compoundTag != null) {
			if(compoundTag.contains("TeaTypes")) {
				ListTag listTag = compoundTag.getList("TeaTypes", 10);
				if(!listTag.isEmpty()) {
					for(int i = 0; i < listTag.size(); ++i) {
						CompoundTag typeTag = listTag.getCompound(i);
						TeaType teaType = new TeaType(typeTag.getString("Strength"), typeTag.getString("Flavor"));
						if(teaType.isCorrect()) {
							teaTypes.add(teaType);
						}
					}
				}
			}
		}
		return teaTypes;
	}

	@Environment(EnvType.CLIENT)
	public static void appendTeaTooltip(ItemStack stack, List<Text> tooltip) {
		List<TeaType> teaTypes = getTeaTypes(stack);
		if(!teaTypes.isEmpty()) {
			MutableText text = (new LiteralText("")).formatted(Formatting.GRAY);
			for(int i = 0; i < teaTypes.size(); ++i) {
				TeaType teaType = teaTypes.get(i);
				if(i > 0) {
					text.append(", ");
				}
				text.append(new TranslatableText("tea_type." + CEFoodstuffs.MOD_ID + "." + teaType.getFlavor().getName() + "." + teaType.getStrength().getName()));
			}
			tooltip.add(text);
		}
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
		if(user instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) user;
			Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
			serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
		}
		if(!world.isClient) {
			//TODO manage effects here
			//example: user.removeStatusEffect(StatusEffects.POISON);
		}
		if(playerEntity != null) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			if(!playerEntity.abilities.creativeMode) {
				stack.decrement(1);
			}
		}
		if(playerEntity == null || !playerEntity.abilities.creativeMode) {
			if(stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}
			if(playerEntity != null) {
				playerEntity.inventory.insertStack(new ItemStack(Items.GLASS_BOTTLE));
			}
		}
		return stack;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 60;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		appendTeaTooltip(stack, tooltip);
	}
}