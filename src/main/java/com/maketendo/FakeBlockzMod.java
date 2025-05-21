package com.maketendo;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeBlockzMod implements ModInitializer {
	public static final String MOD_ID = "fakeblocks";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		FakeBlockzBlocks.registerGhostBlocks();

		LOGGER.info("Successfully Initialized Fake Blockz Mod!");
	}
}