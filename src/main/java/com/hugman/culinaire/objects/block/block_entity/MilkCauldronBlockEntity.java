package com.hugman.culinaire.objects.block.block_entity;

import com.hugman.culinaire.init.CulinaireBlocks;
import com.hugman.culinaire.objects.block.MilkCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

import java.util.Random;

public class MilkCauldronBlockEntity extends BlockEntity implements Tickable {
	private int coagulationTime;
	private int maxCoagulationTime;

	public MilkCauldronBlockEntity() {
		super(CulinaireBlocks.MILK_CAULDRON_ENTITY);
		Random random = new Random();
		this.maxCoagulationTime = random.nextInt(16000) + 32000;
	}

	@Override
	public void tick() {
		BlockState blockState = this.getCachedState();
		if(!this.world.isClient) {
			if(coagulationTime >= maxCoagulationTime && this.world.getBlockState(this.getPos()).get(MilkCauldronBlock.LEVEL) == 3) {
				this.setCoagulatedProperty(blockState, true);
			}
			if(coagulationTime < maxCoagulationTime) {
				coagulationTime++;
			}
		}
	}

	private void setCoagulatedProperty(BlockState state, boolean coagulated) {
		this.world.setBlockState(this.getPos(), state.with(MilkCauldronBlock.COAGULATED, Boolean.valueOf(coagulated)), 3);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.coagulationTime = tag.getInt("CoagulationTime");
		this.maxCoagulationTime = tag.getInt("MaxCoagulationTime");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putInt("CoagulationTime", this.coagulationTime);
		tag.putInt("MaxCoagulationTime", this.maxCoagulationTime);
		return tag;
	}
}
