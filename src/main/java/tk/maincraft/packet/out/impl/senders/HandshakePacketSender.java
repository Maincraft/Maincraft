package tk.maincraft.packet.out.impl.senders;

import java.io.IOException;

import tk.maincraft.network.NetUtils;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.HandshakePacket;


public final class HandshakePacketSender extends AbstractPacketSender<HandshakePacket> {
    @Override
    public void sendData(PacketClient client, HandshakePacket packet) throws IOException {
        NetUtils.writeString(client, packet.getMessage());
    }
}
