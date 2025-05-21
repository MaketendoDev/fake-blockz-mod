package com.maketendo.blocks;

import net.minecraft.block.Block;

public class FakeBlock extends Block {
    public FakeBlock(Settings properties) {
        super(properties.noCollision().nonOpaque().strength(0.0F));
    }
}
