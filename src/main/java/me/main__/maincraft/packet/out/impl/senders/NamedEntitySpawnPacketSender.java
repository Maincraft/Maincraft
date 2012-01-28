package me.main__.maincraft.packet.out.impl.senders;

import java.io.IOException;

import me.main__.maincraft.network.NetUtils;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.NamedEntitySpawnPacket;

public final class NamedEntitySpawnPacketSender extends AbstractPacketSender<NamedEntitySpawnPacket> {
    @Override
    public void sendData(PacketClient client, NamedEntitySpawnPacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getEntityId());
        NetUtils.writeString(client, packet.getPlayerName());
        client.getDataOutputStream().writeInt(packet.getX());
        client.getDataOutputStream().writeInt(packet.getY());
        client.getDataOutputStream().writeInt(packet.getZ());
        client.getDataOutputStream().writeByte(packet.getYaw());
        client.getDataOutputStream().writeByte(packet.getPitch());
        client.getDataOutputStream().writeShort(packet.getCurrentItem());
    }
}
