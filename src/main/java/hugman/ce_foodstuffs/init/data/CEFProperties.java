package hugman.ce_foodstuffs.init.data;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;

public class CEFProperties {
	public static final BooleanProperty COAGULATED = BooleanProperty.of("coagulated");
	public static final IntProperty BITES_4 = IntProperty.of("bites", 0, 5);
	public static final IntProperty LEVEL_1_3 = IntProperty.of("level", 1, 3);
}
