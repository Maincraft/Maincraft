package me.main__.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import me.main__.maincraft.network.NetUtils;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.HandshakePacket;
import me.main__.maincraft.packet.in.impl.InputHandshakePacket;

public final class HandshakePacketHandler extends AbstractPacketHandler<HandshakePacket> {
    @Override
    public HandshakePacket doRead(PacketClient client) throws IOException {
        String username = NetUtils.readString(client, 32);

        return new InputHandshakePacket(client, username);
    }
}
