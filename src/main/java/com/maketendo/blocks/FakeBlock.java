package com.maketendo.blocks;

import net.minecraft.block.Block;

public class FakeBlock extends Block {
    // Unused right now but I'll prob either delete this or find another use
    public FakeBlock(Settings properties) {
        super(properties.noCollision().nonOpaque().strength(0.0F));
    }
}
