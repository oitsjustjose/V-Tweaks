package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import com.oitsjustjose.vtweaks.common.util.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChallengerMobs {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void registerEvent(LivingSpawnEvent event) {
        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get() || MobTweakConfig.CHALLENGER_MOBS_RARITY.get() <= 0) {
            return;
        }

        if (!event.getWorld().isRemote()) {
            // Fix hand items being incorrect
            if (event.getEntity() instanceof MonsterEntity) {
                MonsterEntity monster = (MonsterEntity) event.getEntity();

                if (isChallengerMob(monster)) {
                    ChallengerMobType type = getChallengerMobType(monster);
                    if (monster.getHeldItemMainhand().getItem() != type.getEquipment().getItem()) {
                        monster.setHeldItem(Hand.MAIN_HAND, type.getEquipment());
                    }
                }
            }

            if (event.getWorld().getRandom().nextInt(100) <= MobTweakConfig.CHALLENGER_MOBS_RARITY.get()
                    && !event.getEntity().getPersistentData().getBoolean("challenger_mob_checked")) {
                final ChallengerMobType VARIANT = ChallengerMobType.values()[event.getWorld().getRandom().nextInt(8)];

                if (event.getEntity() != null && event.getEntity() instanceof MonsterEntity) {
                    if (event.getEntity() instanceof ZombifiedPiglinEntity || isBlackListed(event.getEntity())) {
                        return;
                    }

                    MonsterEntity monster = (MonsterEntity) event.getEntity();

                    if (isChallengerMob(monster)) {
                        return;
                    }

                    monster.setItemStackToSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                    monster.setItemStackToSlot(EquipmentSlotType.CHEST, ItemStack.EMPTY);
                    monster.setItemStackToSlot(EquipmentSlotType.LEGS, ItemStack.EMPTY);
                    monster.setItemStackToSlot(EquipmentSlotType.FEET, ItemStack.EMPTY);
                    monster.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);

                    // Custom Name Tags, and infinite fire resistance to prevent cheesy kills
                    if (MobTweakConfig.ENABLE_CHALLENGER_MOBS_NAME.get()) {
                        monster.setCustomName(mobClassName(VARIANT, monster));
                    }

                    // Every challenger mob will have a main hand item. Done before any checks.
                    monster.setHeldItem(Hand.MAIN_HAND, VARIANT.getEquipment());
                    monster.setDropChance(EquipmentSlotType.MAINHAND, Float.MIN_VALUE);

                    monster.getAttribute(Attributes.field_233821_d_).setBaseValue(VARIANT.getSpeed());
                    monster.getAttribute(Attributes.field_233818_a_).setBaseValue(VARIANT.getHealth());
                    monster.setHealth(VARIANT.getHealth());

                    // Special Man Pants for Zistonian Mobs
                    if (VARIANT == ChallengerMobType.ZISTONIAN) {
                        ItemStack pants = new ItemStack(Items.GOLDEN_LEGGINGS);
                        pants.setDisplayName(new TranslationTextComponent("vtweaks.man.pants"));
                        pants.addEnchantment(Utils.getEnchantment("minecraft", "blast_protection"), 5);
                        monster.setItemStackToSlot(EquipmentSlotType.LEGS, pants);
                    }

                    setChallengerTag(monster, VARIANT);
                }
            } else {
                event.getEntity().getPersistentData().putBoolean("challenger_mob_checked", true);
            }

        }
    }

    @SubscribeEvent
    public void registerEvent(LivingDropsEvent event) {
        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get() || MobTweakConfig.challengerMobDrops.size() <= 0) {
            return;
        }

        if (event.getEntity() == null || !(event.getEntity() instanceof MonsterEntity)
                || !isChallengerMob((MonsterEntity) event.getEntity())) {
            return;
        }

        event.getDrops().add(getItem(event.getEntity().world, event.getEntity().func_233580_cy_()));
    }

    @SubscribeEvent
    public void registerEvent(LivingHurtEvent event) {
        if (event.getEntityLiving() == null || event.getSource() == null) {
            return;
        }

        // Special attack features for challenger mobs
        if (event.getSource().getTrueSource() instanceof MonsterEntity) {
            if (isChallengerMob((MonsterEntity) event.getSource().getTrueSource())) {
                MonsterEntity monster = (MonsterEntity) event.getSource().getTrueSource();

                ChallengerMobType type = getChallengerMobType(monster);
                if (type.getHitEffects() != null) {
                    for (EffectInstance effect : type.getHitEffects()) {
                        event.getEntityLiving().addPotionEffect(effect);
                    }
                }
            }
        }
        // Prevent challenger mobs from being killed by fire
        else if (event.getEntity() instanceof MonsterEntity) {
            if (isChallengerMob((MonsterEntity) event.getEntity())) {
                if (event.getSource() == DamageSource.IN_FIRE || event.getSource() == DamageSource.ON_FIRE
                        || event.getSource() == DamageSource.LAVA) {
                    event.getEntity().extinguish();
                    event.setAmount(0F);
                    event.setCanceled(true);
                }
            }
        }
    }

    private void setChallengerTag(MonsterEntity entity, ChallengerMobType variant) {
        CompoundNBT comp = entity.getPersistentData();
        CompoundNBT type = new CompoundNBT();
        type.putString("variant", variant.getPrefix());
        comp.put("challenger_mob_data", type);
    }

    private boolean isBlackListed(Entity entity) {
        if (entity == null) {
            return true;
        }

        for (String str : MobTweakConfig.CHALLENGER_MOBS_BLACKLIST.get()) {
            if (new ResourceLocation(str).equals(entity.getType().getRegistryName())) {
                return true;
            }
        }

        return false;
    }

    private ITextComponent mobClassName(ChallengerMobType type, MonsterEntity mob) {
        return new TranslationTextComponent("vtweaks." + type.getPrefix().toLowerCase() + ".challenger.mob",
                mob.getName());
    }

    private ItemEntity getItem(World world, BlockPos pos) {
        int RNG = world.rand.nextInt(MobTweakConfig.challengerMobDrops.size());
        return Utils.createItemEntity(world, pos, MobTweakConfig.challengerMobDrops.get(RNG));
    }

    public static boolean isChallengerMob(MonsterEntity entity) {
        CompoundNBT comp = entity.getPersistentData();

        if (comp.contains("challenger_mob_data")) {
            return true;
        }
        return false;
    }

    public static ChallengerMobType getChallengerMobType(MonsterEntity monster) {
        CompoundNBT comp = monster.getPersistentData();

        if (comp.contains("challenger_mob_data")) {
            CompoundNBT cmd = comp.getCompound("challenger_mob_data");
            String type = cmd.getString("variant");
            for (ChallengerMobType t : ChallengerMobType.values()) {
                if (t.getPrefix().equalsIgnoreCase(type)) {
                    return t;
                }
            }
        }
        return null;
    }
}