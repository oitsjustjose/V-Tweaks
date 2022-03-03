package com.oitsjustjose.vtweaks.common.entity;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import com.oitsjustjose.vtweaks.common.util.Utils;
import com.oitsjustjose.vtweaks.common.util.WeightedCollection;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;


import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChallengerMobHandler {
    public static final WeightedCollection<ChallengerMob> challengerMobVariants = new WeightedCollection<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void registerEvent(LivingSpawnEvent event) {
        if (event.getWorld().isRemote()) {
            return;
        }

        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get() || MobTweakConfig.GLOBAL_CHALLENGER_MOB_CHANCE.get() <= 0.0D) {
            return;
        }

        if (event.getEntity().getPersistentData().getBoolean("challenger_mob_checked")) {
            return;
        }

        event.getEntity().getPersistentData().putBoolean("challenger_mob_checked", true);
        if (event.getWorld().getRandom().nextDouble() > MobTweakConfig.GLOBAL_CHALLENGER_MOB_CHANCE.get()) {
            return;
        }

        if (event.getEntity() != null && event.getEntity() instanceof MonsterEntity) {
            MonsterEntity monster = (MonsterEntity) event.getEntity();
            if (isChallengerMob(monster)) return;

            ChallengerMob variant = filterForEntity(monster).pick();
            if (variant != null && canBeChallenger(variant, monster)) {
                variant.apply(monster);
            }
        }
    }

    @SubscribeEvent
    public void registerEvent(LivingDropsEvent event) {
        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get()) {
            return;
        }

        if (event.getEntity() == null || !(event.getEntity() instanceof MonsterEntity)) {
            return;
        }

        MonsterEntity m = (MonsterEntity) event.getEntity();
        ChallengerMob chal = getChallengerMob(m);

        if (chal == null) {
            return;
        }

        ItemStack loot = chal.pickLoot();
        if (loot != null) {
            ItemEntity drop = Utils.createItemEntity(m.getEntityWorld(), m.getPosition(), loot);
            event.getDrops().add(drop);
        }
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
                ChallengerMob chal = getChallengerMob(monster);
                if (chal == null) {
                    return;
                }

                List<EffectInstance> eff = chal.getAttackEffects();
                if (!eff.isEmpty()) {
                    eff.forEach(x -> event.getEntityLiving().addPotionEffect(x));
                }
            }
        }
    }

    private WeightedCollection<ChallengerMob> filterForEntity(MonsterEntity e) {
        HashMap<ChallengerMob, Integer> orig = challengerMobVariants.getWeightMap();
        WeightedCollection<ChallengerMob> filtered = new WeightedCollection<>();
        for(Map.Entry<ChallengerMob, Integer> entry : orig.entrySet()) {
            if(canBeChallenger(entry.getKey(), e)) {
                filtered.add(entry.getKey(), entry.getValue());
            }
        }
        return filtered;
    }

    private boolean canBeChallenger(ChallengerMob mob, MonsterEntity entity) {
        ResourceLocation type = entity.getType().getRegistryName();
        boolean isBl = mob.isEntityFilterIsBlacklist();
        List<ResourceLocation> types = mob.getEntityFilter();
        return (types.contains(type) && !isBl) || (!types.contains(type) && isBl);
    }


    public static boolean isChallengerMob(MonsterEntity entity) {
        CompoundNBT comp = entity.getPersistentData();
        return comp.contains("challenger_mob_data");
    }

    public static ChallengerMob getChallengerMob(MonsterEntity monster) {
        CompoundNBT comp = monster.getPersistentData();
        if (comp.contains("challenger_mob_data")) {
            CompoundNBT cmd = comp.getCompound("challenger_mob_data");
            String type = cmd.getString("variant");
            return getChallengerMobByName(type);
        }
        return null;
    }

    @Nullable
    public static ChallengerMob getChallengerMobByName(String name) {
        for (ChallengerMob t : challengerMobVariants.getCollection()) {
            if (t.getUnlocalizedName().equals(name)) {
                return t;
            }
        }
        return null;
    }
}