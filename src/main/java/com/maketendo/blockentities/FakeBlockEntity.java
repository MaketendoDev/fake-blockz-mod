package com.maketendo.blockentities;

import com.maketendo.FakeBlockzBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class FakeBlockEntity extends BlockEntity {
    @Nullable
    private Identifier mimickedBlockId;

    public FakeBlockEntity(BlockPos pos, BlockState state) {
        super(FakeBlockzBlocks.GHOST_ENTITY_TYPE, pos, state);
    }

    public void setMimickedBlockId(@Nullable Identifier id) {
        this.mimickedBlockId = id;
    }

    @Nullable
    public Identifier getMimickedBlockId() {
        return mimickedBlockId;
    }
}

