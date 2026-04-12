package fr.hugman.culinaire.sound;

import fr.hugman.culinaire.Culinaire;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class CulinaireSoundEvents {
    // TEA
    public static final SoundEvent KETTLE_BREW = of("block.kettle.brew");
    public static final SoundEvent TEA_BOTTLE_FILL = SoundEvent.createVariableRangeEvent(Culinaire.id("item.tea_bottle.fill"));

    private static Holder.Reference<SoundEvent> ofRef(String path) {
        var id = Culinaire.id(path);
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, new SoundEvent(id, Optional.empty()));
    }

    private static SoundEvent of(String path) {
        var id = Culinaire.id(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, new SoundEvent(id, Optional.empty()));
    }
}
