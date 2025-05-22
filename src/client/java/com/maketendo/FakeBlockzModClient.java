package com.maketendo;

import com.maketendo.renderers.FakeBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;


public class FakeBlockzModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.register(
				FakeBlockzBlocks.GHOST_ENTITY_TYPE,
				FakeBlockEntityRenderer::new
		);

	}
}