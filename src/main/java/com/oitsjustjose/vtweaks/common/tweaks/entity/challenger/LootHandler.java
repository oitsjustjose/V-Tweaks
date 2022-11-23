package com.oitsjustjose.vtweaks.common.tweaks.entity.challenger;

import com.oitsjustjose.vtweaks.common.tweaks.core.Tweak;
import com.oitsjustjose.vtweaks.common.tweaks.core.VTweak;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.Event;

@Tweak(eventClass = LivingDropsEvent.class, category = "entity")
public class LootHandler extends VTweak {
    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
    }

    @Override
    public void process(Event event) {
        var evt = (LivingDropsEvent) event;
        if (evt.getEntity() == null) return;
        if (!(evt.getEntity() instanceof Monster monster)) return;

        var modifier = Helpers.getChallengerEntityModifier(monster);
        if (modifier == null) return;
        var loot = modifier.pickLoot();
        if (loot == null) return;

        var drop = new ItemEntity(monster.getLevel(), monster.getX(), monster.getY(), monster.getZ(), loot);
        evt.getDrops().add(drop);
    }
}
