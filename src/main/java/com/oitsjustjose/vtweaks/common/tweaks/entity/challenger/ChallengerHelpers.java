package com.oitsjustjose.vtweaks.common.tweaks.entity.challenger;

import com.oitsjustjose.vtweaks.common.entity.ChallengerEntityModifier;
import com.oitsjustjose.vtweaks.common.util.WeightedCollection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class ChallengerHelpers {
    public static boolean hasChallengerEntityModifier(Entity entity) {
        CompoundTag comp = entity.getPersistentData();
        return comp.contains("challenger_mob_data");
    }

    public static ChallengerEntityModifier getChallengerEntityModifier(Entity monster) {
        CompoundTag comp = monster.getPersistentData();
        if (comp.contains("challenger_mob_data")) {
            CompoundTag cmd = comp.getCompound("challenger_mob_data");
            String type = cmd.getString("variant");
            return getChallengerEntityModifierByName(type);
        }
        return null;
    }

    @Nullable
    public static ChallengerEntityModifier getChallengerEntityModifierByName(String name) {
        for (ChallengerEntityModifier t : ChallengerEntityModifier.AllVariants.getCollection()) {
            if (t.getUnlocalizedName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    public static WeightedCollection<ChallengerEntityModifier> filterForEntity(Entity entity) {
        var orig = ChallengerEntityModifier.AllVariants.getWeightMap();
        var filtered = new WeightedCollection<ChallengerEntityModifier>();
        for (var entry : orig.entrySet()) {
            if (canBeChallenger(entry.getKey(), entity)) {
                filtered.add(entry.getKey(), entry.getValue());
            }
        }
        return filtered;
    }

    public static boolean canBeChallenger(ChallengerEntityModifier mob, Entity entity) {
        var type = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        var isBl = mob.isEntityFilterIsBlacklist();
        var types = mob.getEntityFilter();
        return (types.contains(type) && !isBl) || (!types.contains(type) && isBl);
    }
}
