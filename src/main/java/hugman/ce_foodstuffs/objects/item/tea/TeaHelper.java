package hugman.ce_foodstuffs.objects.item.tea;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class TeaHelper {
	public static List<TeaType> getTypes(ItemStack stack) {
		List<TeaType> teaTypes = new ArrayList<>();
		for(TeaType teaType : getAll()) {
			if(Ingredient.fromTag(teaType.getTag()).test(stack)) {
				teaTypes.add(teaType);
			}
		}
		return teaTypes;
	}

	public static List<TeaType> getAll() {
		List<TeaType> teaTypes = new ArrayList<>();
		for(TeaType.Strength strength : TeaType.Strength.values()) {
			for(TeaType.Flavor flavor : TeaType.Flavor.values()) {
				teaTypes.add(new TeaType(strength, flavor));
			}
		}
		return teaTypes;
	}

}
