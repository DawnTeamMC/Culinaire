package fr.hugman.culinaire.item;

import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.TeaTypesComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class TeaBottleItem extends TeaItem {
    public TeaBottleItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack itemStack = super.getDefaultStack();
        itemStack.set(CulinaireComponentTypes.TEA_TYPES, TeaTypesComponent.DEFAULT);
        return itemStack;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, tooltip, type);
        PotionContentsComponent potionContentsComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContentsComponent != null) {
            PotionContentsComponent.buildTooltip(potionContentsComponent.getEffects(), tooltip, 1.0F, context.getUpdateTickRate());
        }
    }
}
