package me.main__.maincraft.packet.out.impl.senders;

import java.io.IOException;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.DestroyEntityPacket;

public final class DestroyEntityPacketSender extends AbstractPacketSender<DestroyEntityPacket> {
    @Override
    public void sendData(PacketClient client, DestroyEntityPacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getEntityId());
    }
}
