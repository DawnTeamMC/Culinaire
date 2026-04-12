package fr.hugman.culinaire.screen;

import fr.hugman.culinaire.item.CulinaireItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class KettleScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;

    public KettleScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(1), new SimpleContainerData(6));
    }

    public KettleScreenHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(CulinaireScreenHandlerTypes.KETTLE, syncId);
        checkContainerSize(inventory, 1);
        checkContainerDataCount(propertyDelegate, 6);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.addSlot(new TeaBagSlot(inventory, 0, 80, 17));
        this.addDataSlots(propertyDelegate);
        int k;
        for (k = 0; k < 3; ++k) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }
        for (k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index != 0) {
                if (TeaBagSlot.matches(itemStack2)) {
                    if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 27) {
                    if (!this.moveItemStackTo(itemStack2, 27, 36, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 36 && !this.moveItemStackTo(itemStack2, 1, 27, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 1, 27, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemStack2);
        }
        return itemStack;
    }

    @Environment(EnvType.CLIENT)
    public int getBrewTime() {
        return this.propertyDelegate.get(0);
    }

    @Environment(EnvType.CLIENT)
    public int getTotalBrewTime() {
        return this.propertyDelegate.get(1);
    }

    @Environment(EnvType.CLIENT)
    public int getFluidLevel() {
        return this.propertyDelegate.get(2);
    }

    @Environment(EnvType.CLIENT)
    public int getFluid() {
        return this.propertyDelegate.get(3);
    }

    @Environment(EnvType.CLIENT)
    public boolean isHot() {
        return this.propertyDelegate.get(4) == 1;
    }

    @Environment(EnvType.CLIENT)
    public int getTeaColor() {
        return this.propertyDelegate.get(5);
    }

    static class TeaBagSlot extends Slot {
        public TeaBagSlot(Container inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        public static boolean matches(ItemStack stack) {
            //TODO: change to a tag
            return stack.getItem() == CulinaireItems.TEA_BAG;
        }

        public boolean mayPlace(ItemStack stack) {
            return matches(stack);
        }

        public int getMaxStackSize() {
            return CulinaireItems.TEA_BAG.getDefaultMaxStackSize();
        }
    }
}
