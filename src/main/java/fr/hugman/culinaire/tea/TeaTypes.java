package fr.hugman.culinaire.tea;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import net.minecraft.registry.RegistryKey;

public class TeaTypes {
    public static final RegistryKey<TeaType> GREEN = of("green");
    public static final RegistryKey<TeaType> WHITE = of("white");
    public static final RegistryKey<TeaType> BLACK = of("black");
    public static final RegistryKey<TeaType> OOLONG = of("oolong");
    public static final RegistryKey<TeaType> PU_ERH = of("pu_erh");
    public static final RegistryKey<TeaType> ENDER = of("ender");

    public static RegistryKey<TeaType> of(String path) {
        return RegistryKey.of(CulinaireRegistryKeys.TEA_TYPE, Culinaire.id(path));
    }
}
