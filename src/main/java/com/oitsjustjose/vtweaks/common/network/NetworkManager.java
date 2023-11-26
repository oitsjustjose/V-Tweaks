package com.oitsjustjose.vtweaks.common.network;

import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkManager {
    private static final String PROTOCOL_VERSION = "1";
    public final SimpleChannel networkWrapper;

    public NetworkManager() {
        networkWrapper = NetworkRegistry.newSimpleChannel(new ResourceLocation(Constants.MOD_ID, "main"),
                () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    }
}