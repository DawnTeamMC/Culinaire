package fr.hugman.culinaire.block.entity;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.block.KettleBlock;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.screen.KettleScreenHandler;
import fr.hugman.culinaire.sound.CulinaireSoundEvents;
import fr.hugman.culinaire.tag.CulinaireBlockTags;
import fr.hugman.culinaire.tea.TeaHelper;
import fr.hugman.culinaire.tea.TeaType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
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
        super(CulinaireBlockEntityTypes.KETTLE, pos, state);
        this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.stackBrewing = ItemStack.EMPTY;
        this.fluid = Fluid.EMPTY;
        this.teaTypes = new ArrayList<>();
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> KettleBlockEntity.this.brewTime;
                    case 1 -> KettleBlockEntity.this.totalBrewTime;
                    case 2 -> KettleBlockEntity.this.fluidLevel;
                    case 3 -> KettleBlockEntity.this.fluid.ordinal();
                    case 4 -> KettleBlockEntity.this.isHot ? 1 : 0;
                    case 5 -> TeaHelper.getColor(KettleBlockEntity.this.teaTypes);
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
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
        if (kettle.brewTime > 0) {
            --kettle.brewTime;
            boolean isFinishedBrewing = kettle.brewTime == 0;
            if (canBrew && isFinishedBrewing) {
                kettle.brew(world, stack);
                kettle.markDirty();
            } else if (!canBrew) {
                kettle.brewTime = 0;
                kettle.markDirty();
            } else if (!ItemStack.areItemsEqual(kettle.stackBrewing, stack)) {
                kettle.brewTime = 0;
                kettle.markDirty();
            }
            float brewProgress = (float) (kettle.totalBrewTime - kettle.brewTime) / kettle.totalBrewTime;
            if (world.random.nextFloat() < brewProgress) produceSteam(world, pos, state);
        } else if (canBrew) {
            kettle.totalBrewTime = kettle.getBrewTime(stack);
            kettle.brewTime = kettle.totalBrewTime;
            kettle.stackBrewing = stack;
            kettle.markDirty();
        }
        if (kettle.fluid != Fluid.EMPTY && kettle.fluidLevel == 0) {
            kettle.fluid = Fluid.EMPTY;
            kettle.markDirty();
        }
    }

    private static boolean isSurroundedByHotBlocks(World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (isHotBlock(world.getBlockState(pos.offset(direction)))) {
                return true;
            }
        }
        return world.getDimension().ultrawarm();
    }

    public static boolean isHotBlock(BlockState state) {
        return state.isIn(CulinaireBlockTags.KETTLE_HOT_BLOCKS) && (!state.contains(Properties.LIT) || state.get(Properties.LIT));
    }

    public static void produceSteam(World world, BlockPos pos, BlockState state) {
        Direction direction = state.get(KettleBlock.FACING);
        double offsetX = (double) pos.getX() + 0.5D;
        double offsetY = (double) pos.getY() + 0.5D;
        double offsetZ = (double) pos.getZ() + 0.5D;
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.SMOKE, offsetX + direction.getOffsetX() * 0.48D, offsetY, offsetZ + direction.getOffsetZ() * 0.48D, 1, direction.getOffsetX() * 0.05, 0.0D, direction.getOffsetZ() * 0.05, 0.01D);
        }
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container." + Culinaire.MOD_ID + ".kettle");
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    private void brew(World world, ItemStack stack) {
        this.fluid = Fluid.TEA;
        this.teaTypes = stack.get(CulinaireComponentTypes.TEA_CONTENTS);
        stack.decrement(1);
        var remainder = stack.getRecipeRemainder();
        if (!world.isClient && !remainder.isEmpty()) {
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), remainder);
        }
        world.playSound(null, pos, CulinaireSoundEvents.KETTLE_BREW, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.UP) {
            return TOP_SLOTS;
        } else {
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
        if (slot >= 0 && slot < this.inventory.size()) {
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
        if (fLevel <= 3) {
            this.fluid = Fluid.WATER;
            this.fluidLevel = fLevel;
            return true;
        }
        return false;
    }

    public boolean removeFluid(int amount) {
        byte fLevel = (byte) (this.fluidLevel - amount);
        if (fLevel >= 0) {
            this.fluidLevel = fLevel;
            if (fLevel == 0) {
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
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (stack.contains(CulinaireComponentTypes.TEA_CONTENTS)) {
            return !stack.get(CulinaireComponentTypes.TEA_CONTENTS).isEmpty();
        } else {
            return false;
        }
    }

    public boolean canBrew(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        } else if (stack.contains(CulinaireComponentTypes.TEA_CONTENTS)) {
            return !stack.get(CulinaireComponentTypes.TEA_CONTENTS).isEmpty() && this.fluid == Fluid.WATER && this.fluidLevel >= 1 && this.isHot;
        } else {
            return false;
        }
    }

    public int getBrewTime(ItemStack stack) {
        List<TeaType> teaTypeList = stack.get(CulinaireComponentTypes.TEA_CONTENTS);
        if (!teaTypeList.isEmpty()) {
            return teaTypeList.stream().mapToInt(TeaType::getBrewTime).sum();
        }
        return 0;
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory, registries);
        this.brewTime = nbt.getShort("BrewTime");
        this.fluid = Fluid.byString(nbt.getString("Fluid"));
        this.fluidLevel = nbt.getByte("FluidLevel");

        if (nbt.contains("tea_types")) {
            TeaType.LIST_CODEC
                    .parse(registries.getOps(NbtOps.INSTANCE), nbt.get("tea_types"))
                    .resultOrPartial(error -> Culinaire.LOGGER.warn("Failed to load tea types: {}", error))
                    .ifPresent(components -> this.teaTypes = components);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        Inventories.writeNbt(nbt, this.inventory, registries);
        nbt.putShort("BrewTime", (short) this.brewTime);
        nbt.putString("Fluid", this.fluid.toString());
        nbt.putByte("FluidLevel", (byte) this.fluidLevel);

        if (!teaTypes.isEmpty()) {
            TeaType.LIST_CODEC
                    .encodeStart(registries.getOps(NbtOps.INSTANCE), this.teaTypes)
                    .resultOrPartial(snbt -> Culinaire.LOGGER.warn("Failed to save tea types: {}", snbt))
                    .ifPresent(element -> nbt.put("tea_types", element));
        }
    }

    public enum Fluid {
        EMPTY, WATER, TEA;

        public static Fluid byString(String s) {
            for (Fluid f : Fluid.values()) {
                if (f.name().toLowerCase(Locale.ROOT).equals(s.toLowerCase(Locale.ROOT))) {
                    return f;
                }
            }
            return Fluid.EMPTY;
        }

        public static Fluid byIndex(int i) {
            for (Fluid f : Fluid.values()) {
                if (f.ordinal() == i) {
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
