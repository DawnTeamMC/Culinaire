package fr.hugman.culinaire.client.render.item.tint;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.client.render.item.tint.TintSourceTypes;

public class CulinaireTintSourceTypes {
    public static void register() {
        TintSourceTypes.ID_MAPPER.put(Culinaire.id("tea"), TeaTintSource.CODEC);
    }
}
