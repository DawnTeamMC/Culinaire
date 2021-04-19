package com.hugman.culinaire.mixin;

import com.hugman.culinaire.Culinaire;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public class MilkBucketMixin {
	@Inject(method = "finishUsing", at = @At(value = "HEAD"), cancellable = true)
	public void commonExpansion_finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
		if(!Culinaire.CONFIG.features.canDrinkMilkBucket) {
			info.setReturnValue(stack);
		}
	}

	@Inject(method = "getMaxUseTime", at = @At(value = "HEAD"), cancellable = true)
	public void commonExpansion_getMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> info) {
		if(!Culinaire.CONFIG.features.canDrinkMilkBucket) {
			info.setReturnValue(0);
		}
	}

	@Inject(method = "getUseAction", at = @At(value = "HEAD"), cancellable = true)
	public void commonExpansion_getUseAction(ItemStack stack, CallbackInfoReturnable<UseAction> info) {
		if(!Culinaire.CONFIG.features.canDrinkMilkBucket) {
			info.setReturnValue(UseAction.NONE);
		}
	}

	@Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
	public void commonExpansion_use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info) {
		if(!Culinaire.CONFIG.features.canDrinkMilkBucket) {
			info.setReturnValue(TypedActionResult.pass(user.getStackInHand(hand)));
		}
	}
}
