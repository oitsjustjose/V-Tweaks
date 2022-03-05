package com.oitsjustjose.vtweaks.common.network;

import com.oitsjustjose.vtweaks.client.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class DustParticlePacket {
    public float r;
    public float g;
    public float b;
    public double x;
    public double y;
    public double z;

    public DustParticlePacket(float r, float g, float b, double x, double y, double z) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public DustParticlePacket(PacketBuffer buf) {
        this.r = buf.readFloat();
        this.g = buf.readFloat();
        this.b = buf.readFloat();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public static DustParticlePacket decode(PacketBuffer buf) {
        return new DustParticlePacket(buf);
    }

    public static void encode(DustParticlePacket msg, PacketBuffer buf) {
        buf.writeFloat(msg.r);
        buf.writeFloat(msg.g);
        buf.writeFloat(msg.b);
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);
    }

    public void handleServer(Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(DustParticlePacket msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> ClientProxy.showDustParticle(msg.r, msg.g, msg.b, msg.x, msg.y, msg.z));
        }
        context.get().setPacketHandled(true);
    }
}
