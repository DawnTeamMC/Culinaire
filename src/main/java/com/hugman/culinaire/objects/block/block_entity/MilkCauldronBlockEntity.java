package com.hugman.culinaire.objects.block.block_entity;

import com.hugman.culinaire.init.CulinaireBlocks;
import com.hugman.culinaire.objects.block.MilkCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class MilkCauldronBlockEntity extends BlockEntity {
	private int coagulationTime;
	private int maxCoagulationTime;

	public MilkCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(CulinaireBlocks.MILK_CAULDRON_ENTITY, pos, state);
		Random random = new Random();
		this.maxCoagulationTime = random.nextInt(16000) + 32000;
	}

	public static void tick(World world, BlockPos pos, BlockState state, MilkCauldronBlockEntity blockEntity) {
		if(!world.isClient) {
			if(blockEntity.coagulationTime >= blockEntity.maxCoagulationTime && state.get(MilkCauldronBlock.LEVEL) == 3) {
				world.setBlockState(pos, state.with(MilkCauldronBlock.COAGULATED, true), 3);
			}
			if(blockEntity.coagulationTime < blockEntity.maxCoagulationTime) {
				blockEntity.coagulationTime++;
			}
		}
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		this.coagulationTime = tag.getInt("CoagulationTime");
		this.maxCoagulationTime = tag.getInt("MaxCoagulationTime");
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		tag.putInt("CoagulationTime", this.coagulationTime);
		tag.putInt("MaxCoagulationTime", this.maxCoagulationTime);
		return tag;
	}
}
