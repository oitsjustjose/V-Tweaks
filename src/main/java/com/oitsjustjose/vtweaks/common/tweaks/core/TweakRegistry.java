package com.oitsjustjose.vtweaks.common.tweaks.core;

import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import org.apache.commons.compress.utils.Lists;
import org.objectweb.asm.Type;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class TweakRegistry {

    final List<VTweak> vtweaks;

    public TweakRegistry() {
        this.vtweaks = Lists.newArrayList();

        var type = Type.getType(Tweak.class);
        var scanData = ModList.get().getAllScanData();
        var moduleClassNames = new LinkedHashSet<String>();
        scanData.forEach(s -> s.getAnnotations().forEach(a -> {
            if (Objects.equals(a.annotationType(), type)) {
                moduleClassNames.add(a.memberName());
            }
        }));

        moduleClassNames.forEach(clsNm -> {
            try {
                var cls = Class.forName(clsNm);
                var inst = cls.asSubclass(VTweak.class);
                var constructor = inst.getDeclaredConstructor();
                this.vtweaks.add(constructor.newInstance());
            } catch (ReflectiveOperationException | LinkageError e) {
                VTweaks.getInstance().LOGGER.error("Failed to load annotation {}", clsNm, e);
            }
        });
    }

    public List<VTweak> getAllTweaks() {
        return this.vtweaks;
    }

    @SubscribeEvent
    public void onPlayerDo(PlayerEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onBreakSpeedCalculated(PlayerEvent.BreakSpeed event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onEntityAttached(AttackEntityEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onBlockBroken(BlockEvent.BreakEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onLivingSpawnCheck(LivingSpawnEvent.CheckSpawn event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onEntityEvent(EntityEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onLevelEvent(LevelEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onTooltipRender(ItemTooltipEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onItemRightclick(PlayerInteractEvent.RightClickBlock event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onItemTossed(ItemTossEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onEntityInteracted(PlayerInteractEvent.EntityInteract event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onAnimalTamed(AnimalTameEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onExplosion(ExplosionEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        this.vtweaks.stream().filter(x -> x.isForEvent(event)).forEach(x -> x.process(event));
    }
}
