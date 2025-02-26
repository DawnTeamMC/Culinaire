package fr.hugman.culinaire.sound;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

import java.util.Optional;

public class CulinaireSoundEvents {
    private static RegistryEntry.Reference<SoundEvent> ofRef(String path) {
        var id = Culinaire.id(path);
        return Registry.registerReference(Registries.SOUND_EVENT, id, new SoundEvent(id, Optional.empty()));
    }

    private static SoundEvent of(String path) {
        var id = Culinaire.id(path);
        return Registry.register(Registries.SOUND_EVENT, id, new SoundEvent(id, Optional.empty()));
    }
}
