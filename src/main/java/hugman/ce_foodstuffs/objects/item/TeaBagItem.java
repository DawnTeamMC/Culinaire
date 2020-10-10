package hugman.ce_foodstuffs.objects.item;

import hugman.ce_foodstuffs.objects.item.tea.TeaHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class TeaBagItem extends Item {
	public TeaBagItem(Settings settings) {
		super(settings);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		TeaHelper.appendTeaTooltip(tooltip, TeaHelper.getTeaTypesByCompound(stack.getTag()));
	}
}