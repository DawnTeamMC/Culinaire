package hugman.ce_foodstuffs.init;

import com.hugman.dawn.api.creator.SoundCreator;
import net.minecraft.sound.SoundEvent;

public class CEFSounds extends CEFPack {
	public static final SoundEvent BLOCK_KETTLE_BREW = register(new SoundCreator.Builder("block.kettle.brew"));
	public static final SoundEvent ITEM_TEA_BOTTLE_FILL = register(new SoundCreator.Builder("item.tea_bottle.fill"));
}
