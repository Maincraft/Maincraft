package me.main__.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import me.main__.maincraft.network.NetUtils;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.KickPacket;
import me.main__.maincraft.packet.in.impl.InputKickPacket;

public final class KickPacketHandler extends AbstractPacketHandler<KickPacket> {
    @Override
    public KickPacket doRead(PacketClient client) throws IOException {
        String reason = NetUtils.readString(client, 1000);

        return new InputKickPacket(client, reason);
    }
}
