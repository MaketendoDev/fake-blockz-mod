package com.maketendo.renderers;

import com.maketendo.blockentities.FakeBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class FakeBlockEntityRenderer implements BlockEntityRenderer<FakeBlockEntity> {

    public FakeBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(FakeBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        Identifier mimickedBlockId = entity.getMimickedBlockId();
        if (mimickedBlockId == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        Block block = client.world.getRegistryManager().get(RegistryKeys.BLOCK).get(mimickedBlockId);
        if (block == null) return;

        BlockState state = block.getDefaultState();
        if (state.getRenderType() != BlockRenderType.MODEL) return;

        matrices.push();

        RenderLayer layer = RenderLayers.getBlockLayer(state);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(layer);

        long seed = entity.getPos().asLong();
        Random random = Random.create(seed);

        client.getBlockRenderManager().renderBlock(
                state,
                entity.getPos(),
                client.world,
                matrices,
                vertexConsumer,
                true,
                random
        );

        matrices.pop();
    }
}
