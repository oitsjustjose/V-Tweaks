package com.oitsjustjose.vtweaks.common.entity.challenger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.util.WeightedCollection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ChallengerMobSer {
    @Nonnull
    public static ItemStack deserializeItemStack(JsonObject parent, String key) {
        if (parent.has(key)) {
            try {
                return ShapedRecipe.itemStackFromJson(parent.getAsJsonObject(key));
            } catch (JsonSyntaxException ex) {
                VTweaks.getInstance().LOGGER.error("Item {} does not exist", parent.get(key).toString());
                ex.printStackTrace();
                return ItemStack.EMPTY;
            }
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
    public static MobEffectInstance deserializeEffectInstance(JsonObject json) {
        try {
            String name = json.get("name").getAsString();
            int amplifier = json.has("amplifier") ? json.get("amplifier").getAsInt() : 0;
            int duration = json.has("duration") ? json.get("duration").getAsInt() : 100;

            MobEffect eff = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(name));
            if (eff == null) {
                VTweaks.getInstance().LOGGER.info("Effect {} is not a valid effect", name);
                return null;
            }

            return new MobEffectInstance(eff, duration, amplifier);
        } catch (NullPointerException ex) {
            VTweaks.getInstance().LOGGER.error("Received invalid effect: {}", json);
            VTweaks.getInstance().LOGGER.error(ex);
        }

        return null;
    }

    public static List<MobEffectInstance> deserializeEffectInstanceList(JsonArray arr) {
        ArrayList<MobEffectInstance> effs = new ArrayList<>();
        for (JsonElement e : arr) {
            MobEffectInstance ins = deserializeEffectInstance(e.getAsJsonObject());
            if (ins != null) {
                effs.add(ins);
            }
        }

        return effs;
    }


}
