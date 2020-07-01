package hugman.common_expansion.mixin;

import hugman.common_expansion.objects.item.MarshmallowOnAStickItem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerRendererMixin {
	@Inject(method = "getArmPose", at = @At(value = "HEAD"), cancellable = true)
	private static void commonExpansion_getArmPose(AbstractClientPlayerEntity abstractClientPlayerEntity, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> info) {
		ItemStack itemStack = abstractClientPlayerEntity.getStackInHand(hand);
		if(!itemStack.isEmpty() && abstractClientPlayerEntity.isSneaking()) {
			if(!abstractClientPlayerEntity.handSwinging && itemStack.getItem() instanceof MarshmallowOnAStickItem) {
				info.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
			}
		}
	}
}
