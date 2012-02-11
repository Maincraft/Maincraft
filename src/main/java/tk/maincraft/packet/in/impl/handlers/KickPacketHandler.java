package tk.maincraft.packet.in.impl.handlers;

import java.io.IOException;

import tk.maincraft.network.NetUtils;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.in.KickPacket;
import tk.maincraft.packet.in.impl.InputKickPacket;


public final class KickPacketHandler extends AbstractPacketHandler<KickPacket> {
    @Override
    public KickPacket doRead(PacketClient client) throws IOException {
        String reason = NetUtils.readString(client, 1000);

        return new InputKickPacket(client, reason);
    }
}
