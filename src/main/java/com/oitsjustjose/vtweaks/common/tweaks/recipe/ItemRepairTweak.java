package com.oitsjustjose.vtweaks.common.tweaks.recipe;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.world.item.DiggerItem;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.Event;

@Tweak(eventClass = AnvilUpdateEvent.class, category = "recipes")
public class ItemRepairTweak extends VTweak {
    public ForgeConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("Makes all anvil tool repairs always cost 1 level of XP").define("enableCheapAnvilRepair", true);
    }

    @Override
    public void process(Event event) {
        var evt = (AnvilUpdateEvent) event;
        if (evt.getLeft().isEmpty() || evt.getRight().isEmpty()) return;

        if (evt.getLeft().getItem() instanceof DiggerItem tool) {
            if (tool.getTier().getRepairIngredient().test(evt.getRight())) {
                evt.getLeft().setRepairCost(0);
                evt.setCost(0);
            }
        }
    }
}
