package com.maketendo;

import com.maketendo.blockentities.FakeBlockEntity;
import com.maketendo.blocks.FakeBlock;
import com.maketendo.items.FakeBlockItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
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

    /**
     * Registers a ghost version of the given block, mimicking its appearance.
     * Skips AIR and blocks from this mod to prevent recursion.
     */
    public static void registerGhostVersion(Block original, Identifier originalId) {
        if (shouldSkipBlock(original, originalId)) {
            FakeBlockzMod.LOGGER.debug("Skipping block {} due to filter", originalId);
            return;
        }

        try {
            Identifier ghostId = new Identifier(MOD_ID, "fake_" + originalId.getPath());

            FakeBlock ghostBlock = new FakeBlock(FabricBlockSettings.copyOf(original)
                    .nonOpaque()
                    .noCollision()
                    .dropsNothing(), originalId);

            Registry.register(Registries.BLOCK, ghostId, ghostBlock);
            Registry.register(Registries.ITEM, ghostId, new FakeBlockItem(ghostBlock, new FabricItemSettings(), originalId, original));

            GHOST_BLOCKS.add(ghostBlock);
            originalBlockMap.put(ghostBlock, originalId);

            FakeBlockzMod.LOGGER.info("Registered ghost block for {}", originalId);
        } catch (Exception e) {
            FakeBlockzMod.LOGGER.warn("Failed to register ghost block for {}: {}", originalId, e.toString());
        }
    }

    /**
     * Registers the BlockEntityType used by all ghost blocks after all ghost blocks are defined.
     */
    public static void registerBlockEntityType() {
        if (GHOST_BLOCKS.isEmpty()) {
            FakeBlockzMod.LOGGER.warn("No ghost blocks found; skipping BlockEntityType registration.");
            return;
        }

        GHOST_ENTITY_TYPE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MOD_ID, "fake_block"),
                FabricBlockEntityTypeBuilder.create(FakeBlockEntity::new, GHOST_BLOCKS.toArray(new Block[0])).build(null)
        );

        FakeBlockzMod.LOGGER.info("Registered FakeBlockEntity for {} ghost blocks", GHOST_BLOCKS.size());
    }

    /**
     * Automatically registers ghost versions of all vanilla (and modded) blocks,
     * skipping air and any blocks from this mod.
     */
    public static void registerAllVanillaGhostBlocks() {
        for (Block block : Registries.BLOCK) {
            Identifier id = Registries.BLOCK.getId(block);
            registerGhostVersion(block, id);
        }
    }

    /**
     * Gets the original block's identifier that a ghost block mimics.
     */
    public static Identifier getOriginalBlockId(Block ghostBlock) {
        return originalBlockMap.get(ghostBlock);
    }

    /**
     * Whether a block should be skipped from ghost registration.
     */
    public static boolean shouldSkipBlock(Block block, Identifier id) {
        return block == null
                || id == null
                || block == Blocks.AIR
                || id.getNamespace().equals(MOD_ID)
                || block instanceof BedBlock
                || block instanceof DoorBlock;
    }

}
