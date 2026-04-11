package fr.hugman.culinaire.mixin;

import fr.hugman.culinaire.component.CulinaireComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "appendTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;appendComponentTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/component/type/TooltipDisplayComponent;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V",
                    ordinal = 12,
                    shift = At.Shift.AFTER
            ))
    private void culinaire$appendTooltips(Item.TooltipContext context, TooltipDisplayComponent displayComponent, @Nullable PlayerEntity player, TooltipType type, Consumer<Text> textConsumer, CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;

        stack.appendComponentTooltip(CulinaireComponentTypes.SANDWICH_CONTENTS, context, displayComponent, textConsumer, type);
        stack.appendComponentTooltip(CulinaireComponentTypes.TEA_TYPES, context, displayComponent, textConsumer, type);
    }
}