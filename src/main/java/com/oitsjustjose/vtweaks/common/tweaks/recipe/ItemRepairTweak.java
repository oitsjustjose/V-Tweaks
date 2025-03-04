package com.oitsjustjose.vtweaks.common.tweaks.recipe;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DiggerItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

@Tweak(category = "recipes")
public class ItemRepairTweak extends VTweak {
    public ModConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
        super.registerConfigs(builder);
        this.enabled = builder.comment("Removes the scaling repair cost of items in an anvil").define("enableCheapAnvilRepair", true);
    }

    @SubscribeEvent
    public void process(AnvilUpdateEvent evt) {
        if (evt.getLeft().isEmpty() || evt.getRight().isEmpty()) return;

        if (evt.getLeft().getItem() instanceof DiggerItem tool) {
            if (tool.getTier().getRepairIngredient().test(evt.getRight())) {
                // Remove the repair cost attribute entirely
                evt.getLeft().remove(DataComponents.REPAIR_COST);
                evt.setCost(0);
            }
        }
    }
}
