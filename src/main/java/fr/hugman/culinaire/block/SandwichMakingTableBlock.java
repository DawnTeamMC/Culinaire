package fr.hugman.culinaire.block;

import com.mojang.serialization.MapCodec;
import fr.hugman.culinaire.world.menu.SandwichMakingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SandwichMakingTableBlock extends Block {
	public static final MapCodec<SandwichMakingTableBlock> CODEC = simpleCodec(SandwichMakingTableBlock::new);
	private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");

	@Override
	public MapCodec<? extends SandwichMakingTableBlock> codec() {
		return CODEC;
	}

	public SandwichMakingTableBlock(final Properties properties) {
		super(properties);
	}

	@Override
	protected InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult) {
		if (!level.isClientSide()) {
			player.openMenu(state.getMenuProvider(level, pos));
			player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	protected MenuProvider getMenuProvider(final BlockState state, final Level level, final BlockPos pos) {
		return new SimpleMenuProvider(
			(containerId, inventory, player) -> new SandwichMakingMenu(containerId, inventory, level, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE
		);
	}
}
