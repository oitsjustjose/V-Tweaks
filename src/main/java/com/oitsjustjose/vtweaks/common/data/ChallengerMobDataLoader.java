package com.oitsjustjose.vtweaks.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.entity.ChallengerMob;
import com.oitsjustjose.vtweaks.common.entity.ChallengerMobHandler;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

public class ChallengerMobDataLoader extends JsonReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();


    public ChallengerMobDataLoader() {
        super(GSON, "challenger_mobs");
    }

    @Override
    public void apply(Map<ResourceLocation, JsonElement> datamap, @Nonnull IResourceManager manager, IProfiler profiler) {
        ChallengerMobHandler.challengerMobVariants.clear();
        datamap.forEach((rl, json) -> {
            try {
                JsonObject obj = json.getAsJsonObject();

                VTweaks.getInstance().LOGGER.info(obj.toString());

                ChallengerMob mob = new ChallengerMob(obj);
                ChallengerMobHandler.challengerMobVariants.add(mob, mob.getWeight());
            } catch (NullPointerException ex) {
                VTweaks.getInstance().LOGGER.error("Skipping registration of Challenger Mob {} due to errors", rl);
                ex.printStackTrace();
            }
        });
    }
}
