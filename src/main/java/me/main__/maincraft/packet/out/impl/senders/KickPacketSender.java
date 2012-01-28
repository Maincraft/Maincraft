package me.main__.maincraft.packet.out.impl.senders;

import java.io.IOException;

import me.main__.maincraft.network.NetUtils;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.KickPacket;

public final class KickPacketSender extends AbstractPacketSender<KickPacket> {
    @Override
    public void sendData(PacketClient client, KickPacket packet) throws IOException {
        NetUtils.writeString(client, packet.getReason());
    }
}
