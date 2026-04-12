package fr.hugman.culinaire.data.provider;

import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import fr.hugman.culinaire.tag.CulinaireTeaTypeTags;
import fr.hugman.culinaire.tea.TeaType;
import fr.hugman.culinaire.tea.TeaTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class CulinaireTeaTypeTagProvider extends FabricTagProvider<TeaType> {
    public CulinaireTeaTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, CulinaireRegistryKeys.TEA_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        // Culinaire
        builder(CulinaireTeaTypeTags.TOOLTIP_ORDER).add(
                TeaTypes.GREEN,
                TeaTypes.WHITE,
                TeaTypes.BLACK,
                TeaTypes.OOLONG,
                TeaTypes.PU_ER,
                TeaTypes.ENDER
        );
    }
}