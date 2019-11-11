package com.oitsjustjose.vtweaks.common.network;

import java.util.function.Supplier;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class ParticlePacket
{
    double x;
    double y;
    double z;

    float r;
    float g;
    float b;

    public ParticlePacket(PacketBuffer buf)
    {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();

        this.r = buf.readFloat();
        this.g = buf.readFloat();
        this.b = buf.readFloat();
    }

    public ParticlePacket(double x, double y, double z, float r, float g, float b)
    {
        this.x = x;
        this.y = y;
        this.z = z;

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static ParticlePacket decode(PacketBuffer buf)
    {
        return new ParticlePacket(buf);
    }

    public static void encode(ParticlePacket msg, PacketBuffer buf)
    {
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);

        buf.writeFloat(msg.r);
        buf.writeFloat(msg.g);
        buf.writeFloat(msg.b);
    }

    public void handleServer(Supplier<NetworkEvent.Context> context)
    {
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(ParticlePacket msg, Supplier<NetworkEvent.Context> context)
    {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
        {
            context.get().enqueueWork(() -> {
                if (MobTweakConfig.ENABLE_CHALLENGER_PARTICLES.get())
                {
                    IParticleData redstoneParticle = new RedstoneParticleData(msg.r, msg.g, msg.b, 1F);
                    Minecraft.getInstance().worldRenderer.addParticle(redstoneParticle, false, msg.x, msg.y, msg.z, 0D,
                            0D, 0D);
                }
            });
        }
        context.get().setPacketHandled(true);
    }
}