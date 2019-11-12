package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import java.util.ArrayList;
import java.util.Random;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;

import net.minecraft.entity.monster.MonsterEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChallengerParticles
{
    private long last = System.currentTimeMillis();

    @SubscribeEvent
    public void registerEvent(TickEvent.WorldTickEvent event)
    {
        if (!(System.currentTimeMillis() - last >= 10L))
        {
            return;
        }

        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get())
        {
            return;
        }

        last = System.currentTimeMillis();

        ArrayList<MonsterEntity> toRemove = Lists.newArrayList();

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
            VTweaks.getInstance().LOGGER.info("Despawning entity " + monster.getDisplayName().getFormattedText());
            VTweaks.proxy.challengerMobs.remove(monster);
        });
    }
}