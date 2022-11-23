package com.oitsjustjose.vtweaks.common.data.helpers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class VTJsonHelpers {
    public static ItemStack deserialize(JsonObject parent, String key) {
        try {
            return ShapedRecipe.itemStackFromJson(parent.getAsJsonObject(key));
        } catch (JsonSyntaxException ex) {
            VTweaks.getInstance().LOGGER.error("Item {} does not exist", parent.get(key).toString());
            ex.printStackTrace();
        }
        return ItemStack.EMPTY;
    }
}
