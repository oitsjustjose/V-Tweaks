package com.oitsjustjose.vtweaks.common.data.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.util.WeightedCollection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class VTJsonHelpers {
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

    public static ArrayList<MobEffectInstance> deserializeEffectInstanceList(JsonArray arr) {
        var effs = new ArrayList<MobEffectInstance>();
        for (var jsonElement : arr) {
            MobEffectInstance ins = deserializeEffectInstance(jsonElement.getAsJsonObject());
            if (ins != null) effs.add(ins);
        }
        return effs;
    }

    @Nullable
    public static MobEffectInstance deserializeEffectInstance(JsonObject json) {
        try {
            var name = json.get("name").getAsString();
            var amplifier = json.has("amplifier") ? json.get("amplifier").getAsInt() : 0;
            var duration = json.has("duration") ? json.get("duration").getAsInt() : 100;

            var effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(name));
            if (effect == null) {
                VTweaks.getInstance().LOGGER.info("Effect {} is not a valid effect", name);
                return null;
            }

            return new MobEffectInstance(effect, duration, amplifier);
        } catch (NullPointerException ex) {
            VTweaks.getInstance().LOGGER.error("Received invalid effect: {}", json);
            VTweaks.getInstance().LOGGER.error(ex);
        }
        return null;
    }

    @Nonnull
    public static WeightedCollection<ItemStack> deserializeLootTable(JsonArray table) {
        WeightedCollection<ItemStack> loot = new WeightedCollection<>();
        for (var jsonElement : table) {
            JsonObject obj = jsonElement.getAsJsonObject();
            int chance = obj.get("weight").getAsInt();
            ItemStack item = deserializeItemStack(obj, "item");
            loot.add(item, chance);
        }
        return loot;
    }
}
