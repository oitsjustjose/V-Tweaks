package com.oitsjustjose.vtweaks.common.data.challenger;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.entity.ChallengerEntityModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Map;

public class ChallengerDataLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();


    public ChallengerDataLoader() {
        super(GSON, "challenger_mobs");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> dataMap, @Nonnull ResourceManager mgr, @NotNull ProfilerFiller prof) {
        ChallengerEntityModifier.AllVariants.clear();
        dataMap.forEach((rl, json) -> {
            try {
                JsonObject obj = json.getAsJsonObject();

                if (obj.entrySet().isEmpty()) {
                    VTweaks.getInstance().LOGGER.info("Challenger mob {} has been disabled", rl);
                } else {
                    var modifier = new ChallengerEntityModifier(obj);
                    ChallengerEntityModifier.AllVariants.add(modifier, modifier.getWeight());
                    VTweaks.getInstance().LOGGER.info("Successfully added new Challenger Mob {} ({})", modifier.getUnlocalizedName(), rl);
                }
            } catch (NullPointerException ex) {
                VTweaks.getInstance().LOGGER.error("Skipping registration of Challenger Mob {} due to errors", rl);
                ex.printStackTrace();
            }
        });
    }
}