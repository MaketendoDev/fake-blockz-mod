package com.maketendo;

import com.maketendo.blocks.FakeBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FakeBlockzMod implements ModInitializer {
	public static final String MOD_ID = "fakeblocks";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Registries.BLOCK.forEach(block -> tryRegisterGhost(block));

		RegistryEntryAddedCallback.event(Registries.BLOCK).register((rawId, id, block) -> {
			tryRegisterGhost(block);
		});

		LOGGER.info("Successfully Initialized Fake Blockz Mod!");
	}


	private void tryRegisterGhost(Block block) {
		Identifier id = Registries.BLOCK.getId(block);
		if (id != null && !id.getNamespace().equals(MOD_ID)) {
			FakeBlockzBlocks.registerGhostVersion(block, id);
		}
	}
}
