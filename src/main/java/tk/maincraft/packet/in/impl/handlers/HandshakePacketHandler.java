package tk.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import tk.maincraft.network.NetUtils;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.HandshakePacket;
import tk.maincraft.packet.in.impl.InputHandshakePacket;


public final class HandshakePacketHandler extends AbstractPacketHandler<HandshakePacket> {
    @Override
    public HandshakePacket doRead(PacketClient client) throws IOException {
        String username = NetUtils.readString(client, 32);

        return new InputHandshakePacket(client, username);
    }
}
