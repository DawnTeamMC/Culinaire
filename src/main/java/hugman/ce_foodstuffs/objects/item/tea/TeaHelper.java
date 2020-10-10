package hugman.ce_foodstuffs.objects.item.tea;

import hugman.ce_foodstuffs.CEFoodstuffs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class TeaHelper {
	public static List<TeaType> getNaturalTypesOfIngredient(ItemStack stack) {
		List<TeaType> teaTypes = new ArrayList<>();
		for(TeaType teaType : getAllTypes()) {
			if(Ingredient.fromTag(teaType.getTag()).test(stack)) {
				teaTypes.add(teaType);
			}
		}
		return teaTypes;
	}

	public static List<TeaType> getAllTypes() {
		List<TeaType> teaTypes = new ArrayList<>();
		for(TeaType.Strength strength : TeaType.Strength.values()) {
			for(TeaType.Flavor flavor : TeaType.Flavor.values()) {
				teaTypes.add(new TeaType(strength, flavor));
			}
		}
		return teaTypes;
	}

	@Environment(EnvType.CLIENT)
	public static void appendTeaTooltip(List<Text> tooltips, List<TeaType> teaTypes) {
		for(TeaType teaType : teaTypes) {
			tooltips.add(new TranslatableText("tea_type." + CEFoodstuffs.MOD_DATA.getModName() + "." + teaType.getFlavor().getName() + "." + teaType.getStrength().getName()).formatted(Formatting.GRAY));
		}
	}

	public static List<TeaType> getTeaTypesByCompound(CompoundTag compoundTag) {
		List<TeaType> list = new ArrayList<>();
		if(compoundTag != null) {
			if(compoundTag.contains("TeaTypes")) {
				ListTag teaTypeList = compoundTag.getList("TeaTypes", 10);
				if(!teaTypeList.isEmpty()) {
					for(int i = 0; i < teaTypeList.size(); ++i) {
						CompoundTag typeTag = teaTypeList.getCompound(i);
						TeaType teaType = new TeaType(typeTag.getString("Strength"), typeTag.getString("Flavor"));
						if(teaType.isCorrect()) {
							list.add(teaType);
						}
					}
				}
			}
		}
		return list;
	}
}
