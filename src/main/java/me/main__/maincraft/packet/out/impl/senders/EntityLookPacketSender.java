package me.main__.maincraft.packet.out.impl.senders;

import java.io.IOException;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.EntityLookPacket;

public final class EntityLookPacketSender extends AbstractPacketSender<EntityLookPacket> {
    @Override
    public void sendData(PacketClient client, EntityLookPacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getEntityId());
        client.getDataOutputStream().writeByte(packet.getYaw());
        client.getDataOutputStream().writeByte(packet.getPitch());
    }
}
