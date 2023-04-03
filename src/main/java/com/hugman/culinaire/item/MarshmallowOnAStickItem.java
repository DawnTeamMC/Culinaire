package com.hugman.culinaire.item;

import com.hugman.culinaire.registry.content.CandyContent;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MarshmallowOnAStickItem extends Item {
    public MarshmallowOnAStickItem(Settings settings) {
        super(settings);
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack itemStack = super.finishUsing(stack, world, user);
        return user instanceof PlayerEntity && ((PlayerEntity) user).getAbilities().creativeMode ? itemStack : new ItemStack(Items.STICK);
    }

    public void incrementBurningTime(LivingEntity livingEntity, ItemStack stack) {
        int burnTime = 0;
        int maxBurnTime = getDefaultMaxBurnTime(stack.getItem());
        Item burnItem = getDefaultBurnItem(stack.getItem());
        NbtCompound compoundTag = stack.getOrCreateNbt();
        if (compoundTag.contains("BurnTime")) {
            burnTime = compoundTag.getInt("BurnTime");
        }
        if (compoundTag.contains("MaxBurnTime")) {
            maxBurnTime = compoundTag.getInt("MaxBurnTime");
        } else {
            compoundTag.putInt("MaxBurnTime", maxBurnTime);
        }
        if (compoundTag.contains("BurnItem")) {
            burnItem = Registries.ITEM.get(new Identifier(compoundTag.getString("BurnItem")));
        } else {
            compoundTag.putString("BurnItem", Registries.ITEM.getId(burnItem).toString());
        }
        burnTime++;
        compoundTag.putInt("BurnTime", burnTime);
        if (burnTime >= maxBurnTime) {
            livingEntity.setStackInHand(Hand.MAIN_HAND, new ItemStack(burnItem, stack.getCount()));
        }
    }

    public int getDefaultMaxBurnTime(Item item) {
        if (item == CandyContent.MARSHMALLOW_ON_A_STICK) {
            return 150;
        } else if (item == CandyContent.TOASTY_MARSHMALLOW_ON_A_STICK) {
            return 75;
        } else if (item == CandyContent.GOLDEN_MARSHMALLOW_ON_A_STICK) {
            return 20;
        } else if (item == CandyContent.BURNT_MARSHMALLOW_ON_A_STICK) {
            return 30;
        } else {
            return 60;
        }
    }

    public Item getDefaultBurnItem(Item item) {
        if (item == CandyContent.MARSHMALLOW_ON_A_STICK) {
            return CandyContent.TOASTY_MARSHMALLOW_ON_A_STICK;
        } else if (item == CandyContent.TOASTY_MARSHMALLOW_ON_A_STICK) {
            return CandyContent.GOLDEN_MARSHMALLOW_ON_A_STICK;
        } else if (item == CandyContent.GOLDEN_MARSHMALLOW_ON_A_STICK) {
            return CandyContent.BURNT_MARSHMALLOW_ON_A_STICK;
        } else if (item == CandyContent.BURNT_MARSHMALLOW_ON_A_STICK) {
            return Items.STICK;
        } else {
            return Items.AIR;
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && selected && entity instanceof LivingEntity && entity.isSneaking()) {
            HitResult hitResult = entity.raycast(1.5D, 0.0F, true);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockState state = world.getBlockState(blockHitResult.getBlockPos());
                if (CampfireBlock.isLitCampfire(state) && blockHitResult.getSide() != Direction.DOWN) {
                    incrementBurningTime((LivingEntity) entity, stack);
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
