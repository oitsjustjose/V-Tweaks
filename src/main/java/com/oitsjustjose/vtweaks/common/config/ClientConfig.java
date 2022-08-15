package com.oitsjustjose.vtweaks.common.config;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ClientConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;
    private static final Builder CLIENT_BUILDER = new Builder();

    public static ForgeConfigSpec.BooleanValue ENABLE_LOW_HEALTH_SOUND;
    public static ForgeConfigSpec.DoubleValue LOW_HEALTH_VOLUME;
    public static ForgeConfigSpec.DoubleValue LOW_HEALTH_THRESHOLD;
    public static ForgeConfigSpec.LongValue LOW_HEALTH_FREQ;

    public static ForgeConfigSpec.BooleanValue ENABLE_SMALL_BEES;

    public static ForgeConfigSpec.BooleanValue ENABLE_CHALLENGER_PARTICLES;

    private static final String CATEGORY_CLIENT = "client_configs";

    static {
        init();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    private static void init() {
        CLIENT_BUILDER.comment("Client Configuration Settings").push(CATEGORY_CLIENT);

        ENABLE_LOW_HEALTH_SOUND = CLIENT_BUILDER
                .comment("Enable a recurring heartbeat sound when health drops below a threshold")
                .define("enableLowHealthSound", true);
        LOW_HEALTH_VOLUME = CLIENT_BUILDER.comment("The volume at which to play the low health sound (if enabled)")
                .defineInRange("lowHealthSoundVolume", .5D, 0D, 1D);
        LOW_HEALTH_THRESHOLD = CLIENT_BUILDER
                .comment("The highest health percentage the player must have to trigger the low health sound")
                .defineInRange("lowHealthSoundThreshold", .25D, 0D, 1D);
        LOW_HEALTH_FREQ = CLIENT_BUILDER
                .comment("The frequency with which the low health sound will play (in milliseconds)")
                .defineInRange("lowHealthSoundFrequency", 2000, 0, Long.MAX_VALUE);

        ENABLE_SMALL_BEES = CLIENT_BUILDER
                .comment("If enabled, all bees will always be half-sized. Does not affect breeding or hitboxes")
                .define("enableSmallBees", true);

        ENABLE_CHALLENGER_PARTICLES = CLIENT_BUILDER
                .comment("Enable colored particles for challenger mobs (dependent on particle setting as well)")
                .define("enableChallengerMobParticles", true);

        CLIENT_BUILDER.pop();
    }
}