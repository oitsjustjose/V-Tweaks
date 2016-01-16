package com.oitsjustjose.vtweaks.event.mobtweaks;

import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//This class is for adding special drops to my specially made mobs

public class ChallengerMobsDrops
{
	@SubscribeEvent
	public void registerEvent(LivingDropsEvent event)
	{
		if (event.entity != null && event.entity instanceof EntityMob)
			if (ChallengerMobs.isChallengerMob((EntityMob) event.entity))
				for (int j = 0; j < 2; j++)
					event.drops.add(getItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ));
	}

	EntityItem getItem(World world, double x, double y, double z)
	{
		Random rand = world.rand;
		int i = world.rand.nextInt(100);

		if (0 < i && i < 25)
			return new EntityItem(world, x, y, z, new ItemStack(Items.gold_ingot, rand.nextInt(3) + 1));
		if (25 < i && i < 50)
			return new EntityItem(world, x, y, z, new ItemStack(Items.gold_nugget, rand.nextInt(9) + 10));
		if (50 < i && i < 70)
			return new EntityItem(world, x, y, z, new ItemStack(Items.redstone, rand.nextInt(9) + 10));
		if (70 < i && i < 80)
			return new EntityItem(world, x, y, z, new ItemStack(Items.ghast_tear, rand.nextInt(2)));
		if (80 < i && i < 85)
			return new EntityItem(world, x, y, z, new ItemStack(Items.ender_pearl, rand.nextInt(2)));
		if (i == 85)
			return new EntityItem(world, x, y, z, new ItemStack(Items.record_wait));
		if (i == 86)
			return new EntityItem(world, x, y, z, new ItemStack(Items.record_ward));
		if (i == 87)
			return new EntityItem(world, x, y, z, new ItemStack(Items.record_blocks));
		if (i == 88)
			return new EntityItem(world, x, y, z, new ItemStack(Items.record_cat));
		if (i == 89)
			return new EntityItem(world, x, y, z, new ItemStack(Items.record_chirp));
		if (i == 90)
			return new EntityItem(world, x, y, z, new ItemStack(Items.record_far));
		if (i == 91)
			return new EntityItem(world, x, y, z, new ItemStack(Items.record_mall));
		if (i == 92)
			return new EntityItem(world, x, y, z, new ItemStack(Items.record_mellohi));
		if (i == 93)
			return new EntityItem(world, x, y, z, new ItemStack(Items.record_stal));
		if (i == 94)
			return new EntityItem(world, x, y, z, new ItemStack(Items.record_strad));
		if (i == 95)
			return new EntityItem(world, x, y, z, new ItemStack(Items.record_13));
		if (i == 96)
			return new EntityItem(world, x, y, z, new ItemStack(Items.record_11));
		if (i == 97)
			return new EntityItem(world, x, y, z, new ItemStack(Items.diamond));
		if (i == 98)
			return new EntityItem(world, x, y, z, new ItemStack(Items.emerald));
		if (i == 99)
			return new EntityItem(world, x, y, z, getRandomEnchantedBook());

		return new EntityItem(world, x, y, z, getRandomEnchantedBook());
	}

	ItemStack getRandomEnchantedBook()
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

		return book;
	}
}