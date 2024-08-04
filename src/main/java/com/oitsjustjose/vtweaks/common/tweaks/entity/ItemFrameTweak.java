package com.oitsjustjose.vtweaks.common.tweaks.entity;


import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Tweak(category = "entity")
public class ItemFrameTweak extends VTweak {
    public static final TagKey<Item> GLASS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "clear_glass"));

    private ModConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
        this.enabled = builder.comment("Allows any vtweaks:clear_glass, or glow ink sac, to be activated on an item frame while sneaking to make the item frame transparent or glowing respectively.").define("enableItemFrameTweak", true);
    }

    @SubscribeEvent
    public void process(PlayerInteractEvent.EntityInteract evt) {
        if (!this.enabled.get()) return;
        if (!(evt.getTarget() instanceof ItemFrame frame)) return;

        var player = evt.getEntity();
        if (!player.isCrouching()) return;

        if (player.getMainHandItem().is(GLASS)) { // Case for glass
            if (frame.isInvisible()) return;
            frame.setInvisible(true);
            player.playSound(SoundEvents.AMETHYST_BLOCK_FALL, 1.f, 1.f);
        } else if (player.getMainHandItem().getItem() == Items.GLOW_INK_SAC) { // Case for Glow Ink
            if (frame instanceof GlowItemFrame) return;
            var glowing = new GlowItemFrame(evt.getLevel(), frame.getPos(), frame.getDirection());
            glowing.getPersistentData().merge(frame.getPersistentData());
            glowing.setItem(frame.getItem());
            glowing.setRotation(frame.getRotation());
            glowing.setInvisible(frame.isInvisible());
            frame.setItem(ItemStack.EMPTY);
            frame.kill();
            evt.getLevel().addFreshEntity(glowing);
            player.playSound(SoundEvents.GLOW_SQUID_AMBIENT, 1.f, 1.f);
            player.getMainHandItem().shrink(1);
        } else {
            return; // invalid item
        }
        player.swing(evt.getHand());
        evt.setCanceled(true);
        evt.setCancellationResult(InteractionResult.CONSUME);
    }
}
