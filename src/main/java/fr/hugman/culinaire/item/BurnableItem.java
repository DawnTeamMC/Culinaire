package fr.hugman.culinaire.item;

import fr.hugman.culinaire.component.CulinaireComponentTypes;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

public class BurnableItem extends Item {
    public BurnableItem(Properties settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot slot) {
        if (!world.isClientSide() && slot != null && slot.getType() == EquipmentSlot.Type.HAND && entity instanceof LivingEntity && entity.isShiftKeyDown()) {
            HitResult hitResult = entity.pick(1.5D, 0.0F, true);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockState state = world.getBlockState(blockHitResult.getBlockPos());
                if (CampfireBlock.isLitCampfire(state) && blockHitResult.getDirection() != Direction.DOWN) {
                    incrementBurningTime((LivingEntity) entity, stack);
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot);
    }

    public void incrementBurningTime(LivingEntity livingEntity, ItemStack stack) {
        if (stack.has(CulinaireComponentTypes.BURNABLE)) {
            int burnTime = stack.getOrDefault(CulinaireComponentTypes.BURN, 0);
            var burnable = stack.get(CulinaireComponentTypes.BURNABLE);
            if (++burnTime >= burnable.burningTime()) {
                livingEntity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(burnable.burnsInto(), stack.getCount()));
                return;
            }
            stack.set(CulinaireComponentTypes.BURN, burnTime);
        }
    }
}
