package tk.maincraft.packet.out.impl.senders;

import java.io.IOException;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.EntityRelMovePacket;


public final class EntityRelMovePacketSender extends AbstractPacketSender<EntityRelMovePacket> {
    @Override
    public void sendData(PacketClient client, EntityRelMovePacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getEntityId());
        client.getDataOutputStream().writeByte(packet.getDiffX());
        client.getDataOutputStream().writeByte(packet.getDiffY());
        client.getDataOutputStream().writeByte(packet.getDiffZ());
    }
}
