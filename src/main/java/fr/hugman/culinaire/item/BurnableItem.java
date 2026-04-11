package fr.hugman.culinaire.item;

import fr.hugman.culinaire.component.CulinaireComponentTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class BurnableItem extends Item {
    public BurnableItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if (!world.isClient() && slot != null && slot.getType() == EquipmentSlot.Type.HAND && entity instanceof LivingEntity && entity.isSneaking()) {
            HitResult hitResult = entity.raycast(1.5D, 0.0F, true);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockState state = world.getBlockState(blockHitResult.getBlockPos());
                if (CampfireBlock.isLitCampfire(state) && blockHitResult.getSide() != Direction.DOWN) {
                    incrementBurningTime((LivingEntity) entity, stack);
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot);
    }

    public void incrementBurningTime(LivingEntity livingEntity, ItemStack stack) {
        if (stack.contains(CulinaireComponentTypes.BURNABLE)) {
            int burnTime = stack.getOrDefault(CulinaireComponentTypes.BURN, 0);
            var burnable = stack.get(CulinaireComponentTypes.BURNABLE);
            if (++burnTime >= burnable.burningTime()) {
                livingEntity.setStackInHand(Hand.MAIN_HAND, new ItemStack(burnable.burnsInto(), stack.getCount()));
                return;
            }
            stack.set(CulinaireComponentTypes.BURN, burnTime);
        }
    }
}
