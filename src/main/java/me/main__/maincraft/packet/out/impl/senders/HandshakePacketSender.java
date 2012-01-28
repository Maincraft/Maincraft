package me.main__.maincraft.packet.out.impl.senders;

import java.io.IOException;

import me.main__.maincraft.network.NetUtils;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.HandshakePacket;

public final class HandshakePacketSender extends AbstractPacketSender<HandshakePacket> {
    @Override
    public void sendData(PacketClient client, HandshakePacket packet) throws IOException {
        NetUtils.writeString(client, packet.getMessage());
    }
}
