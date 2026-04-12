package fr.hugman.culinaire.item;

import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.TeaTypesComponent;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipDisplay;

public class TeaBottleItem extends TeaItem {
    public TeaBottleItem(Properties settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack itemStack = super.getDefaultInstance();
        itemStack.set(CulinaireComponentTypes.TEA_TYPES, TeaTypesComponent.DEFAULT);
        return itemStack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, tooltip, type);
        PotionContents potionContentsComponent = stack.get(DataComponents.POTION_CONTENTS);
        if (potionContentsComponent != null) {
            PotionContents.addPotionTooltip(potionContentsComponent.getAllEffects(), tooltip, 1.0F, context.tickRate());
        }
    }
}
