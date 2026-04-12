package fr.hugman.culinaire.client.render.item.tint;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.client.color.item.ItemTintSources;

public class CulinaireTintSourceTypes {
    public static void register() {
        ItemTintSources.ID_MAPPER.put(Culinaire.id("tea"), TeaTintSource.CODEC);
    }
}
