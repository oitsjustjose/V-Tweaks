package com.oitsjustjose.vtweaks.common.event.itemtweaks;

import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import net.minecraft.world.item.DiggerItem;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AnvilRepairTweaks {
    @SubscribeEvent
    public void registerEvent(AnvilUpdateEvent evt) {
        if (!CommonConfig.ENABLE_CHEAP_ANVIL_REPAIR.get()) return;
        if (evt.getLeft().isEmpty() || evt.getRight().isEmpty()) return;

        if (evt.getLeft().getItem() instanceof DiggerItem tool) {
            if (tool.getTier().getRepairIngredient().test(evt.getRight())) {
                evt.getLeft().setRepairCost(0);
                evt.setCost(0);
            }
        }
    }
}
