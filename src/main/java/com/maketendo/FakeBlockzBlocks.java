package com.maketendo;

import com.maketendo.blockentities.FakeBlockEntity;
import com.maketendo.blocks.FakeBlock;
import com.maketendo.items.FakeBlockItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.*;

import static com.maketendo.FakeBlockzMod.MOD_ID;

public class FakeBlockzBlocks {
    public static final List<Block> GHOST_BLOCKS = new ArrayList<>();
    private static final Map<Block, Identifier> originalBlockMap = new HashMap<>();
    public static BlockEntityType<FakeBlockEntity> GHOST_ENTITY_TYPE;

    public static void registerGhostVersion(Block original, Identifier originalId) {
        try {
            if (shouldSkipBlock(original, originalId)) return;

            FakeBlock ghost = new FakeBlock(FabricBlockSettings.copyOf(original)
                    .nonOpaque()
                    .noCollision()
                    .dropsNothing(), originalId);


            Identifier ghostId = new Identifier(MOD_ID, originalId.getPath());

            Registry.register(Registries.BLOCK, ghostId, ghost);
            Registry.register(Registries.ITEM, ghostId, new FakeBlockItem(ghost, new FabricItemSettings(), originalId, original));

            GHOST_BLOCKS.add(ghost);
            originalBlockMap.put(ghost, originalId);
        } catch (Exception e) {
            FakeBlockzMod.LOGGER.warn("Skipping block '{}' due to error: {}", originalId, e.getMessage());
        }
    }

    public static void registerBlockEntityType() {
        GHOST_ENTITY_TYPE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MOD_ID, "fake_block"),
                FabricBlockEntityTypeBuilder.create(FakeBlockEntity::new, GHOST_BLOCKS.toArray(new Block[0])).build(null)
        );
    }

    public static Identifier getOriginalBlockId(Block ghostBlock) {
        return originalBlockMap.get(ghostBlock);
    }

    public static boolean shouldSkipBlock(Block block, Identifier id) {
        if (block == null || id == null) return true;
        if (block == Blocks.AIR) return true;
        return id.getNamespace().equals(MOD_ID);
    }
}
