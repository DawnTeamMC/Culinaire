package fr.hugman.culinaire.item;

import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.TeaTypesComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class TeaItem extends Item {
    public TeaItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> tooltip, TooltipType type) {
        TeaTypesComponent teaTypesComponent = stack.get(CulinaireComponentTypes.TEA_TYPES);
        if (teaTypesComponent != null) {
            teaTypesComponent.appendTooltip(context, tooltip, type, stack.getComponents());
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        TeaTypesComponent teaTypesComponent = stack.get(CulinaireComponentTypes.TEA_TYPES);
        if (teaTypesComponent != null) {
            var entry = this.getRegistryEntry();
            return teaTypesComponent.getName(entry)
                    .orElse(Text.translatable(entry.value().getTranslationKey() + ".mixed"));
        }
        return super.getName(stack);
    }
}
