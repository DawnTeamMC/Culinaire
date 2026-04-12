package fr.hugman.culinaire.item;

import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.TeaTypesComponent;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class TeaItem extends Item {
    public TeaItem(Properties settings) {
        super(settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> tooltip, TooltipFlag type) {
        TeaTypesComponent teaTypesComponent = stack.get(CulinaireComponentTypes.TEA_TYPES);
        if (teaTypesComponent != null) {
            teaTypesComponent.addToTooltip(context, tooltip, type, stack.getComponents());
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        TeaTypesComponent teaTypesComponent = stack.get(CulinaireComponentTypes.TEA_TYPES);
        if (teaTypesComponent != null) {
            var entry = this.builtInRegistryHolder();
            return teaTypesComponent.getName(entry)
                    .orElse(Component.translatable(entry.value().getDescriptionId() + ".mixed"));
        }
        return super.getName(stack);
    }
}
