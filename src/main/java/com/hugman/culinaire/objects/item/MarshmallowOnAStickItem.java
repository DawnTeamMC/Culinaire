package com.hugman.culinaire.objects.item;

import com.hugman.culinaire.init.CulinaireItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class MarshmallowOnAStickItem extends Item {
	public MarshmallowOnAStickItem(Settings settings) {
		super(settings);
	}

	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		ItemStack itemStack = super.finishUsing(stack, world, user);
		return user instanceof PlayerEntity && ((PlayerEntity) user).abilities.creativeMode ? itemStack : new ItemStack(Items.STICK);
	}

	public void incrementBurningTime(LivingEntity livingEntity, ItemStack stack) {
		int burnTime = 0;
		int maxBurnTime = getDefaultMaxBurnTime(stack.getItem());
		Item burnItem = getDefaultBurnItem(stack.getItem());
		CompoundTag compoundTag = stack.getOrCreateTag();
		if(compoundTag.contains("BurnTime")) {
			burnTime = compoundTag.getInt("BurnTime");
		}
		if(compoundTag.contains("MaxBurnTime")) {
			maxBurnTime = compoundTag.getInt("MaxBurnTime");
		}
		else {
			compoundTag.putInt("MaxBurnTime", maxBurnTime);
		}
		if(compoundTag.contains("BurnItem")) {
			burnItem = Registry.ITEM.get(new Identifier(compoundTag.getString("BurnItem")));
		}
		else {
			compoundTag.putString("BurnItem", Registry.ITEM.getId(burnItem).toString());
		}
		burnTime++;
		compoundTag.putInt("BurnTime", burnTime);
		if(burnTime >= maxBurnTime) {
			livingEntity.setStackInHand(Hand.MAIN_HAND, new ItemStack(burnItem, stack.getCount()));
		}
	}

	public int getDefaultMaxBurnTime(Item item) {
		if(item == CulinaireItems.MARSHMALLOW_ON_A_STICK) {
			return 150;
		}
		else if(item == CulinaireItems.TOASTY_MARSHMALLOW_ON_A_STICK) {
			return 75;
		}
		else if(item == CulinaireItems.GOLDEN_MARSHMALLOW_ON_A_STICK) {
			return 20;
		}
		else if(item == CulinaireItems.BURNT_MARSHMALLOW_ON_A_STICK) {
			return 30;
		}
		else {
			return 60;
		}
	}

	public Item getDefaultBurnItem(Item item) {
		if(item == CulinaireItems.MARSHMALLOW_ON_A_STICK) {
			return CulinaireItems.TOASTY_MARSHMALLOW_ON_A_STICK;
		}
		else if(item == CulinaireItems.TOASTY_MARSHMALLOW_ON_A_STICK) {
			return CulinaireItems.GOLDEN_MARSHMALLOW_ON_A_STICK;
		}
		else if(item == CulinaireItems.GOLDEN_MARSHMALLOW_ON_A_STICK) {
			return CulinaireItems.BURNT_MARSHMALLOW_ON_A_STICK;
		}
		else if(item == CulinaireItems.BURNT_MARSHMALLOW_ON_A_STICK) {
			return Items.STICK;
		}
		else {
			return Items.AIR;
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(!world.isClient() && selected && entity instanceof LivingEntity && entity.isSneaking()) {
			HitResult hitResult = entity.raycast(1.5D, 0.0F, true);
			if(hitResult.getType() == HitResult.Type.BLOCK) {
				BlockHitResult blockHitResult = (BlockHitResult) hitResult;
				BlockState state = world.getBlockState(blockHitResult.getBlockPos());
				if(CampfireBlock.isLitCampfire(state) && blockHitResult.getSide() != Direction.DOWN) {
					incrementBurningTime((LivingEntity) entity, stack);
				}
			}
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}
}
