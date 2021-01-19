package com.oitsjustjose.vtweaks.common.event.itemtweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.CommonConfig;

import net.minecraft.item.ToolItem;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AnvilRepairTweaks {
    @SubscribeEvent
    public void registerEvent(AnvilUpdateEvent evt) {
        if (!CommonConfig.ENABLE_CHEAP_ANVIL_REPAIR.get()) {
            return;
        }
        if (evt.getLeft().isEmpty() || evt.getRight().isEmpty()) {
            VTweaks.getInstance().LOGGER.info("Nah");
            return;
        }

        if (evt.getLeft().getItem() instanceof ToolItem) {
            ToolItem tool = (ToolItem) evt.getLeft().getItem();
            if (tool.getTier().getRepairMaterial().test(evt.getRight())) {
                evt.getLeft().setRepairCost(0);
                evt.setCost(0);
            }
        }
    }
}
