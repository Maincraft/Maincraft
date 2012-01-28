package me.main__.maincraft.packet.out.impl.senders;

import java.io.IOException;

import me.main__.maincraft.network.NetUtils;
import me.main__.maincraft.packet.PacketClient;
import me.main__.maincraft.packet.out.PlayerListItemPacket;

public final class PlayerListItemPacketSender extends AbstractPacketSender<PlayerListItemPacket> {
    @Override
    public void sendData(PacketClient client, PlayerListItemPacket packet) throws IOException {
        NetUtils.writeString(client, packet.getPlayerName());
        client.getDataOutputStream().writeBoolean(packet.getOnlineStatus());
        client.getDataOutputStream().writeShort(packet.getPing());
    }
}
