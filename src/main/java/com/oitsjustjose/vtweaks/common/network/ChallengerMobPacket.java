package com.oitsjustjose.vtweaks.common.network;

import com.oitsjustjose.vtweaks.client.ClientProxy;
import com.oitsjustjose.vtweaks.common.entity.ChallengerMob;
import com.oitsjustjose.vtweaks.common.entity.ChallengerMobHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ChallengerMobPacket {
    int entityId;
    ChallengerMob challengerMob;

    public ChallengerMobPacket(PacketBuffer buf) {
        this.entityId = buf.readInt();
        this.challengerMob = ChallengerMobHandler.getChallengerMobByName(buf.readString());
    }

    public ChallengerMobPacket(int entityId, ChallengerMob challenger) {
        this.entityId = entityId;
        this.challengerMob = challenger;
    }

    public static ChallengerMobPacket decode(PacketBuffer buf) {
        return new ChallengerMobPacket(buf);
    }

    public static void encode(ChallengerMobPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeString(msg.challengerMob.toString());
    }

    public void handleServer(Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(ChallengerMobPacket msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> {
                ClientProxy.challengerMobs.put(msg.entityId, msg.challengerMob);
            });
        }
        context.get().setPacketHandled(true);
    }
}