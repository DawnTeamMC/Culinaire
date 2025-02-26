package fr.hugman.culinaire.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.enums.NoteBlockInstrument;

public final class BlockSettings {
    public static AbstractBlock.Settings rock() {
        return AbstractBlock.Settings.create().instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(1.5F, 6.0f);
    }
}
