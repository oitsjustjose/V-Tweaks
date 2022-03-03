package com.oitsjustjose.vtweaks.common.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.util.WeightedCollection;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ChallengerMobSer {
    @Nonnull
    public static ItemStack deserializeItemStack(JsonObject parent, String key) {
        if(JSONUtils.hasField(parent, key)) {
            return ShapedRecipe.deserializeItem(parent.getAsJsonObject(key));
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    public static WeightedCollection<ItemStack> deserializeLootTable(JsonArray table) {
        WeightedCollection<ItemStack> loot = new WeightedCollection<>();
        for (JsonElement e : table) {
            JsonObject obj = e.getAsJsonObject();
            int chance = obj.get("weight").getAsInt();
            ItemStack item = deserializeItemStack(obj, "item");
            loot.add(item, chance);
        }
        return loot;
    }

    @Nullable
    public static EffectInstance deserializeEffectInstance(JsonObject json) {
        try {
            String name = json.get("name").getAsString();
            int amplifier = JSONUtils.hasField(json, "amplifier") ? json.get("amplifier").getAsInt() : 0;
            int duration = JSONUtils.hasField(json, "duration") ? json.get("duration").getAsInt() : 100;

            Effect eff = ForgeRegistries.POTIONS.getValue(new ResourceLocation(name));
            if (eff == null) {
                VTweaks.getInstance().LOGGER.info("Effect {} is not a valid effect", name);
                return null;
            }

            return new EffectInstance(eff, duration, amplifier);
        } catch (NullPointerException ex) {
            VTweaks.getInstance().LOGGER.error("Received invalid effect: {}", json);
            VTweaks.getInstance().LOGGER.error(ex);
        }

        return null;
    }

    public static List<EffectInstance> deserializeEffectInstanceList(JsonArray arr) {
        ArrayList<EffectInstance> effs = new ArrayList<>();
        for (JsonElement e : arr) {
            EffectInstance ins = deserializeEffectInstance(e.getAsJsonObject());
            if (ins != null) {
                effs.add(ins);
            }
        }

        return effs;
    }



}
