package com.oitsjustjose.vtweaks.common.network;

import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class NetworkManager {
    public SimpleChannel networkWrapper;
    private static final String PROTOCOL_VERSION = "1";

    public NetworkManager() {
        networkWrapper = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Constants.MODID, "armor_break"))
                .clientAcceptedVersions(s -> PROTOCOL_VERSION.equalsIgnoreCase(s))
                .serverAcceptedVersions(s -> PROTOCOL_VERSION.equalsIgnoreCase(s))
                .networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();
    }

}