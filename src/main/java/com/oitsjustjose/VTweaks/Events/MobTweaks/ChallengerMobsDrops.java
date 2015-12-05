package com.oitsjustjose.VTweaks.Events.MobTweaks;

import java.util.Random;

import com.oitsjustjose.VTweaks.Util.Config;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/*
 * This class is for adding special drops to my
 * specially made mobs
 */

public class ChallengerMobsDrops
{
	@SubscribeEvent
	public void registerEvent(LivingDropsEvent event)
	{
		// Checks if there IS an entity, and then whether it's an enemy
		if (event.entity != null && event.entity instanceof EntityMob)
		{
			// Grabs the nametag name, which we happen to assign in our special cases
			String n = ((EntityMob) event.entity).getCustomNameTag().toLowerCase();
			// If there isn't one, I stop all work here
			if (n == null)
				return;
			// Clones the prefixes from the config file
			String[] preFixes = Config.challengerMobs.clone();

			// Scans through them all
			for (int i = 0; i < preFixes.length; i++)
			{
				// If the mobs' nametag contians one of the prefixes
				if (n.contains(preFixes[i].toLowerCase()))
					// AND the mob has a resistance potion on it
					if (((EntityMob) event.entity).isPotionActive(Potion.resistance))
					{
						// I add a special new drop. This is a good way of preventing exploits, really.
						// Best way of doing this? NBT Tags, but I couldn't care enough to figure that out.
						for (int j = 0; j < 2; j++)
							event.drops.add(getItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ));
						if (event.entity.worldObj.rand.nextInt(100) == 99)
							event.drops.add(getEnchantedBook(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ));
					}
			}
		}
	}

	EntityItem getItem(World world, double x, double y, double z)
	{
		ItemStack drop = null;
		Random rand = new Random();

		switch (rand.nextInt(4))
		{
		case 0:
			drop = new ItemStack(Items.gold_ingot, rand.nextInt(3) + 1);
			break;
		case 1:
			drop = new ItemStack(Items.gold_nugget, rand.nextInt(9) + 10);
			break;
		case 2:
			drop = new ItemStack(Items.diamond);
			break;
		case 3:
			drop = new ItemStack(Items.emerald);
			break;
		}
		return new EntityItem(world, x, y, z, drop);
	}

	// A simple manner of finding a random enchanted book from a list of enchantments I'd like to see.
	EntityItem getEnchantedBook(World world, double x, double y, double z)
	{
		Random rand = new Random();
		ItemStack book = new ItemStack(Items.enchanted_book);

		switch (rand.nextInt(11))
		{
		case 0:
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantment.sharpness, rand.nextInt(5) + 1));
			break;
		case 1:
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantment.fortune, rand.nextInt(3) + 1));
			break;
		case 2:
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantment.baneOfArthropods, rand.nextInt(4) + 1));
			break;
		case 3:
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantment.thorns, rand.nextInt(4) + 1));
			break;
		case 4:
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantment.efficiency, rand.nextInt(5) + 1));
			break;
		case 5:
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantment.looting, rand.nextInt(3) + 1));
			break;
		case 6:
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantment.protection, rand.nextInt(5) + 1));
			break;
		case 7:
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantment.unbreaking, rand.nextInt(3) + 1));
			break;
		case 8:
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantment.featherFalling, rand.nextInt(4) + 1));
			break;
		case 9:
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantment.infinity, 1));
			break;
		case 10:
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantment.power, rand.nextInt(5) + 1));
			break;
		}
		return new EntityItem(world, x, y, z, book);
	}
}