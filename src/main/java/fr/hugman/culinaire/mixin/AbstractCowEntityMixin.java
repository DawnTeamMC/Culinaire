package fr.hugman.culinaire.mixin;

import fr.hugman.culinaire.item.CulinaireItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractCow.class)
public class AbstractCowEntityMixin {
    @Inject(method = "mobInteract", at = @At(value = "HEAD"), cancellable = true)
    public void culinaire$interactMob(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
        Cow cowEntity = (Cow) (Object) this;
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() == Items.GLASS_BOTTLE && !cowEntity.isBaby()) {
            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, player, CulinaireItems.MILK_BOTTLE.getDefaultInstance());
            player.setItemInHand(hand, itemStack2);
            info.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
