package com.hugman.culinaire.objects.block.entity;

import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.init.TeaBundle;
import com.hugman.culinaire.init.data.CulinaireTags;
import com.hugman.culinaire.objects.block.KettleBlock;
import com.hugman.culinaire.objects.item.TeaBagItem;
import com.hugman.culinaire.objects.screen.handler.KettleScreenHandler;
import com.hugman.culinaire.objects.tea.TeaType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KettleBlockEntity extends LockableContainerBlockEntity implements SidedInventory {
	private static final int[] TOP_SLOTS = new int[]{0};
	protected final PropertyDelegate propertyDelegate;
	private DefaultedList<ItemStack> inventory;
	private ItemStack stackBrewing;
	private int brewTime;
	private int totalBrewTime;
	private int fluidLevel;
	private Fluid fluid;
	private List<TeaType> teaTypes;
	private boolean isHot;

	public KettleBlockEntity(BlockPos pos, BlockState state) {
		super(TeaBundle.KETTLE_ENTITY, pos, state);
		this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
		this.stackBrewing = ItemStack.EMPTY;
		this.fluid = Fluid.EMPTY;
		this.teaTypes = new ArrayList<>();
		this.propertyDelegate = new PropertyDelegate() {
			public int get(int index) {
				return switch(index) {
					case 0 -> KettleBlockEntity.this.brewTime;
					case 1 -> KettleBlockEntity.this.totalBrewTime;
					case 2 -> KettleBlockEntity.this.fluidLevel;
					case 3 -> KettleBlockEntity.this.fluid.ordinal();
					case 4 -> KettleBlockEntity.this.isHot ? 1 : 0;
					case 5 -> TeaType.getColor(KettleBlockEntity.this.teaTypes);
					default -> 0;
				};
			}

			public void set(int index, int value) {
				switch(index) {
					case 0:
						KettleBlockEntity.this.brewTime = value;
						break;
					case 1:
						KettleBlockEntity.this.totalBrewTime = value;
						break;
					case 2:
						KettleBlockEntity.this.fluidLevel = value;
						break;
					case 3:
						KettleBlockEntity.this.fluid = Fluid.byIndex(value);
						break;
					case 4:
						KettleBlockEntity.this.isHot = value == 1;
						break;
					case 5:
						break;
				}

			}

			public int size() {
				return 6;
			}
		};
	}

	public static void serverTick(World world, BlockPos pos, BlockState state, KettleBlockEntity kettle) {
		ItemStack stack = kettle.inventory.get(0);
		kettle.isHot = isSurroundedByHotBlocks(world, pos);
		boolean canBrew = kettle.canBrew(stack);
		if(kettle.brewTime > 0) {
			--kettle.brewTime;
			boolean isFinishedBrewing = kettle.brewTime == 0;
			if(canBrew && isFinishedBrewing) {
				kettle.brew(world, stack);
				kettle.markDirty();
			}
			else if(!canBrew) {
				kettle.brewTime = 0;
				kettle.markDirty();
			}
			else if(!ItemStack.areNbtEqual(kettle.stackBrewing, stack)) {
				kettle.brewTime = 0;
				kettle.markDirty();
			}
			float brewProgress = (float) (kettle.totalBrewTime - kettle.brewTime) / kettle.totalBrewTime;
			if(world.random.nextFloat() < brewProgress) produceSteam(world, pos, state);
		}
		else if(canBrew) {
			kettle.totalBrewTime = kettle.getBrewTime(stack);
			kettle.brewTime = kettle.totalBrewTime;
			kettle.stackBrewing = stack;
			kettle.markDirty();
		}
		if(kettle.fluid != Fluid.EMPTY && kettle.fluidLevel == 0) {
			kettle.fluid = Fluid.EMPTY;
			kettle.markDirty();
		}
	}

	private static boolean isSurroundedByHotBlocks(World world, BlockPos pos) {
		for(Direction direction : Direction.values()) {
			if(isHotBlock(world.getBlockState(pos.offset(direction)))) {
				return true;
			}
		}
		return world.getDimension().ultrawarm();
	}

	public static boolean isHotBlock(BlockState state) {
		return state.isIn(CulinaireTags.Blocks.KETTLE_HOT_BLOCKS) && (!state.contains(Properties.LIT) || state.get(Properties.LIT));
	}

	public static void produceSteam(World world, BlockPos pos, BlockState state) {
		Direction direction = state.get(KettleBlock.FACING);
		double offsetX = (double) pos.getX() + 0.5D;
		double offsetY = (double) pos.getY() + 0.5D;
		double offsetZ = (double) pos.getZ() + 0.5D;
		if(world instanceof ServerWorld serverWorld) {
			serverWorld.spawnParticles(ParticleTypes.SMOKE, offsetX + direction.getOffsetX() * 0.48D, offsetY, offsetZ + direction.getOffsetZ() * 0.48D, 1, direction.getOffsetX() * 0.05, 0.0D, direction.getOffsetZ() * 0.05, 0.01D);
		}
	}

	@Override
	protected Text getContainerName() {
		return Text.translatable("container." + Culinaire.MOD_DATA.getModName() + ".kettle");
	}

	private void brew(World world, ItemStack stack) {
		this.fluid = Fluid.TEA;
		this.teaTypes = TeaType.fromStack(stack);
		stack.decrement(1);
		if(stack.getItem().hasRecipeRemainder()) {
			ItemStack remainderStack = new ItemStack(stack.getItem().getRecipeRemainder());
			if(stack.isEmpty()) {
				stack = remainderStack;
			}
			else if(!world.isClient) {
				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), remainderStack);
			}
		}
		world.playSound(null, pos, TeaBundle.KETTLE_BREW_SOUND.getSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
		this.inventory.set(0, stack);
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new KettleScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
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
	public ItemStack getStack(int slot) {
		return slot >= 0 && slot < this.inventory.size() ? this.inventory.get(slot) : ItemStack.EMPTY;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		if(slot >= 0 && slot < this.inventory.size()) {
			this.inventory.set(slot, stack);
		}
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
	public void clear() {
		this.inventory.clear();
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
			return !TeaType.fromStack(stack).isEmpty();
		}
		else {
			return false;
		}
	}

	public boolean canBrew(ItemStack stack) {
		if(stack.isEmpty()) {
			return false;
		}
		else if(stack.getItem() instanceof TeaBagItem) {
			return !TeaType.fromStack(stack).isEmpty() && this.fluid == Fluid.WATER && this.fluidLevel >= 1 && this.isHot;
		}
		else {
			return false;
		}
	}

	public int getBrewTime(ItemStack stack) {
		List<TeaType> teaTypeList = TeaType.fromStack(stack);
		if(!teaTypeList.isEmpty()) {
			return teaTypeList.stream().mapToInt(value -> value.potency().brewTime()).sum();
		}
		return 0;
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
				TeaType teaType = TeaType.fromNbt(typeTag);
				if(teaType != null) {
					teaTypes.add(teaType);
				}
			}
		}
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		Inventories.writeNbt(tag, this.inventory);
		tag.putShort("BrewTime", (short) this.brewTime);
		tag.putString("Fluid", this.fluid.toString());
		tag.putByte("FluidLevel", (byte) this.fluidLevel);
		NbtList listTag = new NbtList();
		for(TeaType teaType : teaTypes) {
			listTag.add(teaType.toNbt());
		}
		tag.put("TeaTypes", listTag);
	}

	public enum Fluid {
		EMPTY, WATER, TEA;

		public static Fluid byString(String s) {
			for(Fluid f : Fluid.values()) {
				if(f.name().toLowerCase(Locale.ROOT).equals(s.toLowerCase(Locale.ROOT))) {
					return f;
				}
			}
			return Fluid.EMPTY;
		}

		public static Fluid byIndex(int i) {
			for(Fluid f : Fluid.values()) {
				if(f.ordinal() == i) {
					return f;
				}
			}
			return Fluid.EMPTY;
		}

		@Override
		public String toString() {
			return name().toLowerCase(Locale.ROOT);
		}
	}
}
