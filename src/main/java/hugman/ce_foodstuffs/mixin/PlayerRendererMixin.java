package hugman.ce_foodstuffs.mixin;

import hugman.ce_foodstuffs.objects.item.MarshmallowOnAStickItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
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
				HitResult hitResult = abstractClientPlayerEntity.raycast(1.5D, 0.0F, true);
				if(hitResult.getType() == HitResult.Type.BLOCK) {
					BlockHitResult blockHitResult = (BlockHitResult) hitResult;
					BlockState state = abstractClientPlayerEntity.getEntityWorld().getBlockState(blockHitResult.getBlockPos());
					if(CampfireBlock.isLitCampfire(state) && blockHitResult.getSide() != Direction.DOWN) {
						info.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
					}
				}
			}
		}
	}
}
