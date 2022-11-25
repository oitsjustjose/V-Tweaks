package com.oitsjustjose.vtweaks.common.network.packet;


import com.oitsjustjose.vtweaks.client.ClientProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChallengerParticlePacket {
    public final float r;
    public final float g;
    public final float b;
    public final double x;
    public final double y;
    public final double z;

    public ChallengerParticlePacket(float r, float g, float b, double x, double y, double z) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ChallengerParticlePacket(FriendlyByteBuf buf) {
        this.r = buf.readFloat();
        this.g = buf.readFloat();
        this.b = buf.readFloat();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public static ChallengerParticlePacket decode(FriendlyByteBuf buf) {
        return new ChallengerParticlePacket(buf);
    }

    public static void encode(ChallengerParticlePacket msg, FriendlyByteBuf buf) {
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
    public static void handleClient(ChallengerParticlePacket msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> ClientProxy.showDustParticle(msg.r, msg.g, msg.b, msg.x, msg.y, msg.z));
        }
        context.get().setPacketHandled(true);
    }
}
