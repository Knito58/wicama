package de.knito.wicama;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Cage blocks break like smooth stone with a tool that can mine obsidian,
 * and as slowly as obsidian with anything weaker (and then drop nothing).
 */
public final class CageBlocks {
	/** Smooth stone hardness: correct tools mine the block at smooth stone speed. */
	public static final float HARDNESS = 2.0f;
	/** Same value as bedrock: no explosion can break the block. */
	public static final float BLAST_RESISTANCE = 3_600_000.0f;
	/** Obsidian hardness, used to slow down tools that are too weak. */
	private static final float WRONG_TOOL_HARDNESS = 50.0f;

	private CageBlocks() {
	}

	static float adjust(float vanillaProgress, BlockState state, Player player) {
		if (player.hasCorrectToolForDrops(state)) {
			return vanillaProgress;
		}
		return vanillaProgress * (HARDNESS / WRONG_TOOL_HARDNESS);
	}

	/** Opaque cage block. */
	public static class Opaque extends Block {
		public Opaque(Properties properties) {
			super(properties);
		}

		@Override
		protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
			return adjust(super.getDestroyProgress(state, player, level, pos), state, player);
		}
	}

	/** See-through cage block, glass-like (inner faces between two of them are culled). */
	public static class Transparent extends TransparentBlock {
		public Transparent(Properties properties) {
			super(properties);
		}

		@Override
		protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
			return adjust(super.getDestroyProgress(state, player, level, pos), state, player);
		}
	}
}
