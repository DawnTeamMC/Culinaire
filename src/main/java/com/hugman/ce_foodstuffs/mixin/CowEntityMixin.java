package com.hugman.ce_foodstuffs.mixin;

import com.hugman.ce_foodstuffs.init.CEFItems;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CowEntity.class)
public class CowEntityMixin {
	@Inject(method = "interactMob", at = @At(value = "HEAD"), cancellable = true)
	public void commonExpansion_interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
		CowEntity cowEntity = (CowEntity) (Object) this;
		ItemStack itemStack = player.getStackInHand(hand);
		if(itemStack.getItem() == Items.GLASS_BOTTLE && !cowEntity.isBaby()) {
			player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
			ItemStack itemStack2 = ItemUsage.method_30012(itemStack, player, CEFItems.MILK_BOTTLE.getDefaultStack());
			player.setStackInHand(hand, itemStack2);
			info.setReturnValue(ActionResult.success(cowEntity.world.isClient));
		}
	}
}
