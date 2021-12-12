package com.hugman.culinaire.objects.item;

import com.hugman.culinaire.objects.item.tea.TeaEffect;
import com.hugman.culinaire.objects.item.tea.TeaHelper;
import com.hugman.culinaire.objects.item.tea.TeaType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class TeaBottleItem extends Item {
	public TeaBottleItem(Settings settings) {
		super(settings);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		TeaHelper.appendTeaTooltip(tooltip, TeaHelper.getTeaTypesByCompound(stack.getNbt()));
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if(group == ItemGroup.SEARCH) {
			for(TeaType teaType : TeaHelper.getAllTypes()) {
				stacks.add(TeaHelper.appendTeaType(new ItemStack(this), teaType));
			}
		}
		else if(this.isIn(group)) {
			for(TeaType.Flavor flavor : TeaType.Flavor.values()) {
				stacks.add(TeaHelper.appendTeaType(new ItemStack(this), new TeaType(TeaType.Strength.NORMAL, flavor)));
			}
		}
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 46;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
		if(user instanceof ServerPlayerEntity serverPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
			serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
		}
		if(!world.isClient) {
			List<TeaType> teaTypes = TeaHelper.getTeaTypesByCompound(stack.getNbt());
			if(!teaTypes.isEmpty()) {
				for(TeaType teaType : teaTypes) {
					teaType.getFlavor().getEffect().apply(user, stack,  world, teaType);
				}
			}
		}
		if(playerEntity != null) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			if(!playerEntity.getAbilities().creativeMode) {
				stack.decrement(1);
			}
		}
		if(playerEntity == null || !playerEntity.getAbilities().creativeMode) {
			if(stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}
			if(playerEntity != null) {
				playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
			}
		}
		return stack;
	}
}