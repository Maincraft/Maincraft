package tk.maincraft.packet.out.impl.senders;

import java.io.IOException;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.DestroyEntityPacket;


public final class DestroyEntityPacketSender extends AbstractPacketSender<DestroyEntityPacket> {
    @Override
    public void sendData(PacketClient client, DestroyEntityPacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getEntityId());
    }
}
