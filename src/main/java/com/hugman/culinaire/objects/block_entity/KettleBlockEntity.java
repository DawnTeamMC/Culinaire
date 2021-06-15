package com.hugman.culinaire.objects.block_entity;

import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.init.CulinaireTeaBundle;
import com.hugman.culinaire.objects.item.TeaBagItem;
import com.hugman.culinaire.objects.item.tea.TeaHelper;
import com.hugman.culinaire.objects.item.tea.TeaType;
import com.hugman.culinaire.objects.screen.handler.KettleScreenHandler;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class KettleBlockEntity extends LockableContainerBlockEntity implements SidedInventory {
	private static final int[] TOP_SLOTS = new int[]{0};
	protected final PropertyDelegate propertyDelegate;
	private DefaultedList<ItemStack> inventory;
	private Item itemBrewing;
	private int brewTime;
	private int fluidLevel;
	private Fluid fluid;
	private List<TeaType> teaTypes;
	private boolean isHot;

	public KettleBlockEntity(BlockPos pos, BlockState state) {
		super(CulinaireTeaBundle.KETTLE_ENTITY, pos, state);
		this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
		this.fluid = Fluid.EMPTY;
		this.teaTypes = new ArrayList<>();
		this.propertyDelegate = new PropertyDelegate() {
			public int get(int index) {
				return switch(index) {
					case 0 -> KettleBlockEntity.this.brewTime;
					case 1 -> KettleBlockEntity.this.fluidLevel;
					case 2 -> KettleBlockEntity.this.fluid.toIndex();
					case 3 -> KettleBlockEntity.this.isHot ? 1 : 0;
					case 4 -> TeaHelper.getColor(KettleBlockEntity.this.teaTypes);
					default -> 0;
				};
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
						KettleBlockEntity.this.fluid = Fluid.byIndex(value);
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
		return new TranslatableText("container." + Culinaire.MOD_DATA.getModName() + ".kettle");
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new KettleScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}

	@Override
	public int size() {
		return this.inventory.size();
	}

	public Fluid getFluid() {
		return this.fluid;
	}

	public boolean addWater(int amount) {
		byte fLevel = (byte) (this.fluidLevel + amount);
		if(fLevel <= 3) {
			this.fluid = Fluid.WATER;
			this.fluidLevel = fLevel;
			return true;
		}
		return false;
	}

	public enum Fluid {
		EMPTY(0, "empty"),
		WATER(1, "water"),
		TEA(2, "tea");

		private final int index;
		private final String string;

		Fluid(final int index, final String string) {
			this.index= index;
			this.string = string;
		}

		@Override
		public String toString() {
			return string;
		}

		public int toIndex() {
			return index;
		}

		public static Fluid byString(String s) {
			for (Fluid f : Fluid.values()) {
				if (f.string.equals(s)) {
					return f;
				}
			}
			return Fluid.EMPTY;
		}

		public static Fluid byIndex(int i) {
			for (Fluid f : Fluid.values()) {
				if (f.index == i) {
					return f;
				}
			}
			return Fluid.EMPTY;
		}
	}

	public boolean removeFluid(int amount) {
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

	public static void tick(World world, BlockPos pos, BlockState state, KettleBlockEntity blockEntity) {
		ItemStack stack = blockEntity.inventory.get(0);
		blockEntity.isHot = blockEntity.shouldGetHot(world);
		boolean canBrew = blockEntity.canBrew(stack);
		boolean isBrewing = blockEntity.brewTime > 0;
		if(isBrewing) {
			--blockEntity.brewTime;
			boolean isFinishedBrewing = blockEntity.brewTime == 0;
			if(canBrew && isFinishedBrewing) {
				blockEntity.brew(world, stack);
				blockEntity.markDirty();
			}
			else if(!canBrew) {
				blockEntity.brewTime = 0;
				blockEntity.markDirty();
			}
			else if(blockEntity.itemBrewing != stack.getItem()) {
				blockEntity.brewTime = 0;
				blockEntity.markDirty();
			}
		}
		else if(canBrew) {
			blockEntity.brewTime = 800;
			blockEntity.itemBrewing = stack.getItem();
			blockEntity.markDirty();
		}
		if(blockEntity.fluid != Fluid.EMPTY && blockEntity.fluidLevel == 0) {
			blockEntity.fluid = Fluid.EMPTY;
			blockEntity.markDirty();
		}
	}

	public boolean isHot() {
		return isHot;
	}

	public boolean shouldGetHot(World world) {
		for(Direction direction : Direction.values()) {
			if(isHotBlock(world, this.getPos().offset(direction))) {
				return true;
			}
		}
		return world.getDimension().isUltrawarm();
	}

	public boolean isHotBlock(World world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		return CampfireBlock.isLitCampfire(blockState) || blockState.isOf(Blocks.MAGMA_BLOCK) || blockState.isOf(Blocks.LAVA);
	}

	private boolean canBrew(ItemStack stack) {
		if(stack.isEmpty()) {
			return false;
		}
		else if(stack.getItem() instanceof TeaBagItem) {
			return !TeaHelper.getTeaTypesByCompound(stack.getTag()).isEmpty() && fluid == Fluid.WATER && fluidLevel >= 1 && this.isHot;
		}
		else {
			return false;
		}
	}

	private void brew(World world, ItemStack stack) {
		this.fluid = Fluid.TEA;
		this.teaTypes = TeaHelper.getTeaTypesByCompound(stack.getTag());
		stack.decrement(1);
		BlockPos blockPos = this.getPos();
		if(stack.getItem().hasRecipeRemainder()) {
			ItemStack remainderStack = new ItemStack(stack.getItem().getRecipeRemainder());
			if(stack.isEmpty()) {
				stack = remainderStack;
			}
			else if(!world.isClient) {
				ItemScatterer.spawn(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), remainderStack);
			}
		}
		world.playSound(null, pos, CulinaireTeaBundle.KETTLE_BREW_SOUND.getSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
		this.inventory.set(0, stack);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
		Inventories.readNbt(tag, this.inventory);
		this.brewTime = tag.getShort("BrewTime");
		this.fluid = Fluid.byString(tag.getString("Fluid"));
		this.fluidLevel = tag.getByte("FluidLevel");
		NbtList teaTypeList = tag.getList("TeaTypes", 10);
		if(!teaTypeList.isEmpty()) {
			for(int i = 0; i < teaTypeList.size(); ++i) {
				NbtCompound typeTag = teaTypeList.getCompound(i);
				TeaType teaType = new TeaType(typeTag.getString("Strength"), typeTag.getString("Flavor"));
				if(teaType.isCorrect()) {
					teaTypes.add(teaType);
				}
			}
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		Inventories.writeNbt(tag, this.inventory);
		tag.putShort("BrewTime", (short) this.brewTime);
		tag.putString("Fluid", this.fluid.toString());
		tag.putByte("FluidLevel", (byte) this.fluidLevel);
		NbtList listTag = new NbtList();
		for(TeaType teaType : teaTypes) {
			NbtCompound typeTag = new NbtCompound();
			typeTag.putString("Flavor", teaType.getFlavor().getName());
			typeTag.putString("Strength", teaType.getStrength().getName());
			listTag.add(typeTag);
		}
		tag.put("TeaTypes", listTag);
		return tag;
	}
}
