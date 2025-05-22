package com.maketendo;

import com.maketendo.blocks.FakeBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static com.maketendo.FakeBlockzMod.MOD_ID;

public class FakeBlockzBlocks {
    public static final List<Block> GHOST_BLOCKS = new ArrayList<>();

    public static void registerGhostVersion(Block original, Identifier originalId) {
        try {
            if (shouldSkipBlock(original, originalId)) return;

            Block ghost = new Block(FabricBlockSettings.copyOf(original)
                    .nonOpaque()
                    .noCollision()
                    .dropsNothing());

            Identifier ghostId = new Identifier(MOD_ID, originalId.getPath());

            Registry.register(Registries.BLOCK, ghostId, ghost);
            Registry.register(Registries.ITEM, ghostId, new BlockItem(ghost, new FabricItemSettings()));
            GHOST_BLOCKS.add(ghost);
        } catch (Exception e) {
            FakeBlockzMod.LOGGER.warn("Skipping block '{}' due to error: {}", originalId, e.getMessage());
        }
    }

    public static boolean shouldSkipBlock(Block block, Identifier id) {
        if (block == null || id == null) return true;
        if (block == Blocks.AIR) return true;
        String path = id.getPath();
        return path.contains("ghost") || (id.getNamespace().equals(MOD_ID) && path.startsWith("ghost/"));
    }
}
