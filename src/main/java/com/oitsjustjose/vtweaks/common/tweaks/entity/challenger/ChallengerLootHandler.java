package com.oitsjustjose.vtweaks.common.tweaks.entity.challenger;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Tweak(eventClass = LivingDropsEvent.class, category = "entity")
public class ChallengerLootHandler extends VTweak {
    @SubscribeEvent
    public void process(LivingDropsEvent evt) {
        if (evt.getEntity() == null) return;
        if (!(evt.getEntity() instanceof Monster monster)) return;

        var modifier = ChallengerHelpers.getChallengerEntityModifier(monster);
        if (modifier == null) return;
        var loot = modifier.pickLoot();
        if (loot == null) return;

        var drop = new ItemEntity(monster.getLevel(), monster.getX(), monster.getY(), monster.getZ(), loot);
        evt.getDrops().add(drop);
    }
}
