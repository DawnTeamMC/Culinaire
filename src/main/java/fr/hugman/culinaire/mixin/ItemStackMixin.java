package fr.hugman.culinaire.mixin;

import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.tea.TeaHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V",
                    ordinal = 8,
                    shift = At.Shift.AFTER
            ))
    private void culinaire$appendTooltips(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        /* FIXME
        List<Text> tooltip = cir.getReturnValue();
        Consumer<Text> consumer = tooltip::add;

        stack.appendTooltip(CulinaireComponentTypes.SANDWICH_CONTENTS, context, consumer, type);

        var teaContents = stack.get(CulinaireComponentTypes.TEA_CONTENTS);
        if (teaContents != null) {
            TeaHelper.appendTeaTooltip(consumer, teaContents);
        }

         */
    }
}