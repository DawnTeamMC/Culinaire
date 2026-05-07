package fr.hugman.culinaire.mixin;

import fr.hugman.culinaire.component.CulinaireComponentTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "addDetailsToTooltip", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
            ordinal = 12,
            shift = At.Shift.AFTER
    ))
    private void culinaire$appendTooltips(Item.TooltipContext context, TooltipDisplay display, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;

        stack.addToTooltip(CulinaireComponentTypes.SANDWICH_INGREDIENTS, context, display, builder, tooltipFlag);
        stack.addToTooltip(CulinaireComponentTypes.SANDWICH_CONTENTS, context, display, builder, tooltipFlag);
        stack.addToTooltip(CulinaireComponentTypes.TEA_TYPES, context, display, builder, tooltipFlag);
    }
}