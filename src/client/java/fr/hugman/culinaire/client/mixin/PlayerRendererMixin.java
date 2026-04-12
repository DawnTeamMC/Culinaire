package fr.hugman.culinaire.client.mixin;

import fr.hugman.culinaire.item.BurnableItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AvatarRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;", at = @At(value = "HEAD"), cancellable = true)
    private static void culinaire$getArmPose(Avatar player, ItemStack stack, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        if (!stack.isEmpty() && player.isShiftKeyDown()) {
            if (!player.swinging && stack.getItem() instanceof BurnableItem) {
                HitResult hitResult = player.pick(1.5D, 0.0F, true);
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                    BlockState state = player.level().getBlockState(blockHitResult.getBlockPos());
                    if (CampfireBlock.isLitCampfire(state) && blockHitResult.getDirection() != Direction.DOWN) {
                        cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
                    }
                }
            }
        }
    }
}
