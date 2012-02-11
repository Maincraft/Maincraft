package tk.maincraft.packet.out.impl.senders;

import java.io.IOException;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.KeepAlivePacket;


public final class KeepAlivePacketSender extends AbstractPacketSender<KeepAlivePacket> {
    @Override
    public void sendData(PacketClient client, KeepAlivePacket packet) throws IOException {
        client.getDataOutputStream().writeInt(packet.getToken());
    }
}
