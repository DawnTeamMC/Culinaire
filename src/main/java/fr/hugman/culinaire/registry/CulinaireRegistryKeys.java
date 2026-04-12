package fr.hugman.culinaire.registry;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.tea.TeaType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class CulinaireRegistryKeys {
    public static final ResourceKey<Registry<TeaType>> TEA_TYPE = ResourceKey.createRegistryKey(Culinaire.id("tea_type"));
}
