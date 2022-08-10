package com.oitsjustjose.vtweaks;

import com.oitsjustjose.vtweaks.client.ClientProxy;
import com.oitsjustjose.vtweaks.client.LowHealthSound;
import com.oitsjustjose.vtweaks.client.SmallBees;
import com.oitsjustjose.vtweaks.common.CommonProxy;
import com.oitsjustjose.vtweaks.common.config.ClientConfig;
import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.config.EnchantmentConfig;
import com.oitsjustjose.vtweaks.common.data.ChallengerMobDataLoader;
import com.oitsjustjose.vtweaks.common.data.EntityCullDataLoader;
import com.oitsjustjose.vtweaks.common.enchantment.EnchantmentImperishable;
import com.oitsjustjose.vtweaks.common.enchantment.EnchantmentLumbering;
import com.oitsjustjose.vtweaks.common.enchantment.FeatherFallingTweak;
import com.oitsjustjose.vtweaks.common.enchantment.handler.EnchantmentImperishableHandler;
import com.oitsjustjose.vtweaks.common.enchantment.handler.EnchantmentLumberingHandler;
import com.oitsjustjose.vtweaks.common.entity.challenger.ChallengerMobHandler;
import com.oitsjustjose.vtweaks.common.entity.challenger.ChallengerParticles;
import com.oitsjustjose.vtweaks.common.entity.culling.EntityCullingHandler;
import com.oitsjustjose.vtweaks.common.entity.culling.capability.INBTCapability;
import com.oitsjustjose.vtweaks.common.entity.culling.capability.NBTCapability;
import com.oitsjustjose.vtweaks.common.event.DeathPoint;
import com.oitsjustjose.vtweaks.common.event.StormTweak;
import com.oitsjustjose.vtweaks.common.event.ToolTips;
import com.oitsjustjose.vtweaks.common.event.blocktweaks.*;
import com.oitsjustjose.vtweaks.common.event.itemtweaks.AnvilRepairTweaks;
import com.oitsjustjose.vtweaks.common.event.itemtweaks.ConcreteTweaks;
import com.oitsjustjose.vtweaks.common.event.itemtweaks.DropTweaks;
import com.oitsjustjose.vtweaks.common.event.mobtweaks.*;
import com.oitsjustjose.vtweaks.common.util.Constants;
import com.oitsjustjose.vtweaks.common.util.Recipes;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod(Constants.MODID)
public class VTweaks {
    private static VTweaks instance;
    public Logger LOGGER = LogManager.getLogger();
    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static Enchantment lumbering = new EnchantmentLumbering();
    public static Enchantment imperishable = new EnchantmentImperishable();

    public VTweaks() {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        this.configSetup();
    }

    public static VTweaks getInstance() {
        return instance;
    }

    public void setup(final FMLCommonSetupEvent event) {
        // Proxy init - sets up networking too
        proxy.init();

        // Enchantments
        MinecraftForge.EVENT_BUS.register(new Recipes());

        // Mob Tweaks
        MinecraftForge.EVENT_BUS.register(new PetArmory());
        MinecraftForge.EVENT_BUS.register(new NoPetFriendlyFire());
        MinecraftForge.EVENT_BUS.register(new SmallBees());
        MinecraftForge.EVENT_BUS.register(new FeatherPlucker());
        MinecraftForge.EVENT_BUS.register(new ChallengerMobHandler());
        MinecraftForge.EVENT_BUS.register(new ChallengerParticles());
        MinecraftForge.EVENT_BUS.register(new EntityCullingHandler());
        MinecraftForge.EVENT_BUS.register(new PeacefulSurface());
        MinecraftForge.EVENT_BUS.register(new UngriefedCreepers());

        // Block Tweaks
        MinecraftForge.EVENT_BUS.register(new CropHelper());
        MinecraftForge.EVENT_BUS.register(new BonemealTweaks());
        MinecraftForge.EVENT_BUS.register(new CakeTweak());
        MinecraftForge.EVENT_BUS.register(new ChopDown());
        MinecraftForge.EVENT_BUS.register(new TorchLighting());

        // Item Tweaks
        MinecraftForge.EVENT_BUS.register(new DropTweaks());
        MinecraftForge.EVENT_BUS.register(new ConcreteTweaks());
        ConcreteTweaks.powderBlocks.forEach((item) -> {
            DispenserBlock.registerBehavior(item, ConcreteTweaks.CONCRETE_POWDER_BEHAVIOR_DISPENSE_ITEM);
        });

        // Miscellaneous Features
        MinecraftForge.EVENT_BUS.register(new ToolTips());
        MinecraftForge.EVENT_BUS.register(new StormTweak());
        MinecraftForge.EVENT_BUS.register(new DeathPoint());
        MinecraftForge.EVENT_BUS.register(new AnvilRepairTweaks());
        MinecraftForge.EVENT_BUS.register(new LowHealthSound());
    }

    private void configSetup() {
        ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CLIENT_CONFIG);
        CommonConfig.loadConfig(CommonConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("vtweaks-common.toml"));
        ClientConfig.loadConfig(ClientConfig.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("vtweaks-client.toml"));
    }

    @SubscribeEvent
    public void onSlashReload(AddReloadListenerEvent evt) {
        evt.addListener(new ChallengerMobDataLoader());
        evt.addListener(new EntityCullDataLoader());
    }

    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent evt) {
        evt.register(NBTCapability.class);
    }

    @SubscribeEvent
    public void attachCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject().getLevel().isClientSide()) {
            return;
        }

        try {
            final LazyOptional<INBTCapability> instance = LazyOptional.of(NBTCapability::new);
            final ICapabilitySerializable<CompoundTag> provider = new ICapabilitySerializable<CompoundTag>() {
                @Override
                public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    return NBTCapability.CAP.orEmpty(cap, instance);
                }

                @Override
                public CompoundTag serializeNBT() {
                    INBTCapability cap = this.getCapability(NBTCapability.CAP).orElseThrow(RuntimeException::new);
                    return cap.serialize();
                }

                @Override
                public void deserializeNBT(CompoundTag nbt) {
                    INBTCapability cap = this.getCapability(NBTCapability.CAP).orElseThrow(RuntimeException::new);
                    cap.deserialize(nbt);
                }
            };

            event.addCapability(Constants.ENTITY_CAP, provider);
            event.addListener(instance::invalidate);
        } catch (Exception e) {
            LOGGER.error("VTweaks has faced a fatal error while attaching Entity Capabilities.");
            throw new RuntimeException(e);
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerEnchantments(final RegistryEvent.Register<Enchantment> enchantmentRegistryEvent) {

            if (EnchantmentConfig.ENABLE_LUMBERING.get()) {
                enchantmentRegistryEvent.getRegistry().register(VTweaks.lumbering);
                MinecraftForge.EVENT_BUS.register(new EnchantmentLumberingHandler());
            }

            if (EnchantmentConfig.ENABLE_IMPERISHABLE.get()) {
                enchantmentRegistryEvent.getRegistry().register(VTweaks.imperishable);
                MinecraftForge.EVENT_BUS.register(new EnchantmentImperishableHandler());
            }

            if (EnchantmentConfig.ENABLE_FF_TWEAK.get()) {
                MinecraftForge.EVENT_BUS.register(new FeatherFallingTweak());
            }
        }
    }

}