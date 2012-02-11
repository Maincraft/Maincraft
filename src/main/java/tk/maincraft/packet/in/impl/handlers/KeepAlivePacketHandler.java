package tk.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.KeepAlivePacket;
import tk.maincraft.packet.in.impl.InputKeepAlivePacket;


public final class KeepAlivePacketHandler extends AbstractPacketHandler<KeepAlivePacket> {
    @Override
    public KeepAlivePacket doRead(PacketClient client) throws IOException {
        int id = client.getDataInputStream().readInt();

        return new InputKeepAlivePacket(client, id);
    }
}
