package com.oitsjustjose.vtweaks.common.entity.challenger;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import com.oitsjustjose.vtweaks.common.util.Utils;
import com.oitsjustjose.vtweaks.common.util.WeightedCollection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChallengerMobHandler {
    public static final WeightedCollection<ChallengerMob> challengerMobVariants = new WeightedCollection<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void registerEvent(LivingSpawnEvent.CheckSpawn event) {
        if (event.getLevel().isClientSide()) return;
        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get()) return;
        if (MobTweakConfig.GLOBAL_CHALLENGER_MOB_CHANCE.get() <= 0.0D) return;
        if (event.getEntity().getPersistentData().contains("challenger_mob_data")) return;
        if (event.getLevel().getRandom().nextDouble() > MobTweakConfig.GLOBAL_CHALLENGER_MOB_CHANCE.get()) return;


        if (event.getEntity() != null && event.getEntity() instanceof Monster monster) {
            if (isChallengerMob(monster)) return;

            ChallengerMob variant = filterForEntity(monster).pick();
            if (variant != null && canBeChallenger(variant, monster)) {
                variant.apply(monster);
            }
        }

    }

    @SubscribeEvent
    public void registerEvent(LivingDropsEvent event) {
        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get()) return;

        if (event.getEntity() == null) return;
        if (!(event.getEntity() instanceof Monster monster)) return;

        ChallengerMob challenger = getChallengerMob(monster);
        if (challenger == null) return;

        ItemStack loot = challenger.pickLoot();
        if (loot != null) {
            ItemEntity drop = Utils.createItemEntity(monster.getLevel(), monster.getOnPos(), loot);
            event.getDrops().add(drop);
        }

    }

    @SubscribeEvent
    public void registerEvent(LivingHurtEvent event) {
        if (event.getEntity() == null) return;
        if (event.getSource() == null) return;

        // Special attack features for challenger mobs
        if (event.getSource().getDirectEntity() instanceof Monster monster) {
            if (isChallengerMob(monster)) {
                ChallengerMob challenger = getChallengerMob(monster);
                if (challenger == null) return;

                List<MobEffectInstance> eff = challenger.getAttackEffects();
                if (!eff.isEmpty()) {
                    eff.forEach(x -> event.getEntity().addEffect(x));
                }
            }
        }
    }

    private WeightedCollection<ChallengerMob> filterForEntity(Monster e) {
        HashMap<ChallengerMob, Integer> orig = challengerMobVariants.getWeightMap();
        WeightedCollection<ChallengerMob> filtered = new WeightedCollection<>();
        for (Map.Entry<ChallengerMob, Integer> entry : orig.entrySet()) {
            if (canBeChallenger(entry.getKey(), e)) {
                filtered.add(entry.getKey(), entry.getValue());
            }
        }
        return filtered;
    }

    private boolean canBeChallenger(ChallengerMob mob, Monster entity) {
        ResourceLocation type = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        boolean isBl = mob.isEntityFilterIsBlacklist();
        List<ResourceLocation> types = mob.getEntityFilter();
        return (types.contains(type) && !isBl) || (!types.contains(type) && isBl);
    }


    public static boolean isChallengerMob(Monster entity) {
        CompoundTag comp = entity.getPersistentData();
        return comp.contains("challenger_mob_data");
    }

    public static ChallengerMob getChallengerMob(Monster monster) {
        CompoundTag comp = monster.getPersistentData();
        if (comp.contains("challenger_mob_data")) {
            CompoundTag cmd = comp.getCompound("challenger_mob_data");
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