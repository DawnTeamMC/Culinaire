package com.hugman.ce_foodstuffs.objects.screen.handler;

import com.hugman.ce_foodstuffs.init.CEFBlocks;
import com.hugman.ce_foodstuffs.init.CEFItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class KettleScreenHandler extends ScreenHandler {
	private final Inventory inventory;
	private final PropertyDelegate propertyDelegate;

	public KettleScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(1), new ArrayPropertyDelegate(5));
	}

	public KettleScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(CEFBlocks.KETTLE_SCREEN_HANDLER, syncId);
		checkSize(inventory, 1);
		checkDataCount(propertyDelegate, 5);
		this.inventory = inventory;
		this.propertyDelegate = propertyDelegate;
		this.addSlot(new TeaBagSlot(inventory, 0, 80, 17));
		this.addProperties(propertyDelegate);
		int k;
		for(k = 0; k < 3; ++k) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
			}
		}
		for(k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if(slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if(index != 0) {
				if(TeaBagSlot.matches(itemStack2)) {
					if(!this.insertItem(itemStack2, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				}
				else if(index < 27) {
					if(!this.insertItem(itemStack2, 27, 36, false)) {
						return ItemStack.EMPTY;
					}
				}
				else if(index < 36 && !this.insertItem(itemStack2, 1, 27, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if(!this.insertItem(itemStack2, 1, 27, false)) {
				return ItemStack.EMPTY;
			}
			if(itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			}
			else {
				slot.markDirty();
			}
			if(itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTakeItem(player, itemStack2);
		}
		return itemStack;
	}

	@Environment(EnvType.CLIENT)
	public int getBrewTime() {
		return this.propertyDelegate.get(0);
	}

	@Environment(EnvType.CLIENT)
	public int getFluidLevel() {
		return this.propertyDelegate.get(1);
	}

	@Environment(EnvType.CLIENT)
	public int getFluid() {
		return this.propertyDelegate.get(2);
	}

	@Environment(EnvType.CLIENT)
	public boolean isHot() {
		return this.propertyDelegate.get(3) > 0;
	}

	@Environment(EnvType.CLIENT)
	public int getTeaColor() {
		return this.propertyDelegate.get(4);
	}

	static class TeaBagSlot extends Slot {
		public TeaBagSlot(Inventory inventory, int i, int j, int k) {
			super(inventory, i, j, k);
		}

		public static boolean matches(ItemStack stack) {
			return stack.getItem() == CEFItems.TEA_BAG;
		}

		public boolean canInsert(ItemStack stack) {
			return matches(stack);
		}

		public int getMaxItemCount() {
			return CEFItems.TEA_BAG.getMaxCount();
		}

	}
}
