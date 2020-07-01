package hugman.common_expansion.objects.block.block_entity;

import hugman.common_expansion.init.data.CEBlockEntityTypes;
import hugman.common_expansion.objects.block.MilkCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class MilkCauldronBlockEntity extends BlockEntity implements Tickable {
	private int coagulationTime;

	public MilkCauldronBlockEntity() {
		super(CEBlockEntityTypes.MILK_CAULDRON);
	}

	@Override
	public void tick() {
		BlockState blockState = this.getCachedState();
		boolean isReady = blockState.get(MilkCauldronBlock.READY);
		if(!isReady) {
			coagulationTime++;
		}
		if(coagulationTime >= 48000) {
			this.setReadyProperty(blockState, true);
		}
	}
	private void setReadyProperty(BlockState state, boolean ready) {
		this.world.setBlockState(this.getPos(), state.with(MilkCauldronBlock.READY, Boolean.valueOf(ready)), 3);
	}


	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.coagulationTime = tag.getInt("CoagulationTime");
	}

	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putInt("CoagulationTime", this.coagulationTime);
		return tag;
	}
}
