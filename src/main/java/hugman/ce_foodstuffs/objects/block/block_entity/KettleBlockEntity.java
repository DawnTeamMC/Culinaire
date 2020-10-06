package hugman.ce_foodstuffs.objects.block.block_entity;

import hugman.ce_foodstuffs.CEFoodstuffs;
import hugman.ce_foodstuffs.init.CEFBlocks;
import hugman.ce_foodstuffs.objects.item.TeaBagItem;
import hugman.ce_foodstuffs.objects.item.TeaBottleItem;
import hugman.ce_foodstuffs.objects.item.tea.TeaType;
import net.minecraft.block.BlockState;
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
import net.minecraft.screen.ScreenHandler;
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
	private DefaultedList<ItemStack> inventory;
	private Item itemBrewing;
	private int brewTime;
	private int fluidLevel;
	private Fluid fluid;
	private List<TeaType> teaTypes;

	public KettleBlockEntity() {
		super(CEFBlocks.KETTLE_ENTITY);
		this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
		this.fluid = Fluid.EMPTY;
		this.teaTypes = new ArrayList<>();
	}

	@Override
	protected Text getContainerName() {
		return new TranslatableText("container." + CEFoodstuffs.MOD_DATA.getModName() + ".brewing");
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		//TODO
		return null;
	}

	@Override
	public int size() {
		return this.inventory.size();
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
			return !TeaBottleItem.getTeaTypes(stack).isEmpty();
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

	public boolean addFluidLevel(Fluid fluid, int amount) {
		byte fLevel = (byte) (this.fluidLevel + amount);
		if(fLevel <= 3) {
			this.fluid = fluid;
			this.fluidLevel = fLevel;
			return true;
		}
		return false;
	}

	public boolean removeFluidLevel(int amount) {
		byte fLevel = (byte) (this.fluidLevel - amount);
		if(fLevel >= 0) {
			this.fluidLevel = fLevel;
			if(fLevel == 0) {
				this.fluid = Fluid.EMPTY;
				this.teaTypes = new ArrayList<>();
			}
			return true;
		}
		return false;
	}

	public Fluid getFluid() {
		return this.fluid;
	}

	public List<TeaType> getTeaTypes() {
		return this.teaTypes;
	}

	@Override
	public void tick() {
		ItemStack stack = this.inventory.get(0);
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
	}

	private boolean canBrew(ItemStack stack) {
		boolean isHot = CampfireBlock.isLitCampfire(this.world.getBlockState(this.getPos().down())) || this.world.getDimension().isUltrawarm();
		if(stack.isEmpty()) {
			return false;
		}
		else if(stack.getItem() instanceof TeaBagItem) {
			return !TeaBottleItem.getTeaTypes(stack).isEmpty() && fluid == Fluid.WATER && fluidLevel >= 1 && isHot;
		}
		else {
			return false;
		}
	}

	private void brew(ItemStack stack) {
		this.fluid = Fluid.TEA;
		this.teaTypes = TeaBottleItem.getTeaTypes(stack);
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
		this.inventory.set(0, stack);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		Inventories.toTag(tag, this.inventory);
		tag.putShort("BrewTime", (short) this.brewTime);
		tag.putString("Fluid", this.fluid.toString().toLowerCase());
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
		this.fluid = Fluid.valueOf(tag.getString("Fluid").toUpperCase());
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

	public enum Fluid {
		EMPTY,
		WATER,
		TEA
	}
}
