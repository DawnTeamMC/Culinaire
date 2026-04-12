package fr.hugman.culinaire.block.entity;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.block.KettleBlock;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.TeaTypesComponent;
import fr.hugman.culinaire.screen.KettleScreenHandler;
import fr.hugman.culinaire.sound.CulinaireSoundEvents;
import fr.hugman.culinaire.tag.CulinaireBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import javax.annotation.Nullable;
import java.util.Locale;

public class KettleBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    private static final int[] TOP_SLOTS = new int[]{0};
    protected final ContainerData propertyDelegate;
    private NonNullList<ItemStack> inventory;
    private ItemStack stackBrewing;
    private int brewTime;
    private int totalBrewTime;
    private int fluidLevel;
    private Fluid fluid;
    private TeaTypesComponent teaTypes;
    private boolean isHot;

    public KettleBlockEntity(BlockPos pos, BlockState state) {
        super(CulinaireBlockEntityTypes.KETTLE, pos, state);
        this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
        this.stackBrewing = ItemStack.EMPTY;
        this.fluid = Fluid.EMPTY;
        this.teaTypes = TeaTypesComponent.DEFAULT;
        this.propertyDelegate = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> KettleBlockEntity.this.brewTime;
                    case 1 -> KettleBlockEntity.this.totalBrewTime;
                    case 2 -> KettleBlockEntity.this.fluidLevel;
                    case 3 -> KettleBlockEntity.this.fluid.ordinal();
                    case 4 -> KettleBlockEntity.this.isHot ? 1 : 0;
                    case 5 -> KettleBlockEntity.this.teaTypes.getColor(-13083194);
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

            public int getCount() {
                return 6;
            }
        };
    }

    public static void serverTick(Level world, BlockPos pos, BlockState state, KettleBlockEntity kettle) {
        ItemStack stack = kettle.inventory.get(0);
        kettle.isHot = isSurroundedByHotBlocks(world, pos);
        boolean canBrew = kettle.canBrew(stack);
        if (kettle.brewTime > 0) {
            --kettle.brewTime;
            boolean isFinishedBrewing = kettle.brewTime == 0;
            if (canBrew && isFinishedBrewing) {
                kettle.brew(world, stack);
                kettle.setChanged();
            } else if (!canBrew) {
                kettle.brewTime = 0;
                kettle.setChanged();
            } else if (!ItemStack.isSameItem(kettle.stackBrewing, stack)) {
                kettle.brewTime = 0;
                kettle.setChanged();
            }
            float brewProgress = (float) (kettle.totalBrewTime - kettle.brewTime) / kettle.totalBrewTime;
            if (world.random.nextFloat() < brewProgress) produceSteam(world, pos, state);
        } else if (canBrew) {
            kettle.totalBrewTime = kettle.getBrewTime(stack);
            kettle.brewTime = kettle.totalBrewTime;
            kettle.stackBrewing = stack;
            kettle.setChanged();
        }
        if (kettle.fluid != Fluid.EMPTY && kettle.fluidLevel == 0) {
            kettle.fluid = Fluid.EMPTY;
            kettle.setChanged();
        }
    }

    private static boolean isSurroundedByHotBlocks(Level world, BlockPos pos) {
        //TODO: environmental attribute?
        for (Direction direction : Direction.values()) {
            if (isHotBlock(world.getBlockState(pos.relative(direction)))) {
                return true;
            }
        }
        return world.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, pos);
    }

    public static boolean isHotBlock(BlockState state) {
        return state.is(CulinaireBlockTags.KETTLE_HOT_BLOCKS) && (!state.hasProperty(BlockStateProperties.LIT) || state.getValue(BlockStateProperties.LIT));
    }

    public static void produceSteam(Level world, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(KettleBlock.FACING);
        double offsetX = (double) pos.getX() + 0.5D;
        double offsetY = (double) pos.getY() + 0.5D;
        double offsetZ = (double) pos.getZ() + 0.5D;
        if (world instanceof ServerLevel serverWorld) {
            serverWorld.sendParticles(ParticleTypes.SMOKE, offsetX + direction.getStepX() * 0.48D, offsetY, offsetZ + direction.getStepZ() * 0.48D, 1, direction.getStepX() * 0.05, 0.0D, direction.getStepZ() * 0.05, 0.01D);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container." + Culinaire.MOD_ID + ".kettle");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    private void brew(Level world, ItemStack stack) {
        this.fluid = Fluid.TEA;
        this.teaTypes = stack.get(CulinaireComponentTypes.TEA_TYPES);
        stack.shrink(1);
        var remainder = stack.getRecipeRemainder();
        if (!world.isClientSide() && !remainder.isEmpty()) {
            Containers.dropItemStack(world, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), remainder);
        }
        world.playSound(null, worldPosition, CulinaireSoundEvents.KETTLE_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
        this.inventory.set(0, stack);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new KettleScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public int getContainerSize() {
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
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) {
            return TOP_SLOTS;
        } else {
            return new int[]{};
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < this.inventory.size() ? this.inventory.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.inventory.size()) {
            this.inventory.set(slot, stack);
        }
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.inventory, slot);
    }

    @Override
    public void clearContent() {
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
                this.teaTypes = TeaTypesComponent.DEFAULT;
            }
            return true;
        }
        return false;
    }

    public TeaTypesComponent getTeaTypes() {
        return this.teaTypes;
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (stack.has(CulinaireComponentTypes.TEA_TYPES)) {
            return !stack.get(CulinaireComponentTypes.TEA_TYPES).isEmpty();
        } else {
            return false;
        }
    }

    public boolean canBrew(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        } else if (stack.has(CulinaireComponentTypes.TEA_TYPES)) {
            return !stack.get(CulinaireComponentTypes.TEA_TYPES).isEmpty() && this.fluid == Fluid.WATER && this.fluidLevel >= 1 && this.isHot;
        } else {
            return false;
        }
    }

    public int getBrewTime(ItemStack stack) {
        TeaTypesComponent component = stack.get(CulinaireComponentTypes.TEA_TYPES);
        if (!component.isEmpty()) {
            return component.getBrewTime();
        }
        return 0;
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(view, this.inventory);
        this.brewTime = view.getShortOr("brew_time", (short)0);
        this.fluid = view.getString("fluid").map(Fluid::byString).orElse(Fluid.EMPTY);
        this.fluidLevel = view.getByteOr("fluid_level", (byte)0);

        this.teaTypes = view.read("tea_types", TeaTypesComponent.CODEC).orElse(TeaTypesComponent.DEFAULT);
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        ContainerHelper.saveAllItems(view, this.inventory);
        view.putShort("brew_time", (short) this.brewTime);
        view.putString("fluid", this.fluid.toString());
        view.putByte("fluid_level", (byte) this.fluidLevel);
        view.store("tea_types", TeaTypesComponent.CODEC, this.teaTypes);
    }

    //Todo: implement readComponents + addComponents

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
