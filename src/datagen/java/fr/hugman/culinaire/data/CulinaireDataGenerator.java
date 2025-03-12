package fr.hugman.culinaire.data;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.data.provider.CulinaireModelProvider;
import fr.hugman.culinaire.data.provider.CulinaireRecipeGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import org.jetbrains.annotations.Nullable;

public class CulinaireDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        // - Models
        pack.addProvider(CulinaireModelProvider::new);

        // - Recipes
        pack.addProvider(CulinaireRecipeGenerator::create);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
    }

    @Override
    @Nullable
    public String getEffectiveModId() {
        return Culinaire.MOD_ID;
    }
}
