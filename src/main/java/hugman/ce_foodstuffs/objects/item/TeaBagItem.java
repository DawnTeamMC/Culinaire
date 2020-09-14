package hugman.ce_foodstuffs.objects.item;

import hugman.ce_foodstuffs.CEFoodstuffs;
import hugman.ce_foodstuffs.objects.item.tea.TeaType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TeaBagItem extends Item {
	public TeaBagItem(Settings settings) {
		super(settings);
	}

	public static List<TeaType> getTeaTypes(ItemStack stack) {
		List<TeaType> teaTypes = new ArrayList<>();
		CompoundTag compoundTag = stack.getTag();
		if(compoundTag != null) {
			if(compoundTag.contains("TeaTypes")) {
				ListTag listTag = compoundTag.getList("TeaTypes", 10);
				if(!listTag.isEmpty()) {
					for(int i = 0; i < listTag.size(); ++i) {
						CompoundTag typeTag = listTag.getCompound(i);
						TeaType teaType = new TeaType(typeTag.getString("Strength"), typeTag.getString("Flavor"));
						if(teaType.isCorrect()) {
							teaTypes.add(teaType);
						}
					}
				}
			}
		}
		return teaTypes;
	}

	@Environment(EnvType.CLIENT)
	public static void appendTeaTooltip(ItemStack stack, List<Text> tooltip) {
		List<TeaType> teaTypes = getTeaTypes(stack);
		if(!teaTypes.isEmpty()) {
			MutableText text = (new LiteralText("")).formatted(Formatting.GRAY);
			for(int i = 0; i < teaTypes.size(); ++i) {
				TeaType teaType = teaTypes.get(i);
				if(i > 0) {
					text.append(", ");
				}
				text.append(new TranslatableText("tea_type." + CEFoodstuffs.MOD_ID + "." + teaType.getFlavor().getName() + "." + teaType.getStrength().getName()));
			}
			tooltip.add(text);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		appendTeaTooltip(stack, tooltip);
	}
}