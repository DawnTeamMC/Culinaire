package fr.hugman.culinaire.data;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.data.provider.*;
import fr.hugman.culinaire.registry.CulinaireRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import org.jetbrains.annotations.Nullable;

public class CulinaireDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        // - Models
        pack.addProvider(CulinaireModelProvider::new);

        // Data Pack
        // - Tea types
        pack.addProvider(CulinaireTeaTypeProvider::new);

        // - Loot tables
        pack.addProvider(CulinaireBlockLootTableProvider::new);

        // - Tags
        pack.addProvider(CulinaireBlockTagProvider::new);
        pack.addProvider(CulinaireItemTagProvider::new);
        pack.addProvider(CulinaireTeaTypeTagProvider::new);

        // - Recipes
        pack.addProvider(CulinaireRecipeGenerator::create);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(CulinaireRegistryKeys.TEA_TYPE, CulinaireTeaTypeProvider::register);
    }

    @Override
    @Nullable
    public String getEffectiveModId() {
        return Culinaire.MOD_ID;
    }
}
