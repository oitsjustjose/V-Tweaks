package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import java.util.ArrayList;
import java.util.Random;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;

import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChallengerParticles
{
    private long lastParticle = System.currentTimeMillis();
    private long lastFlush = System.currentTimeMillis();

    @SubscribeEvent
    public void registerEvent(TickEvent.WorldTickEvent event)
    {
        if (!(System.currentTimeMillis() - lastParticle >= 10L))
        {
            return;
        }

        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get())
        {
            return;
        }

        lastParticle = System.currentTimeMillis();

        ArrayList<MonsterEntity> toRemove = Lists.newArrayList();

        if (System.currentTimeMillis() - lastFlush >= 1000L)
        {
            if (event.world instanceof ServerWorld)
            {
                VTweaks.proxy.challengerMobs.clear();
                ServerWorld serverWorld = ((ServerWorld) event.world);
                serverWorld.getEntities().forEach((entity) -> {
                    if (entity instanceof MonsterEntity)
                    {
                        MonsterEntity monster = (MonsterEntity) entity;
                        if (ChallengerMobs.isChallengerMob(monster))
                        {
                            ChallengerMobType type = ChallengerMobs.getChallengerMobType(monster);
                            VTweaks.proxy.challengerMobs.put(monster, type);
                        }
                    }
                });
            }
            lastFlush = System.currentTimeMillis();
        }

        VTweaks.proxy.challengerMobs.forEach((monster, type) -> {
            if (!monster.isAlive())
            {
                toRemove.add(monster);
            }
            else
            {

                Random rand = monster.getRNG();

                float noiseX = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;
                float noiseZ = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;

                double x = monster.posX + noiseX;
                double y = rand.nextBoolean() ? monster.posY + (monster.getHeight() / 2) : monster.posY;
                double z = monster.posZ + noiseZ;

                y += rand.nextFloat() + rand.nextInt(1);

                if (type != null)
                {
                    float r = type.getRed();
                    float g = type.getGreen();
                    float b = type.getBlue();

                    VTweaks.proxy.showParticle(x, y, z, r, g, b);
                }
            }
        });

        toRemove.forEach((monster) -> {
            IWorld world = monster.getEntityWorld();
            if (world instanceof ServerWorld)
            {
                ((ServerWorld) world).removeEntity(monster);
            }
            VTweaks.proxy.challengerMobs.remove(monster);
        });
    }
}