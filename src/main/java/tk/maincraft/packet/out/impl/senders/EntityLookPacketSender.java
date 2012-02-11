package tk.maincraft.packet.out.impl.senders;

import java.io.IOException;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.EntityLookPacket;


public final class EntityLookPacketSender extends AbstractPacketSender<EntityLookPacket> {
    @Override
    public void sendData(PacketClient client, EntityLookPacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getEntityId());
        client.getDataOutputStream().writeByte(packet.getYaw());
        client.getDataOutputStream().writeByte(packet.getPitch());
    }
}
