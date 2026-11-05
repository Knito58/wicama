package de.knito.wicama.gametest;

import de.knito.wicama.Wicama;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class WicamaGameTests {
	private static final Block[] CAGE = {Wicama.BUILDING_BLOCK_OPAQUE, Wicama.BUILDING_BLOCK_TRANSPARENT};

	private static float progress(GameTestHelper helper, Player player, Item tool, Block block, BlockPos rel) {
		helper.setBlock(rel, block);
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(tool));
		BlockPos abs = helper.absolutePos(rel);
		return helper.getLevel().getBlockState(abs).getDestroyProgress(player, helper.getLevel(), abs);
	}

	private static void assertClose(GameTestHelper helper, float expected, float actual, String what) {
		helper.assertTrue(Math.abs(expected - actual) < 1e-6f, what + ": expected " + expected + " but got " + actual);
	}

	@GameTest
	public void witherCannotBreakCage(GameTestHelper helper) {
		for (Block block : CAGE) {
			BlockState state = block.defaultBlockState();
			helper.assertTrue(state.is(BlockTags.WITHER_IMMUNE), block + " is not in minecraft:wither_immune");
			helper.assertTrue(!WitherBoss.canDestroy(state), "the wither can destroy " + block);
		}
		helper.succeed();
	}

	@GameTest
	public void miningSpeedAndDrops(GameTestHelper helper) {
		Player player = helper.makeMockPlayer(GameType.SURVIVAL);
		BlockPos pos = new BlockPos(1, 1, 1);
		for (Block block : CAGE) {
			// Obsidian-capable tools: drop, and as fast as on smooth stone.
			for (Item tool : new Item[] {Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE}) {
				float stone = progress(helper, player, tool, Blocks.SMOOTH_STONE, pos);
				float cage = progress(helper, player, tool, block, pos);
				assertClose(helper, stone, cage, block + " with " + tool + " vs smooth stone");
				helper.assertTrue(player.hasCorrectToolForDrops(block.defaultBlockState()), tool + " should harvest " + block);
			}
			// Weaker tools and the bare hand: no drop, and as slow as on obsidian.
			for (Item tool : new Item[] {Items.IRON_PICKAXE, Items.STONE_PICKAXE, Items.GOLDEN_PICKAXE, Items.AIR}) {
				float obsidian = progress(helper, player, tool, Blocks.OBSIDIAN, pos);
				float cage = progress(helper, player, tool, block, pos);
				assertClose(helper, obsidian, cage, block + " with " + tool + " vs obsidian");
				helper.assertTrue(!player.hasCorrectToolForDrops(block.defaultBlockState()), tool + " should not harvest " + block);
			}
		}
		helper.succeed();
	}

	@GameTest
	public void cageSurvivesExplosions(GameTestHelper helper) {
		BlockPos a = new BlockPos(1, 1, 1);
		BlockPos b = new BlockPos(3, 1, 1);
		helper.setBlock(a, Wicama.BUILDING_BLOCK_OPAQUE);
		helper.setBlock(b, Wicama.BUILDING_BLOCK_TRANSPARENT);
		BlockPos centre = helper.absolutePos(new BlockPos(2, 1, 1));
		// Stronger than a charged creeper, right between the two blocks.
		helper.getLevel().explode(null, centre.getX() + 0.5, centre.getY() + 0.5, centre.getZ() + 0.5, 8.0f, Level.ExplosionInteraction.TNT);
		helper.assertBlockPresent(Wicama.BUILDING_BLOCK_OPAQUE, a);
		helper.assertBlockPresent(Wicama.BUILDING_BLOCK_TRANSPARENT, b);
		helper.succeed();
	}
}
