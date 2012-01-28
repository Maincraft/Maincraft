package me.main__.maincraft.packet.out.impl.senders;

import java.io.IOException;

import me.main__.maincraft.network.NetUtils;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.ChatPacket;

public final class ChatPacketSender extends AbstractPacketSender<ChatPacket> {
    @Override
    public void sendData(PacketClient client, ChatPacket packet) throws IOException {
        NetUtils.writeString(client, packet.getMessage());
    }
}
