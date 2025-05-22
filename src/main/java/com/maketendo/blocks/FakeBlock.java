package com.maketendo.blocks;

import com.maketendo.FakeBlockzBlocks;
import com.maketendo.blockentities.FakeBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FakeBlock extends Block implements BlockEntityProvider {
    private final Identifier mimickedId;

    public FakeBlock(AbstractBlock.Settings settings, Identifier mimickedId) {
        super(settings.noCollision().nonOpaque().strength(0.0F));
        this.mimickedId = mimickedId;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        FakeBlockEntity entity = new FakeBlockEntity(pos, state);
        entity.setMimickedBlockId(mimickedId);
        return entity;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            Identifier originalId = FakeBlockzBlocks.getOriginalBlockId(this);
            if (originalId != null) {
                Registry<Block> blockRegistry = world.getRegistryManager().get(RegistryKeys.BLOCK);
                Block originalBlock = blockRegistry.get(originalId);

                if (originalBlock != null) {
                    world.syncWorldEvent(2001, pos, Block.getRawIdFromState(originalBlock.getDefaultState()));
                }
            }
        }

    }
}
