package fr.hugman.culinaire.client.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {
    @Mutable
    @Accessor("imageWidth")
    void setImageWidth(int imageWidth);

    @Mutable
    @Accessor("imageHeight")
    void setImageHeight(int imageHeight);
}

