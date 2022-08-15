package com.oitsjustjose.vtweaks.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import com.oitsjustjose.vtweaks.common.entity.challenger.ChallengerMob;
import com.oitsjustjose.vtweaks.common.entity.challenger.ChallengerMobHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import javax.annotation.Nonnull;
import java.util.Map;

public class ChallengerMobDataLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();


    public ChallengerMobDataLoader() {
        super(GSON, "challenger_mobs");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> datamap, @Nonnull ResourceManager manager, ProfilerFiller profiler) {
        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get()) return;

        ChallengerMobHandler.challengerMobVariants.clear();
        datamap.forEach((rl, json) -> {
            try {
                JsonObject obj = json.getAsJsonObject();

                if (obj.entrySet().isEmpty()) {
                    VTweaks.getInstance().LOGGER.info("Challenger mob {} has been disabled", rl);
                } else {
                    ChallengerMob mob = new ChallengerMob(obj);
                    ChallengerMobHandler.challengerMobVariants.add(mob, mob.getWeight());
                    VTweaks.getInstance().LOGGER.info("Successfully added new Challenger Mob {} ({})", mob.getUnlocalizedName(), rl);
                }
            } catch (NullPointerException ex) {
                VTweaks.getInstance().LOGGER.error("Skipping registration of Challenger Mob {} due to errors", rl);
                ex.printStackTrace();
            }
        });
    }
}