package com.oitsjustjose.vtweaks.common.network;

import java.util.function.Predicate;

import com.oitsjustjose.vtweaks.common.util.Constants;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkManager
{
    public SimpleChannel networkWrapper;
    private static final String PROTOCOL_VERSION = "1";

    public NetworkManager()
    {
        networkWrapper = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Constants.MODID, "armor_break"))
                .clientAcceptedVersions(new Predicate<String>()
                {
                    @Override
                    public boolean test(String s)
                    {
                        return PROTOCOL_VERSION.equalsIgnoreCase(s);
                    }
                }).serverAcceptedVersions(new Predicate<String>()
                {
                    @Override
                    public boolean test(String s)
                    {
                        return PROTOCOL_VERSION.equalsIgnoreCase(s);
                    }
                }).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();
    }

}