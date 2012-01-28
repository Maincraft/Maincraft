package me.main__.maincraft.packet.out.impl.senders;

import java.io.IOException;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.KeepAlivePacket;

public final class KeepAlivePacketSender extends AbstractPacketSender<KeepAlivePacket> {
    @Override
    public void sendData(PacketClient client, KeepAlivePacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getToken());
    }
}
