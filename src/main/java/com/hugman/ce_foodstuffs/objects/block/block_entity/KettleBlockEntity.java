package com.hugman.ce_foodstuffs.objects.block.block_entity;

import com.hugman.ce_foodstuffs.CEFoodstuffs;
import com.hugman.ce_foodstuffs.init.CEFBlocks;
import com.hugman.ce_foodstuffs.objects.item.TeaBagItem;
import com.hugman.ce_foodstuffs.objects.item.tea.TeaHelper;
import com.hugman.ce_foodstuffs.objects.item.tea.TeaType;
import com.hugman.ce_foodstuffs.objects.screen.handler.KettleScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class KettleBlockEntity extends LockableContainerBlockEntity implements SidedInventory, Tickable {
	private static final int[] TOP_SLOTS = new int[]{0};
	protected final PropertyDelegate propertyDelegate;
	private DefaultedList<ItemStack> inventory;
	private Item itemBrewing;
	private int brewTime;
	private int fluidLevel;
	private int fluid;
	private List<TeaType> teaTypes;
	private boolean isHot;

	public KettleBlockEntity() {
		super(CEFBlocks.KETTLE_ENTITY);
		this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
		this.fluid = 0;
		this.teaTypes = new ArrayList<>();
		this.propertyDelegate = new PropertyDelegate() {
			public int get(int index) {
				switch(index) {
					case 0:
						return KettleBlockEntity.this.brewTime;
					case 1:
						return KettleBlockEntity.this.fluidLevel;
					case 2:
						return KettleBlockEntity.this.fluid;
					case 3:
						return KettleBlockEntity.this.isHot ? 1 : 0;
					case 4:
						return TeaHelper.getColor(KettleBlockEntity.this.teaTypes);
					default:
						return 0;
				}
			}

			public void set(int index, int value) {
				switch(index) {
					case 0:
						KettleBlockEntity.this.brewTime = value;
						break;
					case 1:
						KettleBlockEntity.this.fluidLevel = value;
						break;
					case 2:
						KettleBlockEntity.this.fluid = value;
						break;
					case 3:
						KettleBlockEntity.this.isHot = value == 1;
						break;
					case 4:
						break;
				}

			}

			public int size() {
				return 5;
			}
		};
	}

	@Override
	protected Text getContainerName() {
		return new TranslatableText("container." + CEFoodstuffs.MOD_DATA.getModName() + ".kettle");
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new KettleScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}

	@Override
	public int size() {
		return this.inventory.size();
	}

	/**
	 * <code>0</code> = Empty<p>
	 * <code>1</code> = Water<p>
	 * <code>2</code> = Tea
	 *
	 * @return
	 */
	public int getFluid() {
		return this.fluid;
	}

	public boolean addWater(int amount) {
		byte fLevel = (byte) (this.fluidLevel + amount);
		if(fLevel <= 3) {
			this.fluid = 1;
			this.fluidLevel = fLevel;
			return true;
		}
		return false;
	}

	public boolean removeFluid(int amount) {
		byte fLevel = (byte) (this.fluidLevel - amount);
		if(fLevel >= 0) {
			this.fluidLevel = fLevel;
			if(fLevel == 0) {
				this.fluid = 0;
				this.teaTypes = new ArrayList<>();
			}
			return true;
		}
		return false;
	}

	public List<TeaType> getTeaTypes() {
		return this.teaTypes;
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack stack : this.inventory) {
			if(!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		return slot >= 0 && slot < this.inventory.size() ? this.inventory.get(slot) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(this.inventory, slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(this.inventory, slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		if(slot >= 0 && slot < this.inventory.size()) {
			this.inventory.set(slot, stack);
		}
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		if(this.world.getBlockEntity(this.pos) != this) {
			return false;
		}
		else {
			return player.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		if(stack.getItem() instanceof TeaBagItem) {
			return !TeaHelper.getTeaTypesByCompound(stack.getTag()).isEmpty();
		}
		else {
			return false;
		}
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		if(side == Direction.UP) {
			return TOP_SLOTS;
		}
		else {
			return new int[]{};
		}
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return this.isValid(slot, stack);
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public void tick() {
		ItemStack stack = this.inventory.get(0);
		this.isHot = isHot();
		boolean canBrew = this.canBrew(stack);
		boolean isBrewing = this.brewTime > 0;
		if(isBrewing) {
			--this.brewTime;
			boolean isFinishedBrewing = this.brewTime == 0;
			if(canBrew && isFinishedBrewing) {
				this.brew(stack);
				this.markDirty();
			}
			else if(!canBrew) {
				this.brewTime = 0;
				this.markDirty();
			}
			else if(this.itemBrewing != stack.getItem()) {
				this.brewTime = 0;
				this.markDirty();
			}
		}
		else if(canBrew) {
			this.brewTime = 800;
			this.itemBrewing = stack.getItem();
			this.markDirty();
		}
		if(this.fluid != 0 && this.fluidLevel == 0) {
			this.fluid = 0;
			this.markDirty();
		}
	}

	public boolean isHot() {
		for(Direction direction : Direction.values()) {
			if(isHotBlock(this.getPos().offset(direction))) {
				return true;
			}
		}
		return this.world.getDimension().isUltrawarm();
	}

	public boolean isHotBlock(BlockPos pos) {
		BlockState blockState = this.world.getBlockState(pos);
		return CampfireBlock.isLitCampfire(blockState) || blockState.isOf(Blocks.MAGMA_BLOCK) || blockState.isOf(Blocks.LAVA);
	}

	private boolean canBrew(ItemStack stack) {
		if(stack.isEmpty()) {
			return false;
		}
		else if(stack.getItem() instanceof TeaBagItem) {
			return !TeaHelper.getTeaTypesByCompound(stack.getTag()).isEmpty() && fluid == 1 && fluidLevel >= 1 && this.isHot;
		}
		else {
			return false;
		}
	}

	private void brew(ItemStack stack) {
		this.fluid = 2;
		this.teaTypes = TeaHelper.getTeaTypesByCompound(stack.getTag());
		stack.decrement(1);
		BlockPos blockPos = this.getPos();
		if(stack.getItem().hasRecipeRemainder()) {
			ItemStack remainderStack = new ItemStack(stack.getItem().getRecipeRemainder());
			if(stack.isEmpty()) {
				stack = remainderStack;
			}
			else if(!this.world.isClient) {
				ItemScatterer.spawn(this.world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), remainderStack);
			}
		}
		this.world.playSound(null, pos, CEFBlocks.KETTLE_BREW_SOUND, SoundCategory.BLOCKS, 1.0F, 1.0F);
		this.inventory.set(0, stack);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		Inventories.toTag(tag, this.inventory);
		tag.putShort("BrewTime", (short) this.brewTime);
		tag.putByte("Fluid", (byte) this.fluid);
		tag.putByte("FluidLevel", (byte) this.fluidLevel);
		ListTag listTag = new ListTag();
		for(TeaType teaType : teaTypes) {
			CompoundTag typeTag = new CompoundTag();
			typeTag.putString("Flavor", teaType.getFlavor().getName());
			typeTag.putString("Strength", teaType.getStrength().getName());
			listTag.add(typeTag);
		}
		tag.put("TeaTypes", listTag);
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
		Inventories.fromTag(tag, this.inventory);
		this.brewTime = tag.getShort("BrewTime");
		this.fluid = tag.getByte("Fluid");
		this.fluidLevel = tag.getByte("FluidLevel");
		ListTag teaTypeList = tag.getList("TeaTypes", 10);
		if(!teaTypeList.isEmpty()) {
			for(int i = 0; i < teaTypeList.size(); ++i) {
				CompoundTag typeTag = teaTypeList.getCompound(i);
				TeaType teaType = new TeaType(typeTag.getString("Strength"), typeTag.getString("Flavor"));
				if(teaType.isCorrect()) {
					teaTypes.add(teaType);
				}
			}
		}
	}
}
