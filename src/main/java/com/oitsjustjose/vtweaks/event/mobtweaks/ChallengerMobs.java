package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.util.HelperFunctions;
import com.oitsjustjose.vtweaks.util.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class ChallengerMobs
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void registerEvent(LivingSpawnEvent event)
    {
        if (!ModConfig.mobTweaks.challengerMobs.enabled)
        {
            return;
        }

        if (!event.getWorld().isRemote)
        {
            if (event.getWorld().rand.nextInt(ModConfig.mobTweaks.challengerMobs.chance) == 0)
            {
                final ChallengerMobType VARIANT = ChallengerMobType.values()[event.getWorld().rand.nextInt(8)];

                if (event.getEntity() != null && event.getEntity() instanceof EntityMob)
                {
                    if (event.getEntity() instanceof EntityPigZombie || isBlackListed(event.getEntity()))
                    {
                        return;
                    }

                    EntityMob monster = (EntityMob) event.getEntity();
                    monster.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
                    monster.setItemStackToSlot(EntityEquipmentSlot.CHEST, ItemStack.EMPTY);
                    monster.setItemStackToSlot(EntityEquipmentSlot.LEGS, ItemStack.EMPTY);
                    monster.setItemStackToSlot(EntityEquipmentSlot.FEET, ItemStack.EMPTY);

                    // Custom Name Tags, and infinite fire resistance to prevent cheesy kills
                    monster.setCustomNameTag(mobClassName(VARIANT, monster));
                    // Specifically keeps creepers from spawning with fire resistance to prevent
                    // funny business
                    if (!(monster instanceof EntityCreeper))
                    {
                        monster.addPotionEffect(new PotionEffect(
                                Objects.requireNonNull(
                                        Potion.REGISTRY.getObject(new ResourceLocation("fire_resistance"))),
                                Integer.MAX_VALUE, 0, true, true));
                    }
                    // Every challenger mob will have a main hand item. Done before any checks.
                    monster.setHeldItem(EnumHand.MAIN_HAND, VARIANT.getEquipment());
                    monster.setDropChance(EntityEquipmentSlot.MAINHAND, Float.MIN_VALUE);

                    monster.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(VARIANT.getSpeed());
                    monster.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(VARIANT.getHealth());
                    monster.setHealth(VARIANT.getHealth());

                    // Special Man Pants for Zestonian Mobs
                    if (VARIANT == ChallengerMobType.ZESTONIAN)
                    {
                        ItemStack pants = new ItemStack(Items.GOLDEN_LEGGINGS);
                        pants.setStackDisplayName("Man Pants");
                        pants.addEnchantment(HelperFunctions.getEnchantment("minecraft", "blast_protection"), 5);
                        monster.setItemStackToSlot(EntityEquipmentSlot.LEGS, pants);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void registerEvent(LivingDropsEvent event)
    {
        if (!ModConfig.mobTweaks.challengerMobs.enabled || ModConfig.mobTweaks.challengerMobs.loot.length <= 0)
        {
            return;
        }

        if (event.getEntity() == null || !(event.getEntity() instanceof EntityMob)
                || !isChallengerMob((EntityMob) event.getEntity()))
        {
            return;
        }

        event.getDrops().add(getItem(event.getEntity().world, event.getEntity().getPosition()));
    }

    private boolean isBlackListed(Entity entity)
    {
        for (String s : ModConfig.mobTweaks.challengerMobs.blacklist)
        {
            if (entity.getClass().getName().toLowerCase().contains(s.toLowerCase()))
            {
                return true;
            }
        }
        return false;
    }

    private String mobClassName(ChallengerMobType type, EntityMob mob)
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
        return type.getPrefix() + " " + mobString;
    }

    private EntityItem getItem(World world, BlockPos pos)
    {
        int RNG = world.rand.nextInt(ModConfig.MobTweaks.ChallengerMobs.challengerLootTable.size());
        return HelperFunctions.createItemEntity(world, pos,
                ModConfig.MobTweaks.ChallengerMobs.challengerLootTable.get(RNG));
    }

    private boolean isChallengerMob(EntityMob entity)
    {

        String n = entity.getCustomNameTag();

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