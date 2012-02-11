package tk.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import tk.maincraft.network.NetUtils;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.ChatPacket;
import tk.maincraft.packet.in.impl.InputChatPacket;


public final class ChatPacketHandler extends AbstractPacketHandler<ChatPacket> {
    @Override
    public ChatPacket doRead(PacketClient client) throws IOException {
        String message = NetUtils.readString(client, 500);

        return new InputChatPacket(client, message);
    }
}
