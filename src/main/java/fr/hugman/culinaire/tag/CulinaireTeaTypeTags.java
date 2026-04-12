package fr.hugman.culinaire.tag;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import fr.hugman.culinaire.tea.TeaType;
import net.minecraft.tags.TagKey;

public class CulinaireTeaTypeTags {
    public static final TagKey<TeaType> TOOLTIP_ORDER = of("tooltip_order");

    private static TagKey<TeaType> of(String path) {
        return TagKey.create(CulinaireRegistryKeys.TEA_TYPE, Culinaire.id(path));
    }
}
