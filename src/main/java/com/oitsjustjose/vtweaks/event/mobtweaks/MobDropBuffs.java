package com.oitsjustjose.vtweaks.event.mobtweaks;

import java.util.Random;

import com.oitsjustjose.vtweaks.util.HelperFunctions;
import com.oitsjustjose.vtweaks.util.ModConfig;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobDropBuffs
{
    @SubscribeEvent
    public void registerTweak(LivingDropsEvent event)
    {
        Random random = new Random();

        if (event.getEntity() == null)
        {
            return;
        }

        if (event.getEntity() instanceof EntityChicken && ModConfig.mobTweaks.dropBuffs.chickenFeathers)
        {
            ItemStack dropStack = new ItemStack(Items.FEATHER, 1 + random.nextInt(4));
            event.getDrops().add(HelperFunctions.createItemEntity(event.getEntity().getEntityWorld(),
                    event.getEntity().getPosition(), dropStack));
        }

        else if (event.getEntity() instanceof EntityCow && ModConfig.mobTweaks.dropBuffs.cowLeather)
        {
            ItemStack dropStack = new ItemStack(Items.LEATHER, 1 + random.nextInt(3));
            event.getDrops().add(HelperFunctions.createItemEntity(event.getEntity().getEntityWorld(),
                    event.getEntity().getPosition(), dropStack));
        }

        else if (event.getEntity() instanceof EntitySkeleton && ModConfig.mobTweaks.dropBuffs.skeletonBones)
        {
            ItemStack dropStack = new ItemStack(Items.BONE, 1 + random.nextInt(2));
            event.getDrops().add(HelperFunctions.createItemEntity(event.getEntity().getEntityWorld(),
                    event.getEntity().getPosition(), dropStack));
        }

        else if (event.getEntity() instanceof EntitySquid && ModConfig.mobTweaks.dropBuffs.squidSacs)
        {
            ItemStack dropStack = new ItemStack(Items.DYE, 1 + random.nextInt(3));
            event.getDrops().add(HelperFunctions.createItemEntity(event.getEntity().getEntityWorld(),
                    event.getEntity().getPosition(), dropStack));
        }

        else if (event.getEntity() instanceof EntityEnderman && ModConfig.mobTweaks.dropBuffs.enderPearls)
        {
            ItemStack dropStack = new ItemStack(Items.ENDER_PEARL, 1 + random.nextInt(1));
            event.getDrops().add(HelperFunctions.createItemEntity(event.getEntity().getEntityWorld(),
                    event.getEntity().getPosition(), dropStack));
        }
        else if (event.getEntity() instanceof EntityHorse && ModConfig.mobTweaks.enableHorseGlue
                && random.nextInt(4 - event.getLootingLevel()) == 0)
        {
            if (event.getSource().getTrueSource() instanceof EntityPlayer
                    && EnchantmentHelper.getFireAspectModifier((EntityPlayer) event.getSource().getTrueSource()) > 0)
            {
                ItemStack dropStack = new ItemStack(Items.SLIME_BALL)
                        .setStackDisplayName(TextFormatting.RESET + "Glue Ball");
                event.getDrops().add(HelperFunctions.createItemEntity(event.getEntity().getEntityWorld(),
                        event.getEntity().getPosition(), dropStack));
            }
        }
    }
}