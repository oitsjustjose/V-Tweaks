package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import com.oitsjustjose.vtweaks.common.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ChallengerMobs {
    private final ArrayList<ItemStack> challengerMobDrops = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void registerEvent(LivingSpawnEvent event) {
        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get() || MobTweakConfig.CHALLENGER_MOBS_RARITY.get() <= 0) {
            return;
        }

        if (!event.getWorld().isClientSide()) {
            // Fix hand items being incorrect
            if (event.getEntity() instanceof Monster) {
                Monster monster = (Monster) event.getEntity();

                if (isChallengerMob(monster)) {
                    ChallengerMobType type = getChallengerMobType(monster);
                    if (monster.getMainHandItem().getItem() != type.getEquipment().getItem()) {
                        monster.setItemInHand(InteractionHand.MAIN_HAND, type.getEquipment());
                    }
                }
            }

            if (event.getWorld().getRandom().nextInt(100) <= MobTweakConfig.CHALLENGER_MOBS_RARITY.get()
                    && !event.getEntity().getPersistentData().getBoolean("challenger_mob_checked")) {
                final ChallengerMobType VARIANT = ChallengerMobType.values()[event.getWorld().getRandom().nextInt(8)];

                if (event.getEntity() != null && event.getEntity() instanceof Monster) {
                    if (event.getEntity() instanceof ZombifiedPiglin || isBlackListed(event.getEntity())) {
                        return;
                    }

                    Monster monster = (Monster) event.getEntity();

                    if (isChallengerMob(monster)) {
                        return;
                    }

                    monster.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                    monster.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                    monster.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
                    monster.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
                    monster.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

                    // Custom Name Tags, and infinite fire resistance to prevent cheesy kills
                    if (MobTweakConfig.ENABLE_CHALLENGER_MOBS_NAME.get()) {
                        monster.setCustomName(mobClassName(VARIANT, monster));
                    }

                    // Every challenger mob will have a main hand item. Done before any checks.
                    monster.setItemInHand(InteractionHand.MAIN_HAND, VARIANT.getEquipment());
                    monster.setDropChance(EquipmentSlot.MAINHAND, Float.MIN_VALUE);

                    monster.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(VARIANT.getSpeed());
                    monster.getAttribute(Attributes.MAX_HEALTH).setBaseValue(VARIANT.getHealth());
                    monster.setHealth(VARIANT.getHealth());

                    // Special Man Pants for Zistonian Mobs
                    if (VARIANT == ChallengerMobType.ZISTONIAN) {
                        ItemStack pants = new ItemStack(Items.GOLDEN_LEGGINGS);
                        pants.setHoverName(new TranslatableComponent("vtweaks.man.pants"));
                        pants.enchant(Utils.getEnchantment("minecraft", "blast_protection"), 5);
                        monster.setItemSlot(EquipmentSlot.LEGS, pants);
                    }

                    setChallengerTag(monster, VARIANT);
                }
            } else {
                event.getEntity().getPersistentData().putBoolean("challenger_mob_checked", true);
            }

        }
    }

    @SubscribeEvent
    public void registerEvent(LivingDropsEvent event) {
        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get() || challengerMobDrops.size() <= 0) {
            return;
        }

        if (event.getEntity() == null || !(event.getEntity() instanceof Monster)
                || !isChallengerMob((Monster) event.getEntity())) {
            return;
        }

        event.getDrops().add(getItem(event.getEntity().level, event.getEntity().getOnPos()));
    }

    @SubscribeEvent
    public void registerEvent(LivingHurtEvent event) {
        if (event.getEntityLiving() == null || event.getSource() == null) {
            return;
        }

        // Special attack features for challenger mobs
        if (event.getSource().getDirectEntity() instanceof Monster) {
            if (isChallengerMob((Monster) event.getSource().getDirectEntity())) {
                Monster monster = (Monster) event.getSource().getDirectEntity();

                ChallengerMobType type = getChallengerMobType(monster);
                if (type.getHitEffects() != null) {
                    for (MobEffectInstance effect : type.getHitEffects()) {
                        event.getEntityLiving().addEffect(effect);
                    }
                }
            }
        }
        // Prevent challenger mobs from being killed by fire
        else if (event.getEntity() instanceof Monster) {
            if (isChallengerMob((Monster) event.getEntity())) {
                if (event.getSource() == DamageSource.IN_FIRE || event.getSource() == DamageSource.ON_FIRE
                        || event.getSource() == DamageSource.LAVA) {
                    event.getEntity().setSecondsOnFire(0);
                    event.getEntity().setRemainingFireTicks(0);
                    event.setAmount(0F);
                    event.setCanceled(true);
                }
            }
        }
    }

    private void process() {
        challengerMobDrops.clear();

        MobTweakConfig.CHALLENGER_MOBS_LOOT.get().forEach((itemName) -> {
            String[] parts = itemName.split("[\\W]");

            if (parts.length != 2 && parts.length != 3) {
                VTweaks.getInstance().LOGGER
                        .error("{} does not conform to <modid:item> or <modid:item*quantity>. Skipping...", itemName);
            }

            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));

            if (item != null) {
                ItemStack i = new ItemStack(item, parts.length == 3 ? Integer.parseInt(parts[2]) : 1);
                VTweaks.getInstance().LOGGER.info("Added {} {} to Challenger Mobs Loot", i.getCount(), i.getItem());
                challengerMobDrops.add(i);
            } else {
                VTweaks.getInstance().LOGGER.error(
                        "{} has no syntax issues but could not be resolved to an item in-game. Skipping...", itemName);
            }
        });
    }

    @SubscribeEvent
    public void onServerStart(final ServerStartedEvent evt) {
        process();
    }

    @SubscribeEvent
    public void onSlashReload(AddReloadListenerEvent evt) {
        evt.addListener(new PreparableReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier p_10638_,
                    ResourceManager p_10639_, ProfilerFiller p_10640_, ProfilerFiller p_10641_, Executor p_10642_,
                    Executor p_10643_) {
                return CompletableFuture.runAsync(() -> {
                    process();
                }, p_10642_).thenCompose(p_10638_::wait);
            }
        });
    }

    private void setChallengerTag(Monster entity, ChallengerMobType variant) {
        CompoundTag comp = entity.getPersistentData();
        CompoundTag type = new CompoundTag();
        type.putString("variant", variant.getPrefix());
        comp.put("challenger_mob_data", type);
    }

    private boolean isBlackListed(Entity entity) {
        if (entity == null) {
            return true;
        }

        for (String str : MobTweakConfig.CHALLENGER_MOBS_BLACKLIST.get()) {
            if (new ResourceLocation(str).equals(entity.getType().getRegistryName())) {
                return true;
            }
        }

        return false;
    }

    private TranslatableComponent mobClassName(ChallengerMobType type, Monster mob) {
        return new TranslatableComponent("vtweaks." + type.getPrefix().toLowerCase() + ".challenger.mob",
                mob.getName());
    }

    private ItemEntity getItem(Level world, BlockPos pos) {
        int RNG = world.getRandom().nextInt(challengerMobDrops.size());
        return Utils.createItemEntity(world, pos, challengerMobDrops.get(RNG));
    }

    public static boolean isChallengerMob(Monster entity) {
        CompoundTag comp = entity.getPersistentData();
        return comp.contains("challenger_mob_data");
    }

    public static ChallengerMobType getChallengerMobType(Monster monster) {
        CompoundTag comp = monster.getPersistentData();

        if (comp.contains("challenger_mob_data")) {
            CompoundTag cmd = comp.getCompound("challenger_mob_data");
            String type = cmd.getString("variant");
            for (ChallengerMobType t : ChallengerMobType.values()) {
                if (t.getPrefix().equalsIgnoreCase(type)) {
                    return t;
                }
            }
        }
        return null;
    }
}