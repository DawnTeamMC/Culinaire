package fr.hugman.culinaire.client.mixin;

import fr.hugman.culinaire.item.BurnableItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
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
    @Inject(method = "getArmPose(Lnet/minecraft/entity/PlayerLikeEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;", at = @At(value = "HEAD"), cancellable = true)
    private static void culinaire$getArmPose(PlayerLikeEntity player, ItemStack stack, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        if (!stack.isEmpty() && player.isSneaking()) {
            if (!player.handSwinging && stack.getItem() instanceof BurnableItem) {
                HitResult hitResult = player.raycast(1.5D, 0.0F, true);
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                    BlockState state = player.getEntityWorld().getBlockState(blockHitResult.getBlockPos());
                    if (CampfireBlock.isLitCampfire(state) && blockHitResult.getSide() != Direction.DOWN) {
                        cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
                    }
                }
            }
        }
    }
}
