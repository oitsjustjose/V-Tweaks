package com.oitsjustjose.vtweaks.common.tweaks.entity;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.compress.utils.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.oitsjustjose.vtweaks.common.util.Constants.MOD_ID;

@Tweak(category = "entity.trader")
public class TraderStonks extends VTweak {

    /* --- CONFIGS --- */
    private ForgeConfigSpec.BooleanValue enabled;
    private ForgeConfigSpec.BooleanValue enableFlavorText;
    private ForgeConfigSpec.BooleanValue saveUnenchanted;
    private ForgeConfigSpec.IntValue numItemsToRestore;

    private ForgeConfigSpec.IntValue minCost;
    private ForgeConfigSpec.IntValue maxCost;

    private ForgeConfigSpec.DoubleValue chanceToSellValuable;

    /* --- CONSTANTS --- */
    public static final String lostItemKey = MOD_ID + ":lost_items";
    public static final String lostItemAddedKey = MOD_ID + ":lost_item_added";
    public static final String skipKey = MOD_ID + ":skipme";
    public static final TagKey<Item> VALUABLE = ItemTags.create(new ResourceLocation(MOD_ID, "valuable"));

    /* --- TRACKING --- */
    private final HashMap<UUID, List<ItemEntity>> trackedValuables = new HashMap<>();
    long last = 0;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        enabled = builder.comment("If enabled, any valuable tools / items lost forever via Cactus, Lava, Void, etc. might be found by the next Wandering Trader and put up for sale!\nValuable items are determined by the vtweaks:valuable tag and, if the item can be enchanted and the config option wanderingTraderRecoverySaveUnenchantedGear is set to true, is enchanted.").define("wanderingTraderRecoveryEnabled", true);
        enableFlavorText = builder.comment("Whether or not to show a silly message when interacting with a trader that has that player's items").define("enableFlavorText", true);
        saveUnenchanted = builder.comment("Determines if a destroyed #vtweaks:valuable should still be saved if it *can* be enchanted but isn't").define("saveUnenchantedGear", false);
        numItemsToRestore = builder.comment("The maximum number of a player's lost items will be sold by a Wandering Trader each visit").defineInRange("numItemsToRestore", 5, 1, Integer.MAX_VALUE);
        minCost = builder.comment("The minimum amount of emeralds a recovered item should cost").defineInRange("minEmeraldCost", 24, 1, 128);
        maxCost = builder.comment("The maximum amount of emeralds a recovered item should cost").defineInRange("maxEmeraldCost", 92, 1, 128);
        chanceToSellValuable = builder.comment("The chance that a given Wandering Trader will offer lost valuables for sale").defineInRange("chanceToSellValuable", 1.F, 0.F, 1.F);

        builder.pop();
    }

    /* --- Item added to world --- */
    @SubscribeEvent
    public void process(ItemTossEvent evt) {
        if (!enabled.get()) return;
        if (evt.getEntity().level().isClientSide()) return;
        if (!evt.getEntity().getItem().is(VALUABLE)) return;

        var uuid = evt.getPlayer().getUUID();
        if (trackedValuables.containsKey(uuid)) {
            trackedValuables.get(uuid).add(evt.getEntity());
        } else {
            trackedValuables.put(uuid, List.of(evt.getEntity()));
        }
    }

    @SubscribeEvent
    public void process(LivingDropsEvent evt) {
        if (!this.enabled.get()) return;
        if (evt.getEntity().level().isClientSide()) return;
        if (!(evt.getEntity() instanceof Player player)) return;

        var uuid = player.getUUID();
        for (var itemEntity : evt.getDrops()) {
            if (!isItemValuable(itemEntity.getItem())) continue;

            if (trackedValuables.containsKey(uuid)) {
                trackedValuables.get(uuid).add(itemEntity);
            } else {
                trackedValuables.put(uuid, List.of(itemEntity));
            }
        }
    }

    /* --- Item removed from world --- */
    @SubscribeEvent
    public void process(PlayerEvent.ItemPickupEvent evt) {
        if (!enabled.get()) return;
        if (evt.getOriginalEntity().level().isClientSide()) return;

        HashMap<UUID, List<ItemEntity>> changes = new HashMap<>();
        for (var entry : trackedValuables.entrySet()) {
            if (entry.getValue().contains(evt.getOriginalEntity())) {
                var cpy = Lists.newArrayList(entry.getValue().listIterator());
                cpy.remove(evt.getOriginalEntity());
                changes.put(entry.getKey(), cpy);
            }
        }

        trackedValuables.putAll(changes);
    }

    /* --- Inject trade --- */
    @SubscribeEvent
    public void process(TickEvent.ServerTickEvent evt) {
        if (!enabled.get()) return;

        var now = System.currentTimeMillis();
        if (now - last < 1000) return;

        HashMap<UUID, List<ItemEntity>> changes = new HashMap<>();

        // Iterate over all players who have lost anything
        for (var entry : trackedValuables.entrySet()) {
            // Iterate over that player's lost items
            for (var valuable : entry.getValue()) {
                if (!valuable.isAlive()) { // It died -- handle that
                    var player = evt.getServer().getPlayerList().getPlayer(entry.getKey());
                    if (player != null) {
                        // Copy the list of items this player has lost and work on the copy
                        var cpy = Lists.newArrayList(entry.getValue().listIterator());
                        cpy.remove(valuable);
                        changes.put(entry.getKey(), cpy);

                        addLostValuableToPlayer(player, valuable);
                    }
                }
            }
        }

        // merge changes back into "main" to avoid CME
        trackedValuables.putAll(changes);
        // new timestamp because processing takes time
        last = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void process(PlayerInteractEvent.EntityInteract evt) {
        if (!enabled.get()) return;
        if (evt.getLevel().isClientSide()) return;

        // Not a trader
        if (!(evt.getTarget() instanceof WanderingTrader trader)) return;
        var traderTag = trader.getPersistentData();
        if (traderTag.contains(skipKey)) return;
        // Use RNG to determine if we should add the skip key to persist between interacts
        if (evt.getLevel().getRandom().nextDouble() > chanceToSellValuable.get()) {
            traderTag.putBoolean(skipKey, true);
            return;
        }

        // Already added a trade to their list, don't add another
        if (traderTag.contains(lostItemAddedKey)) return;

        // Snag the tag off of the player that is currently trading in case this event is a fluke
        var tag = evt.getEntity().getPersistentData();
        if (!tag.contains(lostItemKey)) return;

        // No lost items stored on the player
        var lostItems = tag.getList(lostItemKey, Tag.TAG_COMPOUND);
        if (lostItems.size() == 0) return;

        int addedItemsCount = 0;
        for (int __ = 0; __ < numItemsToRestore.get(); __++) {
            if (lostItems.isEmpty()) break;
            // Pick a lost item,
            var idx = evt.getEntity().getRandom().nextInt(lostItems.size());
            if (!(lostItems.get(idx) instanceof CompoundTag itemTag)) return;
            lostItems.remove(idx);
            // Parse the stack from NBT
            var stack = ItemStack.of(itemTag);
            if (stack.isEmpty()) return;

            var newOffer = makeListing(stack, trader).getOffer(trader, trader.getRandom());
            trader.getOffers().add(newOffer);
            addedItemsCount++;
        }

        traderTag.putBoolean(lostItemAddedKey, true);
        if (enableFlavorText.get()) {
            try {
                var str = addedItemsCount > 1 ? "vtweaks.warranty.message.text.plural" : "vtweaks.warranty.message.text.singular";
                var text = new TranslatableContents(str, null, new Object[]{}).resolve(null, null, 0);
                evt.getEntity().displayClientMessage(text, true);
            } catch (CommandSyntaxException ignored) {
                // It's just a message, no need to worry about it tbh
            }
        }
    }

    private void addLostValuableToPlayer(Player player, ItemEntity valuable) {
        var tag = player.getPersistentData();
        var lostItems = tag.contains(lostItemKey) ? tag.getList(lostItemKey, Tag.TAG_COMPOUND) : new ListTag();

        var itemStackAsTag = new CompoundTag();
        valuable.getItem().save(itemStackAsTag);
        lostItems.add(itemStackAsTag);

        tag.put(lostItemKey, lostItems);
    }

    private boolean isItemValuable(ItemStack stack) {
        if (!stack.is(VALUABLE)) return false;

        if (!saveUnenchanted.get() && stack.isEnchantable()) {
            return stack.isEnchanted();
        }
        return true;
    }

    private BasicItemListing makeListing(ItemStack forSale, WanderingTrader trader) {
        // Determine the cost - if min & max are the same, don't use RNG
        int min = minCost.get();
        int max = maxCost.get();
        var cost = min == max ? min : trader.getRandom().nextInt(min, max);
        var leftOver = Math.max(cost - 64, 0);

        if (leftOver == 0) { // no need to make a second-slot trade cost
            return new BasicItemListing(cost, forSale, 1, 0);
        }

        return new BasicItemListing(new ItemStack(Items.EMERALD, cost - leftOver), new ItemStack(Items.EMERALD, leftOver), forSale, 1, 0, 0.f);
    }
}
