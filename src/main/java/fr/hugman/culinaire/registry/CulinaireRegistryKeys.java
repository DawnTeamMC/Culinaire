package fr.hugman.culinaire.registry;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.tea.TeaType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class CulinaireRegistryKeys {
    public static final RegistryKey<Registry<TeaType>> TEA_TYPE = RegistryKey.ofRegistry(Culinaire.id("tea_type"));
}
