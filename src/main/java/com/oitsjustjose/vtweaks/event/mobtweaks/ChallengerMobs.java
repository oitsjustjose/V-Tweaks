package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.config.MobTweakConfig;
import com.oitsjustjose.vtweaks.util.HelperFunctions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChallengerMobs
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void registerEvent(LivingSpawnEvent event)
    {
        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get() || MobTweakConfig.CHALLENGER_MOBS_RARITY.get() <= 0)
        {
            return;
        }

        if (!event.getWorld().isRemote())
        {
            if (event.getWorld().getRandom().nextInt(MobTweakConfig.CHALLENGER_MOBS_RARITY.get()) == 0)
            {
                final ChallengerMobType VARIANT = ChallengerMobType.values()[event.getWorld().getRandom().nextInt(8)];

                if (event.getEntity() != null && event.getEntity() instanceof MobEntity)
                {
                    if (event.getEntity() instanceof ZombiePigmanEntity || isBlackListed(event.getEntity()))
                    {
                        return;
                    }

                    MobEntity monster = (MobEntity) event.getEntity();
                    monster.setItemStackToSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                    monster.setItemStackToSlot(EquipmentSlotType.CHEST, ItemStack.EMPTY);
                    monster.setItemStackToSlot(EquipmentSlotType.LEGS, ItemStack.EMPTY);
                    monster.setItemStackToSlot(EquipmentSlotType.FEET, ItemStack.EMPTY);

                    // Custom Name Tags, and infinite fire resistance to prevent cheesy kills
                    monster.setCustomName(mobClassName(VARIANT, monster));
                    // Specifically keeps creepers from spawning with fire resistance to prevent
                    // funny business
                    if (!(monster instanceof CreeperEntity))
                    {
                        monster.addPotionEffect(
                                new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, true));
                    }
                    // Every challenger mob will have a main hand item. Done before any checks.
                    monster.setHeldItem(Hand.MAIN_HAND, VARIANT.getEquipment());
                    monster.setDropChance(EquipmentSlotType.MAINHAND, Float.MIN_VALUE);

                    monster.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(VARIANT.getSpeed());
                    monster.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(VARIANT.getHealth());
                    monster.setHealth(VARIANT.getHealth());

                    // Special Man Pants for Zestonian Mobs
                    if (VARIANT == ChallengerMobType.ZESTONIAN)
                    {
                        ItemStack pants = new ItemStack(Items.GOLDEN_LEGGINGS);
                        pants.setDisplayName(new StringTextComponent(TextFormatting.DARK_PURPLE + "Man Pants"));
                        pants.addEnchantment(HelperFunctions.getEnchantment("minecraft", "blast_protection"), 5);
                        monster.setItemStackToSlot(EquipmentSlotType.LEGS, pants);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void registerEvent(LivingDropsEvent event)
    {
        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get() || MobTweakConfig.challengerMobDrops.size() <= 0)
        {
            return;
        }

        if (event.getEntity() == null || !(event.getEntity() instanceof MobEntity)
                || !isChallengerMob((MobEntity) event.getEntity()))
        {
            return;
        }

        event.getDrops().add(getItem(event.getEntity().world, event.getEntity().getPosition()));
    }

    private boolean isBlackListed(Entity entity)
    {
        for (String s : MobTweakConfig.CHALLENGER_MOBS_BLACKLIST.get())
        {
            if (entity.getClass().getName().toLowerCase().contains(s.toLowerCase()))
            {
                return true;
            }
        }
        return false;
    }

    private ITextComponent mobClassName(ChallengerMobType type, MobEntity mob)
    {
        StringBuilder mobString = new StringBuilder(mob.toString().substring(0, mob.toString().indexOf("[")));
        String[] nameParts = mobString.toString().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
        mobString = new StringBuilder();
        for (int i = 0; i < nameParts.length; i++)
        {
            if (!nameParts[i].toLowerCase().contains("entity"))
            {
                if (i != (nameParts.length - 1))
                {
                    mobString.append(nameParts[i]).append(" ");
                }
                else
                {
                    mobString.append(nameParts[i]);
                }
            }
        }
        return new StringTextComponent(type.getPrefix() + " " + mobString);
    }

    private ItemEntity getItem(World world, BlockPos pos)
    {
        int RNG = world.rand.nextInt(MobTweakConfig.challengerMobDrops.size());
        return HelperFunctions.createItemEntity(world, pos, MobTweakConfig.challengerMobDrops.get(RNG));
    }

    private boolean isChallengerMob(MobEntity entity)
    {

        String n = entity.getCustomName().getFormattedText();

        for (ChallengerMobType type : ChallengerMobType.values())
        {
            if (n.startsWith(type.getPrefix()))
            {
                return true;
            }
        }

        return false;
    }
}