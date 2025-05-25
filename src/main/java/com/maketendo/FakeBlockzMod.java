package com.maketendo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;

public class FakeBlockzMod implements ModInitializer {
	public static final String MOD_ID = "fakeblocks";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ItemGroup MAIN_TAB = FabricItemGroup.builder()
			.icon(() -> new ItemStack(Items.BARRIER))
			.displayName(Text.literal("Fake Blockz"))
			.entries((context, entries) -> {
				for (Item item : Registries.ITEM) {
					Identifier id = Registries.ITEM.getId(item);
					if (id.getNamespace().equals(MOD_ID)) {
						entries.add(item);
					}
				}
			})
			.build();

	@Override
	public void onInitialize() {
		FakeBlockzBlocks.registerAllVanillaGhostBlocks();
		FakeBlockzBlocks.registerBlockEntityType();

		Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "fake_blocks"), MAIN_TAB);

		LOGGER.info("Successfully Initialized Fake Blockz Mod!");
	}
}
