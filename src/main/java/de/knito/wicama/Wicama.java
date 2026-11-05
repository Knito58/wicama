package de.knito.wicama;

import java.util.function.Function;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class Wicama implements ModInitializer {
	public static final String MOD_ID = "wicama";

	public static final Block BUILDING_BLOCK_OPAQUE = register("building_block_opaque", CageBlocks.Opaque::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.STONE)
					.strength(CageBlocks.HARDNESS, CageBlocks.BLAST_RESISTANCE)
					.requiresCorrectToolForDrops()
					.pushReaction(PushReaction.IMMOVEABLE)
					.sound(SoundType.STONE));

	public static final Block BUILDING_BLOCK_TRANSPARENT = register("building_block_transparent", CageBlocks.Transparent::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.NONE)
					.strength(CageBlocks.HARDNESS, CageBlocks.BLAST_RESISTANCE)
					.requiresCorrectToolForDrops()
					.pushReaction(PushReaction.IMMOVEABLE)
					.sound(SoundType.GLASS)
					.noOcclusion()
					.isValidSpawn(Blocks::never)
					.isRedstoneConductor(Blocks::never)
					.isSuffocating(Blocks::never)
					.isViewBlocking((state, level, pos, box) -> false));

	private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
		Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
		Block block = Registry.register(BuiltInRegistries.BLOCK, blockKey, factory.apply(properties.setId(blockKey)));

		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
		Registry.register(BuiltInRegistries.ITEM, itemKey,
				new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
		return block;
	}

	@Override
	public void onInitialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(output -> {
			output.accept(BUILDING_BLOCK_OPAQUE);
			output.accept(BUILDING_BLOCK_TRANSPARENT);
		});
	}
}
