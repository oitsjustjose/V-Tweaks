package com.oitsjustjose.vtweaks.common.network;

import com.oitsjustjose.vtweaks.client.ClientProxy;
import com.oitsjustjose.vtweaks.common.entity.ChallengerMob;
import com.oitsjustjose.vtweaks.common.entity.ChallengerMobHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChallengerMobPacket {
    int entityId;
    ChallengerMob challengerMob;

    public ChallengerMobPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.challengerMob = ChallengerMobHandler.getChallengerMobByName(buf.readUtf());
    }

    public ChallengerMobPacket(int entityId, ChallengerMob challengerMob) {
        this.entityId = entityId;
        this.challengerMob = challengerMob;
    }

    public static ChallengerMobPacket decode(FriendlyByteBuf buf) {
        return new ChallengerMobPacket(buf);
    }

    public static void encode(ChallengerMobPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeUtf(msg.challengerMob.toString());
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