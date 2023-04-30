package com.oitsjustjose.vtweaks.common.tweaks.entity;


import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Tweak(eventClass = PlayerInteractEvent.EntityInteract.class, category = "entity")
public class ItemFrameTweak extends VTweak {
    public static final TagKey<Item> GLASS = ItemTags.create(new ResourceLocation(Constants.MOD_ID, "clear_glass"));

    private ForgeConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("Allows any vtweaks:clear_glass to be right-clicked on an item frame to make invisible").define("enableTransparentItemFrameTweak", true);
    }

    @SubscribeEvent
    public void process(PlayerInteractEvent.EntityInteract evt) {
        if (!this.enabled.get()) return;
        if (evt.getTarget() == null || evt.getEntity() == null) return;
        if (!(evt.getTarget() instanceof ItemFrame frame)) return;

        var player = evt.getEntity();
        if (!player.isCrouching()) return;
        if (!player.getMainHandItem().is(GLASS)) return;

        if (frame.isInvisible()) return;
        frame.setInvisible(true);
        // Prevent rotation change
        frame.setRotation(frame.getRotation() - 1);
        evt.setCanceled(true);
        evt.setResult(Event.Result.DENY);
    }
}
