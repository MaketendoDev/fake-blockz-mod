package com.maketendo.mixin.client;

import com.maketendo.items.FakeBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V",
            at = @At("HEAD"), cancellable = true)
    private void renderFakeBlockInHand(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode,
                                       boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                       @Nullable World world, int light, int overlay, int seed, CallbackInfo ci) {

        if (!(stack.getItem() instanceof FakeBlockItem fakeBlockItem)) return;
        Block block = fakeBlockItem.getBlock();
        if (block == null) return;

        BlockState state = fakeBlockItem.getOriginalBlock().getDefaultState();
        MinecraftClient client = MinecraftClient.getInstance();
        BlockRenderManager renderManager = client.getBlockRenderManager();
        BakedModel model = renderManager.getModel(state);

        matrices.push();
        matrices.scale(0.25f, 0.3f, 0.3f);
        matrices.translate(-0.5f, -0.5f, -0.5f);

        RenderLayer layer = RenderLayers.getBlockLayer(state);
        VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();

        int lightLevel = light;
        if (world != null && entity != null) {
            BlockPos pos = entity.getBlockPos();
            lightLevel = WorldRenderer.getLightmapCoordinates(world, state, pos);
        }


        renderManager.getModelRenderer().render(
                matrices.peek(),
                immediate.getBuffer(layer),
                state,
                model,
                1.0f, 1.0f, 1.0f,
                lightLevel,
                overlay
        );

        immediate.draw();
        matrices.pop();
        ci.cancel();
    }

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At("HEAD"), cancellable = true)
    private void renderFakeBlockInGui(ItemStack stack, ModelTransformationMode mode, boolean leftHanded,
                                      MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                      int light, int overlay, BakedModel model, CallbackInfo ci) {

        if (!(stack.getItem() instanceof FakeBlockItem fakeBlockItem)) return;
        Block block = fakeBlockItem.getBlock();
        if (block == null) return;

        BlockState state = fakeBlockItem.getOriginalBlock().getDefaultState();
        MinecraftClient client = MinecraftClient.getInstance();
        BlockRenderManager renderManager = client.getBlockRenderManager();
        BakedModel blockModel = renderManager.getModel(state);

        matrices.push();

        matrices.translate(0.45, -0.25, 0.5);

        matrices.multiply(new Quaternionf().rotationXYZ(
                (float) Math.toRadians(30),
                (float) Math.toRadians(225),
                0f
        ));

        matrices.scale(0.6f, 0.6f, 0.6f);

        RenderLayer layer = RenderLayers.getBlockLayer(state);
        VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();

        renderManager.getModelRenderer().render(
                matrices.peek(),
                immediate.getBuffer(layer),
                state,
                blockModel,
                1.0f, 1.0f, 1.0f,
                0xF000F0,
                overlay
        );

        immediate.draw();
        matrices.pop();
        ci.cancel();
    }
}
