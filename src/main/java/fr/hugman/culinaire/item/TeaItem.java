package fr.hugman.culinaire.item;

import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.TeaTypesComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;

import java.util.List;

public class TeaItem extends Item {
    public TeaItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        TeaTypesComponent teaTypesComponent = stack.get(CulinaireComponentTypes.TEA_TYPES);
        if (teaTypesComponent != null) {
            teaTypesComponent.appendTooltip(context, tooltip::add, type);
        }
    }
}
