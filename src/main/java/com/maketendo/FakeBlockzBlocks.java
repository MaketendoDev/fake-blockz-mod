package com.maketendo;

import com.maketendo.blocks.FakeBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static com.maketendo.FakeBlockzMod.MOD_ID;

public class FakeBlockzBlocks {

    public static final List<Block> GHOST_BLOCKS = new ArrayList<>();

    public static void registerGhostBlocks() {
        List<Identifier> blockIds = new ArrayList<>(Registries.BLOCK.getIds());

        for (Identifier id : blockIds) {
            if (id.getNamespace().equals("minecraft")) {
                Block baseBlock = Registries.BLOCK.get(id);

                if (id.getPath().startsWith("ghost_") || baseBlock == Blocks.AIR || baseBlock == null) continue;

                Identifier ghostId = new Identifier(MOD_ID, "ghost_" + id.getPath());

                try {
                    Block ghostBlock = new FakeBlock(AbstractBlock.Settings.copy(baseBlock));
                    Registry.register(Registries.BLOCK, ghostId, ghostBlock);

                    Registry.register(Registries.ITEM, ghostId,
                            new BlockItem(ghostBlock, new FabricItemSettings()));

                    GHOST_BLOCKS.add(ghostBlock);
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping block " + id + " due to state property issues: " + e.getMessage());
                }
            }
        }
    }

}