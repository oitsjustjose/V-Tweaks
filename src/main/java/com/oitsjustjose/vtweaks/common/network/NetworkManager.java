package com.oitsjustjose.vtweaks.common.network;

import com.oitsjustjose.vtweaks.common.network.packet.ChallengerParticleData;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class NetworkManager {
    @SubscribeEvent
    public void register(final RegisterPayloadHandlersEvent evt) {
        var registrar = evt.registrar(Constants.MOD_ID).versioned("1").optional();
        registrar.playToClient(
                ChallengerParticleData.TYPE,
                ChallengerParticleData.STREAM_CODEC,
                ChallengerParticleData::handleClient
        );
    }
}