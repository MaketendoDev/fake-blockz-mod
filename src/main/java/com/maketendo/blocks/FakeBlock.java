package com.maketendo.blocks;

import com.maketendo.FakeBlockzBlocks;
import com.maketendo.blockentities.FakeBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FakeBlock extends Block implements BlockEntityProvider {
    public static final EnumProperty<Axis> AXIS = Properties.AXIS;
    public static final BooleanProperty LIT = Properties.LIT;
    public static final BooleanProperty BERRIES = Properties.BERRIES;
    public static final IntProperty CANDLES = Properties.CANDLES;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final IntProperty CHARGES = Properties.CHARGES;
    public static final IntProperty LEVEL = Properties.LEVEL_1_8;

    private final Identifier mimickedId;
    private final List<Property<?>> properties = new ArrayList<>();

    public FakeBlock(AbstractBlock.Settings settings, Identifier mimickedId) {
        super(settings.noCollision().nonOpaque().dropsNothing());
        this.mimickedId = mimickedId;

        Block mimickedBlock = Registries.BLOCK.get(mimickedId);
        BlockState baseState = getStateManager().getDefaultState();
        StateManager<Block, BlockState> stateManager = mimickedBlock.getStateManager();

        addPropertyIfPresent(stateManager, AXIS, Axis.Y);
        addPropertyIfPresent(stateManager, LIT, false);
        addPropertyIfPresent(stateManager, BERRIES, false);
        addPropertyIfPresent(stateManager, CANDLES, 1);
        addPropertyIfPresent(stateManager, WATERLOGGED, false);
        addPropertyIfPresent(stateManager, CHARGES, 0);
        addPropertyIfPresent(stateManager, LEVEL, 1);

        this.setDefaultState(baseState);
    }

    // Generic helper method to conditionally add a property
    private <T extends Comparable<T>> void addPropertyIfPresent(
            StateManager<Block, BlockState> stateManager,
            Property<T> property,
            T defaultValue
    ) {
        if (stateManager.getProperties().contains(property)) {
            properties.add(property);
            setDefaultState(getDefaultState().with(property, defaultValue));
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        for (Property<?> property : properties) {
            builder.add(property);
        }
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
                    List<BlockState> ghostStates = GhostBlockUtil.getGhostBlockStates(originalBlock);
                    for (BlockState ghostState : ghostStates) {
                        world.syncWorldEvent(2001, pos, Block.getRawIdFromState(ghostState));
                    }
                }
            }
        }
        super.onBreak(world, pos, state, player);
    }

    public static class GhostBlockUtil {
        public static List<BlockState> getGhostBlockStates(Block block) {
            List<BlockState> results = new ArrayList<>();

            for (BlockState state : block.getStateManager().getStates()) {
                boolean shouldAdd = false;

                if (state.contains(Properties.LIT) && state.get(Properties.LIT)) shouldAdd = true;
                if (state.contains(Properties.POWERED) && state.get(Properties.POWERED)) shouldAdd = true;
                if (state.contains(Properties.OPEN) && state.get(Properties.OPEN)) shouldAdd = true;
                if (state.contains(Properties.BERRIES) && state.get(Properties.BERRIES)) shouldAdd = true;

                if (shouldAdd) {
                    results.add(state);
                }
            }

            if (results.isEmpty()) {
                results.add(block.getDefaultState());
            }

            return results;
        }
    }
}
