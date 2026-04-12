package fr.hugman.culinaire.tea;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import net.minecraft.resources.ResourceKey;

public class TeaTypes {
    public static final ResourceKey<TeaType> GREEN = of("green");
    public static final ResourceKey<TeaType> WHITE = of("white");
    public static final ResourceKey<TeaType> BLACK = of("black");
    public static final ResourceKey<TeaType> OOLONG = of("oolong");
    public static final ResourceKey<TeaType> PU_ER = of("pu_er");
    public static final ResourceKey<TeaType> ENDER = of("ender");

    public static ResourceKey<TeaType> of(String path) {
        return ResourceKey.create(CulinaireRegistryKeys.TEA_TYPE, Culinaire.id(path));
    }
}
