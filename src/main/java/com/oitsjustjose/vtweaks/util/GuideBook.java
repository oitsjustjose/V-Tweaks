package com.oitsjustjose.vtweaks.util;

import java.util.ArrayList;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuideBook
{
	public static ItemStack getGuideBook()
	{
		ItemStack guideBook = new ItemStack(Items.written_book, 1);
		guideBook.setTagCompound(new NBTTagCompound());
		guideBook.getTagCompound().setString("title", "aTitle");
		guideBook.getTagCompound().setString("author", "v " + VTweaks.version);
		NBTTagList pages = new NBTTagList();

		ArrayList<String> indiPages = new ArrayList<String>();

		indiPages.add(tableOfContentsPage1());
		indiPages.add(tableOfContentsPage2());

		indiPages.add(StatCollector.translateToLocal("book.title.enderdragon") + "\n\n"
				+ "With V-Tweaks, you are able to respawn the ender dragon once it's been defeated. "
				+ "This book will not tell you how, but a Dragon Egg may...");
		indiPages.add(StatCollector.translateToLocal("book.title.hypermending") + "\n\n"
				+ "V-Tweaks adds an enchantment named Hypermending. Similar to the 'Mending' enchantment that the future seems to hold, "
				+ "this enchantment will keep your tools repaired, at NO COST at all. " + EnumChatFormatting.DARK_AQUA
				+ "Combine a Book & Quill + Nether Star in an anvil to craft.");
		indiPages.add(StatCollector.translateToLocal("book.title.autosmelt") + "\n\n"
				+ "An enchantment that is self-explanitory. All drops will be smelted when a tool with this enchant is used. This enchantment "
				+ "stacks with Fortune as well (only on Ores, though). " + EnumChatFormatting.DARK_AQUA
				+ "Combine a Book & Quill + Lava Bucket in an anvil to craft.");
		indiPages.add(StatCollector.translateToLocal("book.title.stepboost") + "\n\n"
				+ "Applied only to boots (randomly obtainable from normal enchanting)"
				+ ", and allows the wearer to walk up 1-block high elevation changes without jumping.");
		indiPages.add(StatCollector.translateToLocal("book.title.lumbering") + "\n\n"
				+ "Lumbering is an enchantment which can only be applied to axes. When applied and the player is sneaking, you can use "
				+ "your axe to cut down a whole tree with one swoop! " + EnumChatFormatting.DARK_AQUA
				+ "Combine a Book & Quill + Gold Axe in an anvil to craft.");
		indiPages.add(StatCollector.translateToLocal("book.title.featherfalling") + "\n\n"
				+ "Feather Falling can be adjusted by V-Tweaks to disable all fall damage when Feather Falling IV is applied to boots. "
				+ "All fall damage negated by your boots will come at *no cost* to durability to your boots.");
		indiPages.add(StatCollector.translateToLocal("book.title.disenchanting") + "\n\n"
				+ "If Botania or ThaumicTinkerer are NOT installed, you can combine a piece of paper with any enchanted item to strip "
				+ "all enchantments off of the item, without damaging the tool.");
		indiPages.add(StatCollector.translateToLocal("book.title.cropharvesting") + "\n\n"
				+ "Crop harvesting is easier than ever with V-Tweaks! Just right click on a crop to harvest it! NO SEEDS WILL DROP, this "
				+ "is intended and will not change. Works with most crops, including Extra Utilities and Witchery.");
		indiPages.add(StatCollector.translateToLocal("book.title.blocktweaks") + "\n\n"
				+ "Some blocks should really have a proper tool for breaking with... V-Tweaks fixes that with Melons, Hay Bales, Packed Ice, "
				+ "Any Glass Material, Ladders, and Leaves.");
		indiPages.add(StatCollector.translateToLocal("book.title.bonemealtweaks") + "\n\n"
				+ "Bonemeal works on Cactus and Sugar Cane with V-Tweaks, however the max growth height is still ONLY going to be 3 blocks high. "
				+ "Blaze Powder can be used to grow Nether Wart as well!");
		indiPages.add(StatCollector.translateToLocal("book.title.caketweak") + "\n\n"
				+ "An uneaten cake can be broken to be reclaimed! HOORAH! SAVE THE CAKE!");
		indiPages.add(StatCollector.translateToLocal("book.title.challengermobs") + "\n\n"
				+ "Challenger Mobs are custom mobs (can apply to any enemy from any mod) that are equipped with some pretty dangerous items... and buffs. "
				+ "HOWEVER! They drop some great loot, and they're really fun if you can take them down!");
		indiPages.add(StatCollector.translateToLocal("book.title.mobdropbuffs") + "\n\n"
				+ "Many mobs don't drop enough items if you ask me. V-Tweaks has added bonuses for Chickens, Cows, Endermen, Skeletons and Squids!");
		indiPages.add(StatCollector.translateToLocal("book.title.mobkiller") + "\n\n"
				+ "Nobody likes Bats, and some people really don't understand the purpose of Pig Zombies. For that reason, you can disable either/both!");
		indiPages.add(StatCollector.translateToLocal("book.title.stacktweaks") + "\n\n"
				+ "Some things inexplicably do not stack in this game. Well, if you want them to, they do now!");
		indiPages.add(StatCollector.translateToLocal("book.title.horserecipes") + "\n\n"
				+ "Need horse armor? That's perfectly reasonable. Take two *undamaged* pairs of Iron, Gold or Diamond leggings to an Anvil, "
				+ "put one in each slot, and BAM!: for some levels, you've got it! Wait.. what's that say?");
		indiPages.add(StatCollector.translateToLocal("book.title.gameplay") + "\n\n"
				+ "Looking for something to spice up the game? Leave the gameplay tweaks enabled in the config and you'll find early-game to "
				+ "be a lot more interesting! Not ridiculously challenging, just realistic!");
		

		for (String s : indiPages)
			pages.appendTag(new NBTTagString(s));

		guideBook.setTagInfo("pages", pages);
		guideBook.setStackDisplayName(StatCollector.translateToLocal("book.title"));
		return guideBook;
	}

	static String tableOfContentsPage1()
	{
		return StatCollector.translateToLocal("book.title.tablecontents") + "\n\n" + ">Dragon Rebirth-3" + "\n" + ">Hypermending" + "\n"
				+ "Enchantment-4" + "\n" + ">Auto-Smelt" + "\n" + "   Enchantment-5" + "\n" + ">Step Boost" + "\n" + "   Enchantment-6" + "\n"
				+ "Lumbering Enchantment-7" + "\n" + ">Feather Falling" + "\n" + "   Tweaks-8" + "\n" + ">Disenchanting-9";
	}

	static String tableOfContentsPage2()
	{
		return ">Better Crop" + "\n" + "   Harvesting-10" + "\n" + ">Better Tool" + "\n" + "   Efficiencies-11" + "\n" + ">Bone Meal Tweaks-12" + "\n"
				+ ">Cake Fix-13" + "\n" + ">Challenger Mobs-14" + "\n" + ">Mob Drop Buffs-15" + "\n" + ">Mob Spawning" + "\n" + "   Adjustments-16"
				+ "\n" + ">Stack Size Tweaks-17" + "\n" + ">Horse Armor-18" + "\n" + ">Gameplay Tweaks-19" + "\n";
	}

	@SubscribeEvent
	public void registerEvent(EntityJoinWorldEvent event)
	{
		EntityItem bookEntity = new EntityItem(event.world, event.entity.posX, event.entity.posY, event.entity.posZ, getGuideBook());
		final Entity entity = event.entity;
		final String GIVEN_BOOK_TAG = "givenVTweaksBook" + VTweaks.version;
		if (entity == null)
			return;
		if (!event.world.isRemote && entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			NBTTagCompound persistTag = getPlayerPersistTag(player, VTweaks.modid);
			boolean shouldGive = !persistTag.getBoolean(GIVEN_BOOK_TAG);
			if (shouldGive)
			{
				if (!player.inventory.addItemStackToInventory(getGuideBook()))
					event.world.spawnEntityInWorld(bookEntity);
				persistTag.setBoolean(GIVEN_BOOK_TAG, true);
			}
		}
	}

	// Many thanks to OpenBlocks for this.. I had no idea how to handle this beforehand.
	public NBTTagCompound getPlayerPersistTag(EntityPlayer player, String modID)
	{
		NBTTagCompound tag = player.getEntityData();
		NBTTagCompound persistTag = null;

		if (tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
			persistTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		else
		{
			persistTag = new NBTTagCompound();
			tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistTag);
		}

		NBTTagCompound modTag = null;
		if (persistTag.hasKey(modID))
			modTag = persistTag.getCompoundTag(modID);
		else
		{
			modTag = new NBTTagCompound();
			persistTag.setTag(modID, modTag);
		}

		return modTag;
	}
}