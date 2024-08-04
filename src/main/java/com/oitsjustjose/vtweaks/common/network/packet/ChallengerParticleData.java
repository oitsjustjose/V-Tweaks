package com.oitsjustjose.vtweaks.common.network.packet;

import com.oitsjustjose.vtweaks.client.ClientProxy;
import com.oitsjustjose.vtweaks.common.util.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ChallengerParticleData(
        float r, float g, float b,
        double x, double y, double z
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ChallengerParticleData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "challenger_particle_packet"));

    public static final StreamCodec<ByteBuf, ChallengerParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            ChallengerParticleData::r,
            ByteBufCodecs.FLOAT,
            ChallengerParticleData::g,
            ByteBufCodecs.FLOAT,
            ChallengerParticleData::b,
            ByteBufCodecs.DOUBLE,
            ChallengerParticleData::x,
            ByteBufCodecs.DOUBLE,
            ChallengerParticleData::y,
            ByteBufCodecs.DOUBLE,
            ChallengerParticleData::z,
            ChallengerParticleData::new
    );

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(ChallengerParticleData data, IPayloadContext ctx) {
        if (ctx.flow().getReceptionSide() == LogicalSide.CLIENT) {
            ctx.enqueueWork(() -> ClientProxy.showDustParticle(data));
        }
    }
}
