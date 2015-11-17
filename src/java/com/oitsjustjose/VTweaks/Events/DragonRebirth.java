package com.oitsjustjose.VTweaks.Events;

import java.util.Random;

import com.oitsjustjose.VTweaks.Achievement.Achievements;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class DragonRebirth
{
	@SubscribeEvent
	public void registerEvent(PlayerInteractEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		ItemStack heldItem = player.getCurrentEquippedItem();
		World world = event.world;
		Block testFor = world.getBlock(event.x, event.y, event.z);
		boolean inEnd = world.provider.dimensionId == 1;
		boolean isValidPyramid = true;
		boolean thermalFoundation = Loader.isModLoaded("ThermalFoundation");

		EntityDragon dragon = new EntityDragon(world);

		// Test that the player is right clicking a block, said block exists,
		// and that it's a dragon egg
		if (event.action == Action.RIGHT_CLICK_BLOCK && testFor != null && testFor == Blocks.dragon_egg)
			// Test that the player is in the end, else output a "go to the end"
			// type message
			if (inEnd)
			{
				// Test that the player is in the end, not holding nothing, and
				// is holding a nether star, else tell you do so.
				if (player.isSneaking() && heldItem != null && heldItem.getItem() == Items.nether_star)
				{
					// Use Thermal Foundation's Enderium Blocks to test that
					// it's on a nice big base
					if (thermalFoundation)
					{
						Block TEStorage = GameRegistry.findBlock("ThermalFoundation", "Storage");

						for (int xMod = -1; xMod < 2; xMod++)
							for (int zMod = -1; zMod < 2; zMod++)
								if (world.getBlock(event.x + xMod, event.y - 1, event.z + zMod) != TEStorage
										|| world.getBlockMetadata(event.x + xMod, event.y - 1, event.z + zMod) != 12)
									isValidPyramid = false;

						for (int xMod = -2; xMod < 3; xMod++)
							for (int zMod = -2; zMod < 3; zMod++)
								if (world.getBlock(event.x + xMod, event.y - 2, event.z + zMod) != TEStorage
										|| world.getBlockMetadata(event.x + xMod, event.y - 2, event.z + zMod) != 12)
									isValidPyramid = false;
					}
					// Or use vanilla's Emerald blocks to test that it's on a
					// nice big base too.
					else
					{
						for (int xMod = -1; xMod < 2; xMod++)
							for (int zMod = -1; zMod < 2; zMod++)
								if (world.getBlock(event.x + xMod, event.y - 1, event.z + zMod) != Blocks.emerald_block)
									isValidPyramid = false;

						for (int xMod = -2; xMod < 3; xMod++)
							for (int zMod = -2; zMod < 3; zMod++)
								if (world.getBlock(event.x + xMod, event.y - 2, event.z + zMod) != Blocks.emerald_block)
									isValidPyramid = false;
					}

					// Perform the ritual and destroy 1 nether star if the
					// pyramid is valid
					if (isValidPyramid)
					{
						spawnRitual(world, event.x, event.y, event.z, dragon);
						spawnKikoku(player);
						player.addStat(Achievements.rebirth, 1);
						--heldItem.stackSize;
					}
					// Otherwise tell you how to fix your beacon, relative to
					// what mods you have available.
					else
						if (!world.isRemote)
							// Not on pyramid of Enderium if ThermalFoundation
							// is installed
							if (thermalFoundation)
							{
								player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
								player.addChatMessage(new ChatComponentTranslation("chat.message.enderium"));
								player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
							}
							// Or not on a pyramid of Emerald blocks if
							// ThermalFoundation isn't installed
							else
							{
								player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
								player.addChatMessage(new ChatComponentTranslation("chat.message.emerald"));
								player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
							}
				}
				else // Not holding a nether star message
					if (!world.isRemote)
					{
						player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
						player.addChatMessage(new ChatComponentTranslation("chat.message.netherstar"));
						player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
					}
			}
			else // Not in The End Message
				if (!world.isRemote)
				{
					player.addChatMessage(new ChatComponentTranslation("chat.message.line"));
					player.addChatMessage(new ChatComponentTranslation("chat.message.end"));
					player.addChatMessage(new ChatComponentTranslation("chat.message.line"));

				}
	}

	void spawnRitual(World world, int x, int y, int z, EntityDragon dragon)
	{
		// Destroy Egg
		world.setBlockToAir(x, y, z);
		// Spawn Dragon
		if (!world.isRemote)
			world.spawnEntityInWorld(dragon);
		Random rand = world.rand;

		// Set 3 x 3 pyramid part of pyramid blocks to air
		for (int xMod = -1; xMod < 2; xMod++)
			for (int zMod = -1; zMod < 2; zMod++)
				world.setBlockToAir(x + xMod, y - 1, z + zMod);

		// Set 5 x 5 pyramid part of pyramid blocks to air
		for (int xMod = -2; xMod < 3; xMod++)
			for (int zMod = -2; zMod < 3; zMod++)
				world.setBlockToAir(x + xMod, y - 2, z + zMod);

		// Spawn an explosion for pretties. Does not do damage
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