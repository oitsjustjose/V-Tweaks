package com.oitsjustjose.vtweaks.common.tweaks.entity;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Tweak(category = "entity")
public class FeatherPluckTweak extends VTweak {
    public static final TagKey<EntityType<?>> CHICKENS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("forge", "chickens"));
    public static final TagKey<Item> SHEARS = ItemTags.create(new ResourceLocation("forge", "shears"));
    private static final String PLUCK_COOL_DOWN_KEY = Constants.MOD_ID + ":pluck_cool_down";

    private ForgeConfigSpec.BooleanValue enabled;
    private ForgeConfigSpec.LongValue cooldown;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("Allows chicken feathers to be plucked w/ shears").define("enableFeatherPlucking", true);
        this.cooldown = builder.comment("The amount of time (in Milliseconds) between plucks. Defaults to 10 minutes.").defineInRange("featurePluckingCooldown", 600000, 1, Long.MAX_VALUE);
    }

    @SubscribeEvent
    public void process(PlayerInteractEvent.EntityInteract evt) {
        if (!this.enabled.get()) return;

        if (evt.getTarget() == null || evt.getEntity() == null) return;
        if (!evt.getTarget().getType().is(CHICKENS)) return;

        var player = evt.getEntity();
        if (!player.getMainHandItem().is(SHEARS)) return;

        if (!canPluck(evt.getTarget())) return;
        player.swing(InteractionHand.MAIN_HAND);
        if (player.getLevel().isClientSide()) return;

        var drop = new ItemEntity(evt.getLevel(), evt.getTarget().getX(), evt.getTarget().getY(), evt.getTarget().getZ(), new ItemStack(Items.FEATHER));
        evt.getLevel().addFreshEntity(drop);
        evt.getTarget().hurt(player.damageSources().generic(), 0.0F);
        setCooldown(evt.getTarget());
        if (!player.isCreative()) {
            player.getMainHandItem().hurt(1, player.getRandom(), null);
        }
    }

    private boolean canPluck(Entity entity) {
        CompoundTag tag = entity.getPersistentData();
        // No pluck cooldown key, means never plucked..
        if (!tag.contains(PLUCK_COOL_DOWN_KEY)) return true;
        long lastTime = tag.getLong(PLUCK_COOL_DOWN_KEY);
        return System.currentTimeMillis() - lastTime >= this.cooldown.get();
    }

    private void setCooldown(Entity entity) {
        CompoundTag tag = entity.getPersistentData();
        tag.putLong(PLUCK_COOL_DOWN_KEY, System.currentTimeMillis());
    }
}
