package tk.maincraft.packet.out.impl.senders;

import java.io.IOException;

import tk.maincraft.network.NetUtils;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.out.KickPacket;


public final class KickPacketSender extends AbstractPacketSender<KickPacket> {
    @Override
    public void sendData(PacketClient client, KickPacket packet) throws IOException {
        NetUtils.writeString(client, packet.getReason());
    }
}
