package me.main__.maincraft.packet.out.impl.senders;

import java.io.IOException;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.EntityRelMovePacket;

public final class EntityRelMovePacketSender extends AbstractPacketSender<EntityRelMovePacket> {
    @Override
    public void sendData(PacketClient client, EntityRelMovePacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getEntityId());
        client.getDataOutputStream().writeByte(packet.getDiffX());
        client.getDataOutputStream().writeByte(packet.getDiffY());
        client.getDataOutputStream().writeByte(packet.getDiffZ());
    }
}
