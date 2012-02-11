package tk.maincraft.packet.out.impl.senders;

import java.io.IOException;

import tk.maincraft.network.NetUtils;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.ChatPacket;


public final class ChatPacketSender extends AbstractPacketSender<ChatPacket> {
    @Override
    public void sendData(PacketClient client, ChatPacket packet) throws IOException {
        NetUtils.writeString(client, packet.getMessage());
    }
}
