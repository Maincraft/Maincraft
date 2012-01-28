package me.main__.maincraft.packet.out.impl.senders;

import java.io.IOException;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.SpawnPositionPacket;

public final class SpawnPositionPacketSender extends AbstractPacketSender<SpawnPositionPacket> {
    @Override
    public void sendData(PacketClient client, SpawnPositionPacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getX());
        client.getDataOutputStream().writeInt(packet.getY());
        client.getDataOutputStream().writeInt(packet.getZ());
    }
}
