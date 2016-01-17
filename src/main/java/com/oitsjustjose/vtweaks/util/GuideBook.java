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
	public static ItemStack getChangelog()
	{
		ItemStack changelog = new ItemStack(Items.written_book, 1);
		changelog.setTagCompound(new NBTTagCompound());
		changelog.getTagCompound().setString("title", "_");
		changelog.getTagCompound().setString("author", "Version " + VTweaks.version);
		NBTTagList pages = new NBTTagList();

		ArrayList<String> indiPages = new ArrayList<String>();

		indiPages.add(VTweaks.version + " Changes:" + "\n\n" + "Quickly removed the sign editor tweak. This was not a good idea and has been adjusted");

		for (String s : indiPages)
			pages.appendTag(new NBTTagString(s));

		changelog.setTagInfo("pages", pages);
		changelog.setStackDisplayName(StatCollector.translateToLocal("book.changelog.title"));
		return changelog;
	}

	public static ItemStack getGuideBook()
	{
		ItemStack guideBook = new ItemStack(Items.written_book, 1);
		guideBook.setTagCompound(new NBTTagCompound());
		guideBook.getTagCompound().setString("title", "_");
		guideBook.getTagCompound().setString("author", "Last Written for Version: " + VTweaks.version);
		NBTTagList pages = new NBTTagList();

		ArrayList<String> indiPages = new ArrayList<String>();

		indiPages.add(StatCollector.translateToLocal("book.title.enderdragon") + "\n\n" + "With V-Tweaks, you are able to respawn the ender dragon once it's been defeated. " + "This book will not tell you how, but a Dragon Egg may...");
		indiPages.add(StatCollector.translateToLocal("book.title.hypermending") + "\n\n" + "V-Tweaks adds an enchantment named Hypermending. Similar to the 'Mending' enchantment that the future seems to hold, " + "this enchantment will keep your tools repaired, at NO COST at all. " + EnumChatFormatting.DARK_AQUA + "Combine a Book & Quill + Nether Star in an anvil to craft.");
		indiPages.add(StatCollector.translateToLocal("book.title.autosmelt") + "\n\n" + "An enchantment that is self-explanitory. All drops will be smelted when a tool with this enchant is used. This enchantment " + "stacks with Fortune as well (only on Ores, though). " + EnumChatFormatting.DARK_AQUA + "Combine a Book & Quill + Lava Bucket in an anvil to craft.");
		indiPages.add(StatCollector.translateToLocal("book.title.stepboost") + "\n\n" + "Applied only to boots (randomly obtainable from normal enchanting)" + ", and allows the wearer to walk up 1-block high elevation changes without jumping.");
		indiPages.add(StatCollector.translateToLocal("book.title.lumbering") + "\n\n" + "Lumbering is an enchantment which can only be applied to axes. When applied and the player is sneaking, you can use " + "your axe to cut down a whole tree with one swoop! " + EnumChatFormatting.DARK_AQUA + "Combine a Book & Quill + Gold Axe in an anvil to craft.");
		indiPages.add(StatCollector.translateToLocal("book.title.featherfalling") + "\n\n" + "Feather Falling can be adjusted by V-Tweaks to disable all fall damage when Feather Falling IV is applied to boots. " + "All fall damage negated by your boots will come at *no cost* to durability to your boots.");
		indiPages.add(StatCollector.translateToLocal("book.title.disenchanting") + "\n\n" + "If Botania or ThaumicTinkerer are NOT installed, you can combine a piece of paper with any enchanted item to strip " + "all enchantments off of the item, without damaging the tool.");
		indiPages.add(StatCollector.translateToLocal("book.title.cropharvesting") + "\n\n" + "Crop harvesting is easier than ever with V-Tweaks! Just right click on a crop to harvest it! Works with most crops, including Extra Utilities and Witchery.");
		indiPages.add(StatCollector.translateToLocal("book.title.blocktweaks") + "\n\n" + "Some blocks should really have a proper tool for breaking with... V-Tweaks fixes that with Melons, Hay Bales, Packed Ice, " + "Any Glass Material, Ladders, and Leaves.");
		indiPages.add(StatCollector.translateToLocal("book.title.bonemealtweaks") + "\n\n" + "Bonemeal works on Cactus and Sugar Cane with V-Tweaks, however the max growth height is still ONLY 3 blocks high. " + "Blaze Powder can be used to grow Nether Wart as well!");
		indiPages.add(StatCollector.translateToLocal("book.title.caketweak") + "\n\n" + "An uneaten cake can be broken to be reclaimed! HOORAH! SAVE THE CAKE!");
		indiPages.add(StatCollector.translateToLocal("book.title.challengermobs") + "\n\n" + "Challenger Mobs are custom mobs (can apply to any enemy from any mod) that are equipped with some pretty dangerous items... and buffs. " + "HOWEVER! They drop some great loot, and they're really fun if you can take them down!");
		indiPages.add(StatCollector.translateToLocal("book.title.mobdropbuffs") + "\n\n" + "Many mobs don't drop enough items if you ask me. V-Tweaks has added bonuses for Chickens, Cows, Endermen, Skeletons and Squids!");
		indiPages.add(StatCollector.translateToLocal("book.title.mobkiller") + "\n\n" + "Nobody likes Bats, and some people really don't understand the purpose of Pig Zombies. For that reason, you can disable either/both!");
		indiPages.add(StatCollector.translateToLocal("book.title.stacktweaks") + "\n\n" + "Some things inexplicably do not stack in this game. Well, if you want them to, they do now!");
		indiPages.add(StatCollector.translateToLocal("book.title.horserecipes") + "\n\n" + "Need horse armor? That's perfectly reasonable. Take two *undamaged* pairs of Iron, Gold or Diamond leggings to an Anvil, " + "put one in each slot, and BAM!: for some levels, you've got it!");
		indiPages.add(StatCollector.translateToLocal("book.title.gameplay") + "\n\n" + "Looking for something to spice up the game? Leave the gameplay tweaks enabled in the config and you'll find early-game to " + "be a lot more interesting! Not ridiculously challenging, just realistic!");
		indiPages.add(StatCollector.translateToLocal("book.title.audio") + "\n\n" + "Fixes up a few of the sounds for various objects in the vanilla game, including added sounds for door placement!");
		indiPages.add(StatCollector.translateToLocal("book.title.lightning") + "\n\n" + "Technical Details: turns any ''Thunder Storm'' event into just a normal rain event, preventing lightning. This is good for preventing " + "ghost light blocks and unwanted fires!");
		indiPages.add(StatCollector.translateToLocal("book.title.torch") + "\n\n" + "Right-clicking with any conventional tool places a torch from your inventory! Works with most mod torches and any localization.");
		indiPages.add(StatCollector.translateToLocal("book.title.hush") + "\n\n" + "Allows for a console-spammy method for silencing villager sounds. This feature is disabled by default");
		indiPages.add(StatCollector.translateToLocal("book.title.withersafety") + "\n\n" + "Forces the player to spawn the wither outside of the overworld. Summoning items are returned to the player :)");
		indiPages.add(StatCollector.translateToLocal("book.title.hangars") + "\n\n" + "Forcefully fixes ItemFrames and Paintings and their retarded dropping mannerisms.");
		indiPages.add(StatCollector.translateToLocal("book.title.petarmory") + "\n\n" + "Gear your tamed animal up with Iron, Gold or Diamond Horse Armor! Right click the pet to equip; proper armor protection applied!");

		for (String s : indiPages)
			pages.appendTag(new NBTTagString(s));

		guideBook.setTagInfo("pages", pages);
		guideBook.setStackDisplayName(StatCollector.translateToLocal("book.title"));
		return guideBook;
	}

	@SubscribeEvent
	public void registerEvent(EntityJoinWorldEvent event)
	{
		EntityItem guideEntity = new EntityItem(event.world, event.entity.posX, event.entity.posY, event.entity.posZ, getGuideBook());
		EntityItem changelogEntity = new EntityItem(event.world, event.entity.posX, event.entity.posY, event.entity.posZ, getChangelog());
		final Entity entity = event.entity;
		final String GIVEN_GUIDE_TAG = "givenVTweaksBook";
		final String GIVEN_CHANGELOG_TAG = "givenVTweaksChangelog" + VTweaks.version;

		if (entity == null)
			return;
		if (!event.world.isRemote && entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			NBTTagCompound persistTag = getPlayerPersistTag(player, VTweaks.modid);
			boolean shouldGiveGuide = !persistTag.getBoolean(GIVEN_GUIDE_TAG);
			boolean shouldGiveChangelog = !persistTag.getBoolean(GIVEN_CHANGELOG_TAG);
			if (shouldGiveGuide)
			{
				if (!player.inventory.addItemStackToInventory(getGuideBook()))
					event.world.spawnEntityInWorld(guideEntity);
				persistTag.setBoolean(GIVEN_GUIDE_TAG, true);
			}
			if (shouldGiveChangelog)
			{
				if (!player.inventory.addItemStackToInventory(getChangelog()))
					event.world.spawnEntityInWorld(changelogEntity);
				persistTag.setBoolean(GIVEN_CHANGELOG_TAG, true);
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