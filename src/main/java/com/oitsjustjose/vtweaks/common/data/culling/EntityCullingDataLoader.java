package com.oitsjustjose.vtweaks.common.data.culling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.tweaks.entity.culling.EntityCullingHandler;
import com.oitsjustjose.vtweaks.common.tweaks.entity.culling.EntityCullingRule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Map;

public class EntityCullingDataLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public EntityCullingDataLoader() {
        super(GSON, "culled_entities");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> dataMap, @Nonnull ResourceManager mgr, @NotNull ProfilerFiller prof) {
        EntityCullingHandler.AllRules.clear();

        dataMap.forEach((rl, json) -> {
            try {
                if (json == null) return;

                var obj = json.getAsJsonObject();
                var eRaw = new ArrayList<String>();
                var bRaw = new ArrayList<String>();
                var dRaw = new ArrayList<String>();

                obj.get("entities").getAsJsonArray().forEach(x -> eRaw.add(x.getAsString()));
                if (obj.has("dimensions")) {
                    obj.get("dimensions").getAsJsonArray().forEach(x -> dRaw.add(x.getAsString()));
                }
                if (obj.has("biomes")) {
                    obj.get("biomes").getAsJsonArray().forEach(x -> bRaw.add(x.getAsString()));
                }

                var rule = new EntityCullingRule(bRaw, dRaw, eRaw);
                EntityCullingHandler.AllRules.add(rule);
                VTweaks.getInstance().LOGGER.info("Loaded {} into {}", rl, rule);
            } catch (NullPointerException ex) {
                VTweaks.getInstance().LOGGER.error("Skipping registration of Entity Culling Rule {} due to errors", rl);
                ex.printStackTrace();
            }
        });
    }
}
