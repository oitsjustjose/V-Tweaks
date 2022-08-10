package com.oitsjustjose.vtweaks.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.entity.culling.EntityCullingHandler;
import com.oitsjustjose.vtweaks.common.entity.culling.EntityCullingRule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Map;

public class EntityCullDataLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public EntityCullDataLoader() {
        super(GSON, "culled_entities");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> datamap, @Nonnull ResourceManager manager, ProfilerFiller profiler) {
        EntityCullingHandler.rules.clear();

        datamap.forEach((rl, json) -> {
            try {
                if (json == null) return;

                JsonObject obj = json.getAsJsonObject();

                ArrayList<String> eRaw = new ArrayList<>();
                ArrayList<String> bRaw = new ArrayList<>();
                ArrayList<String> dRaw = new ArrayList<>();

                obj.get("entities").getAsJsonArray().forEach(x -> eRaw.add(x.getAsString()));
                if (obj.has("dimensions")) {
                    obj.get("dimensions").getAsJsonArray().forEach(x -> dRaw.add(x.getAsString()));
                }
                if (obj.has("biomes")) {
                    obj.get("biomes").getAsJsonArray().forEach(x -> bRaw.add(x.getAsString()));
                }

                EntityCullingRule rule = new EntityCullingRule(bRaw, dRaw, eRaw);
                EntityCullingHandler.rules.add(rule);
                VTweaks.getInstance().LOGGER.info("Loaded {} into {}", rl, rule);
            } catch (NullPointerException ex) {
                VTweaks.getInstance().LOGGER.error("Skipping registration of Entity Culling Rule {} due to errors", rl);
                ex.printStackTrace();
            }
        });
    }
}
