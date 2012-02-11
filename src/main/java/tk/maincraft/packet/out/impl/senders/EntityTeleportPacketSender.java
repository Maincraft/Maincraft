package tk.maincraft.packet.out.impl.senders;

import java.io.IOException;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.EntityTeleportPacket;


public final class EntityTeleportPacketSender extends AbstractPacketSender<EntityTeleportPacket> {
    @Override
    public void sendData(PacketClient client, EntityTeleportPacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getEntityId());
        client.getDataOutputStream().writeInt(packet.getX());
        client.getDataOutputStream().writeInt(packet.getY());
        client.getDataOutputStream().writeInt(packet.getZ());
        client.getDataOutputStream().writeByte(packet.getYaw());
        client.getDataOutputStream().writeByte(packet.getPitch());
    }
}