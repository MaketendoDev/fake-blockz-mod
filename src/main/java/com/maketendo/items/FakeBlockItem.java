package com.maketendo.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FakeBlockItem extends BlockItem {

    private final Identifier originalId;
    private final Block originalBlock;

    /**
     * Constructs a new FakeBlockItem that represents a ghost version of the original block.
     *
     * @param block      The block instance this item places.
     * @param settings   Item settings.
     * @param originalId The Identifier of the original block this fake block mimics.
     */
    public FakeBlockItem(Block block, Settings settings, Identifier originalId, Block originalBlock) {
        super(block, settings);
        this.originalId = originalId;
        this.originalBlock = originalBlock;
    }

    /**
     * Gets the original Identifier of the block this fake block represents.
     *
     * @return the original block's Identifier.
     */
    public Identifier getOriginalId() {
        return originalId;
    }

    /**
     * Gets the original block this fake block copy off.
     *
     * @return the original block.
     */
    public Block getOriginalBlock() {
        return this.originalBlock;
    }

    /**
     * Returns the display name of this item, showing the original block's name with " (Ghost)" appended.
     *
     * @param stack The item stack.
     * @return A text component with the display name.
     */
    @Override
    public Text getName(ItemStack stack) {
        String originalName = originalId != null ? originalId.getPath() : "unknown";
        if (originalName.isEmpty()) {
            originalName = "unknown";
        }
        originalName = originalName.replace('_', ' ');
        String displayName = Character.toUpperCase(originalName.charAt(0)) + originalName.substring(1);

        return Text.literal(displayName + " (Fake)");
    }
}
