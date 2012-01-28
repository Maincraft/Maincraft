package me.main__.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import me.main__.maincraft.network.NetUtils;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.ChatPacket;
import me.main__.maincraft.packet.in.impl.InputChatPacket;

public final class ChatPacketHandler extends AbstractPacketHandler<ChatPacket> {
    @Override
    public ChatPacket doRead(PacketClient client) throws IOException {
        String message = NetUtils.readString(client, 500);

        return new InputChatPacket(client, message);
    }
}
