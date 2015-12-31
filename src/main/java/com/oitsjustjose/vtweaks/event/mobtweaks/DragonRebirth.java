package com.oitsjustjose.vtweaks.event.mobtweaks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class DragonRebirth
{
	@SubscribeEvent
	public void registerEvent(PlayerInteractEvent event)
	{
		if (event.action != event.action.RIGHT_CLICK_BLOCK || event.world.getBlockState(event.pos) == null)
			return;

		EntityPlayer player = event.entityPlayer;
		ItemStack heldItem = player.getCurrentEquippedItem();
		World world = event.world;
		Block testFor = world.getBlockState(event.pos).getBlock();
		boolean inEnd = world.provider.getDimensionId() == 1;
		boolean isValidPyramid = true;
		boolean thermalFoundation = Loader.isModLoaded("ThermalFoundation");
		EntityDragon dragon = new EntityDragon(world);

		if (testFor == Blocks.dragon_egg)
			if (inEnd)
			{
				if (player.isSneaking() && heldItem != null && heldItem.getItem() == Items.nether_star)
				{
					if (thermalFoundation)
					{
						Block TEStorage = GameRegistry.findBlock("ThermalFoundation", "Storage");

						for (int xMod = -1; xMod < 2; xMod++)
							for (int zMod = -1; zMod < 2; zMod++)
								if (world.getBlockState(new BlockPos(event.pos.getX() + xMod, event.pos.getY() - 1, event.pos.getZ() + zMod)).getBlock() != TEStorage || world.getBlockState(
										new BlockPos(event.pos.getX() + xMod, event.pos.getY() - 1, event.pos.getZ() + zMod)).getBlock().getDamageValue(world, new BlockPos(event.pos.getX() + xMod,
												event.pos.getY() - 1, event.pos.getZ() + zMod)) != 12)
									isValidPyramid = false;

						for (int xMod = -2; xMod < 3; xMod++)
							for (int zMod = -2; zMod < 3; zMod++)
								if (world.getBlockState(new BlockPos(event.pos.getX() + xMod, event.pos.getY() - 2, event.pos.getZ() + zMod)).getBlock() != TEStorage || world.getBlockState(
										new BlockPos(event.pos.getX() + xMod, event.pos.getY() - 2, event.pos.getZ() + zMod)).getBlock().getDamageValue(world, new BlockPos(event.pos.getX() + xMod,
												event.pos.getY() - 2, event.pos.getZ() + zMod)) != 12)
									isValidPyramid = false;
					}
					else
					{
						for (int xMod = -1; xMod < 2; xMod++)
							for (int zMod = -1; zMod < 2; zMod++)
								if (world.getBlockState(new BlockPos(event.pos.getX() + xMod, event.pos.getY() - 1, event.pos.getZ() + zMod)).getBlock() != Blocks.emerald_block)
									isValidPyramid = false;

						for (int xMod = -2; xMod < 3; xMod++)
							for (int zMod = -2; zMod < 3; zMod++)
								if (world.getBlockState(new BlockPos(event.pos.getX() + xMod, event.pos.getY() - 2, event.pos.getZ() + zMod)).getBlock() != Blocks.emerald_block)
									isValidPyramid = false;
					}

					if (isValidPyramid)
					{
						spawnRitual(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), dragon);
						spawnKikoku(player);
						--heldItem.stackSize;
					}
					else if (!world.isRemote)
						if (thermalFoundation)
						{
							player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
							player.addChatMessage(new ChatComponentTranslation("chat.message.enderium"));
							player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
						}

						else
						{
							player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
							player.addChatMessage(new ChatComponentTranslation("chat.message.emerald"));
							player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
						}
				}
				else if (!world.isRemote)
				{
					player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
					player.addChatMessage(new ChatComponentTranslation("chat.message.netherstar"));
					player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
				}
			}
			else if (!world.isRemote)
			{
				player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
				player.addChatMessage(new ChatComponentTranslation("chat.message.end"));
				player.addChatMessage(new ChatComponentTranslation("chat.message.line"));

			}
	}

	void spawnRitual(World world, int x, int y, int z, EntityDragon dragon)
	{
		world.setBlockToAir(new BlockPos(x, y, z));

		if (!world.isRemote)
			world.spawnEntityInWorld(dragon);
		Random rand = world.rand;

		for (int xMod = -1; xMod < 2; xMod++)
			for (int zMod = -1; zMod < 2; zMod++)
				world.setBlockToAir(new BlockPos(x + xMod, y - 1, z + zMod));

		for (int xMod = -2; xMod < 3; xMod++)
			for (int zMod = -2; zMod < 3; zMod++)
				world.setBlockToAir(new BlockPos(x + xMod, y - 2, z + zMod));

		for (int explodeX = -5; explodeX < 6; explodeX++)
			for (int explodeY = -5; explodeY < 6; explodeY++)
				for (int explodeZ = -5; explodeZ < 6; explodeZ++)
					world.createExplosion((Entity) null, x + explodeX, y + explodeY, z + explodeZ, 0.0F, false);
	}

	void spawnKikoku(EntityPlayer player)
	{
		if (!Loader.isModLoaded("ExtraUtilities"))
			return;
		Item kikoku = GameRegistry.findItem("ExtraUtilities", "lawSword");
		Item etherealSword = GameRegistry.findItem("ExtraUtilities", "ethericsword");
		if (kikoku == null || etherealSword == null)
			return;

		if (player.inventory.hasItem(etherealSword))
		{
			player.inventory.consumeInventoryItem(etherealSword);
			player.inventory.addItemStackToInventory(new ItemStack(kikoku));
			if (!player.worldObj.isRemote)
				player.addChatMessage(new ChatComponentTranslation("chat.message.weird"));
		}
	}
}