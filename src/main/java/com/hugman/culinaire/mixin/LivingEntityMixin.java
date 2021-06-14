package com.hugman.culinaire.mixin;

import com.hugman.culinaire.init.CulinaireTeaBundle;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Inject(method = "getArmor", at = @At("HEAD"), cancellable = true)
	private void dawn_getArmor(CallbackInfoReturnable<Integer> info) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if(entity.hasStatusEffect(CulinaireTeaBundle.GUARD)) {
			double d = entity.getAttributeValue(EntityAttributes.GENERIC_ARMOR);
			info.setReturnValue(MathHelper.floor(d + d * 0.15F * (float) (entity.getStatusEffect(CulinaireTeaBundle.GUARD).getAmplifier() + 1)));
		}
	}
}
