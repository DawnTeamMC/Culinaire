package hugman.common_expansion.objects.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class MarshmallowOnAStickItem extends Item {
	public MarshmallowOnAStickItem(Settings settings) {
		super(settings);
	}

	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		ItemStack itemStack = super.finishUsing(stack, world, user);
		return user instanceof PlayerEntity && ((PlayerEntity) user).abilities.creativeMode ? itemStack : new ItemStack(Items.STICK);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(selected && entity.isSneaking()) {
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}
}
