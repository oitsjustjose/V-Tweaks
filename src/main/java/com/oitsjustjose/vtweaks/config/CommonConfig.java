package com.oitsjustjose.vtweaks.config;

import java.nio.file.Path;
import java.util.ArrayList;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonConfig
{
    public static final ForgeConfigSpec COMMON_CONFIG;
    private static final Builder COMMON_BUILDER = new Builder();

    public static ForgeConfigSpec.BooleanValue DISABLE_THUNDER_STORMS;
    public static ForgeConfigSpec.BooleanValue ENABLE_DEATH_MESSAGE;
    public static ForgeConfigSpec.EnumValue<FoodTooltips> FOOD_TOOLTIP;
    public static ForgeConfigSpec.EnumValue<DurabilityTooltips> DURABILITY_TOOLTIP;
    public static ForgeConfigSpec.BooleanValue ENABLE_WELCOME_MESSAGE;

    private static String CATEGORY_MISC = "miscellaneous";

    static
    {
        EnchantmentConfig.init(COMMON_BUILDER);
        MobTweakConfig.init(COMMON_BUILDER);
        BlockTweakConfig.init(COMMON_BUILDER);
        ItemTweakConfig.init(COMMON_BUILDER);
        init();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path)
    {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    private static void init()
    {
        COMMON_BUILDER.comment("Miscellaneous").push(CATEGORY_MISC);

        DISABLE_THUNDER_STORMS = COMMON_BUILDER
                .comment("Disables thunder storms, fixing glitched lighting from thunder and other side-effects")
                .define("enableStormTweak", true);
        ENABLE_DEATH_MESSAGE = COMMON_BUILDER.comment("Prints your death point in chat").define("enableDeathPoint",
                true);
        FOOD_TOOLTIP = COMMON_BUILDER.comment("Show food hunger & saturation on item hover")
                .defineEnum("foodTooltipSetting", FoodTooltips.WITH_SHIFT);
        DURABILITY_TOOLTIP = COMMON_BUILDER.comment("Show tool durability on item hover")
                .defineEnum("durabilityTooltipSetting", DurabilityTooltips.WITH_SHIFT);
        ENABLE_WELCOME_MESSAGE = COMMON_BUILDER
                .comment("Show a welcome message in chat when joining a world for the first time")
                .define("enableGuideNotifier", true);

        COMMON_BUILDER.pop();
    }

    public enum FoodTooltips
    {
        NEVER, WITH_SHIFT, ALWAYS
    }

    public enum DurabilityTooltips
    {
        NEVER, WITH_SHIFT, ALWAYS
    }

    public static void setChallengerLootTable(ArrayList<ItemStack> newList)
    {
        // MobTweaks.ChallengerMobs.challengerLootTable = new ArrayList<>();
        // MobTweaks.ChallengerMobs.challengerLootTable.addAll(newList);
    }
}