package me.main__.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.in.KeepAlivePacket;
import me.main__.maincraft.packet.in.impl.InputKeepAlivePacket;

public final class KeepAlivePacketHandler extends AbstractPacketHandler<KeepAlivePacket> {
    @Override
    public KeepAlivePacket doRead(PacketClient client) throws IOException {
        int id = client.getDataInputStream().readInt();

        return new InputKeepAlivePacket(client, id);
    }
}
