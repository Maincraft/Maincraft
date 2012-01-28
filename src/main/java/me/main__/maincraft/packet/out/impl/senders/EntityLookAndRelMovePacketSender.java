package me.main__.maincraft.packet.out.impl.senders;

import java.io.IOException;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.EntityLookAndRelMovePacket;

public final class EntityLookAndRelMovePacketSender extends AbstractPacketSender<EntityLookAndRelMovePacket> {
    @Override
    public void sendData(PacketClient client, EntityLookAndRelMovePacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getEntityId());
        client.getDataOutputStream().writeByte(packet.getDiffX());
        client.getDataOutputStream().writeByte(packet.getDiffY());
        client.getDataOutputStream().writeByte(packet.getDiffZ());
        client.getDataOutputStream().writeByte(packet.getYaw());
        client.getDataOutputStream().writeByte(packet.getPitch());
    }
}
