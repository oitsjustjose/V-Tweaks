package com.oitsjustjose.vtweaks.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CakeRegistry
{
	private static Map<Block, Item> cakeBlockToItem = new HashMap<Block, Item>();

	@GameRegistry.ObjectHolder("minecraft:cake")
	public static final Block CAKE_BLOCK = null;

	@GameRegistry.ObjectHolder("minecraft:cake")
	public static final Item CAKE_ITEM = null;

	public static void init()
	{
		cakeBlockToItem.put(CAKE_BLOCK, CAKE_ITEM);

		for (Item item : Item.REGISTRY)
		{
			Block block = Block.getBlockFromItem(item);
			if(block instanceof BlockCake)
			{
				cakeBlockToItem.put(block, item);
			}
		};
	}

	public static Item getItemFromCakeBlock(Block block)
	{
		Item item = cakeBlockToItem.get(block);
		if (item == null) item = Item.getItemFromBlock(block);
		return item;
	}
}
